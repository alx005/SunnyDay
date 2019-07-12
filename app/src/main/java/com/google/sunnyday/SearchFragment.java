package com.google.sunnyday;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.sunnyday.databinding.FragmentSearchBinding;
import com.google.sunnyday.service.model.City;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.utils.Utils;
import com.google.sunnyday.view.adapter.RecyclerViewWeatherAdapter;
import com.google.sunnyday.viewmodel.CityViewModel;
import com.google.sunnyday.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

//@BindingMethods({
//        @BindingMethod(type = android.widget.ToggleButton.class,
//                attribute = "android:background",
//                method = "set_toggle_image") })
public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding binding;
    private androidx.appcompat.widget.SearchView searchView;
    private CityViewModel cViewModel;
    private ArrayList<City> cities = new ArrayList<>();
    private ListView listView;
    private RecyclerViewWeatherAdapter adapter;
    private RecyclerView recyclerView;
    private String savedSearch;
    private List<String> favorites = new ArrayList<>();
    private WeatherViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        Fragment lifecycleOwner = SearchFragment.this;
        viewModel = ViewModelProviders.of(lifecycleOwner).get(WeatherViewModel.class);
        binding.setViewmodel(viewModel);
        View view = binding.getRoot();

        recyclerView = binding.searchRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        adapter = new RecyclerViewWeatherAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupToggleBtn();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause saving query : "+ searchView.getQuery().toString());

        Utils.saveStringToPref(R.string.search, searchView.getQuery().toString(), getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        savedSearch = Utils.getSavedStringWithKey(R.string.search, null, getActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        Log.d(TAG, "onCreateOptionsMenu");
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        MenuItem menu_search = menu.findItem(R.id.app_bar_search);

        searchView = (androidx.appcompat.widget.SearchView) menu_search
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);

        // listening to search query text change
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                //get weather
                if (query != null && query.length() > 0) {
                    binding.cityname.setText(Utils.camelCase(query));
                    getWeatherWithCityName(query);
                    searchView.setQuery(null,false);
                    menu_search.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //add suggestions
                return false;
            }
        });

        //force show the keyboard
        if (savedSearch != null && savedSearch.length() > 0) {
            menu_search.expandActionView();
            searchView.setQuery(savedSearch, false);
        }

    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private void showWorkInProgress() {
        Log.d(TAG, "showWorkInProgress");
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private void showWorkFinished(String JSONString) {
        Log.d(TAG, "showWorkFinished");
    }

    private void getWeatherWithCityName(String cityname) {

        Fragment lifecycleOwner = SearchFragment.this;
        viewModel.setViewModelParams(cityname, null,null, Utils.getDateToday());
        viewModel.getWeatherObservable(true).observe(lifecycleOwner, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather == null){
                    Log.d(TAG, "failed to get from DB, getting from service");
                    viewModel.getWeatherObservable().removeObserver(this);
                    viewModel.getWeatherObservable(false).observe(lifecycleOwner, new Observer<Weather>() {
                        @Override
                        public void onChanged(Weather weather) {
                            if (weather != null) {
                                reloadUIWithWeather(viewModel, weather);
                                viewModel.getWeatherObservable().removeObserver(this);
                            } else {
                                Log.e(TAG, "null weather");
                                binding.loadingProgress.setVisibility(View.GONE);
                                Toast.makeText(getContext(),getActivity().getString(R.string.weather_failed),Toast.LENGTH_LONG).show();
                                reloadUIWithWeather(viewModel, null);
                                viewModel.getWeatherObservable().removeObserver(this);
                            }
                        }
                    });
                } else {
                    reloadUIWithWeather(viewModel, weather);
                }
            }
        });
    }

    private void reloadUIWithWeather(WeatherViewModel viewModel, Weather weather) {

        if (adapter.forecasts != null) {
            adapter.forecasts.clear();
        }

        if (weather == null) {
            binding.toggleBtn.setVisibility(View.INVISIBLE);
        } else {
            viewModel.setWeather(weather);

            adapter.forecasts = weather.getForecasts();

            binding.toggleBtn.setVisibility(View.VISIBLE);

            checkFavorites();

        }

        adapter.notifyDataSetChanged();

    }



    private void checkFavorites() {
        if (binding.cityname.getText().toString().length() > 0) {

            String searchCountry = binding.cityname.getText().toString().toLowerCase();
            Log.d(TAG, "checkFavorites country: " + searchCountry);
            Log.d(TAG, "checkFavorites in " + favorites.toString());

            if (favorites.contains(searchCountry)) {
                Log.d(TAG, "found  "+searchCountry);
                binding.toggleBtn.setChecked(true);
            } else {
                binding.toggleBtn.setChecked(false);
            }
        }
    }

    private void setupToggleBtn(){
        Fragment lifecycleOwner = SearchFragment.this;

        //getFavoriteStrings
        viewModel.getFavoriteStrings().observe(lifecycleOwner, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "getFavoriteStrings "+strings.toString());
                favorites = strings;
                checkFavorites();
            }
        });

        binding.toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton btn = (ToggleButton) v;
                Log.i(TAG, btn.isChecked() ? "selected" : "not selected");
                Weather currentWeather = binding.getViewmodel().weather.get();
                Log.d(TAG, "saving favorites " + currentWeather.getCityname());
                currentWeather.setFavorite(btn.isChecked());
                viewModel.updateWeatherFavorite(currentWeather);

            }
        });
    }
}
