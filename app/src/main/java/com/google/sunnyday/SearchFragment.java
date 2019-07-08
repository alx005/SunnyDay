package com.google.sunnyday;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.sunnyday.databinding.FragmentSearchBinding;
import com.google.sunnyday.service.model.City;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.utils.Utils;
import com.google.sunnyday.view.adapter.RecyclerViewWeatherAdapter;
import com.google.sunnyday.viewmodel.CityViewModel;
import com.google.sunnyday.viewmodel.WeatherViewModel;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding binding;
    private androidx.appcompat.widget.SearchView searchView;
    private CityViewModel cViewModel;
    private ArrayList<City> cities = new ArrayList<>();
    private ListView listView;
    private RecyclerViewWeatherAdapter adapter;
    private RecyclerView recyclerView;
    private String savedSearch = new String();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View view = binding.getRoot();

        recyclerView = binding.searchRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        adapter = new RecyclerViewWeatherAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        outState.putString("SearchText", searchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            savedSearch = savedInstanceState.getString("SearchText");
            Log.i(TAG, "onSaveInstanceState "+savedSearch);
        }
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            savedSearch = savedInstanceState.getString("SearchText");
            Log.i(TAG, "onSaveInstanceState "+savedSearch);
        }



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

        Log.i(TAG, "onCreateOptionsMenu");


        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: "+query);
                //get weather
                if (query != null && query.length() > 0) {
                    getWeatherWithCityName(query);
                }



                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, "onQueryTextChange: "+ query);

                //add suggestions
                return false;
            }
        });

        if (savedSearch != null) {
            Log.d(TAG, "savedSearch: "+ savedSearch);
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
//        Log.d(TAG, "showWorkFinished");
//        JsonParser parser = new JsonParser();
//        JsonElement mJson =  parser.parse(JSONString);
//        Gson gson = new Gson();
//
//        Type collectionType = new TypeToken<Collection<City>>(){}.getType();
//        cities = gson.fromJson(mJson, collectionType);
    }

    private void getWeatherWithCityName(String cityname) {

        final WeatherViewModel viewModel = ViewModelProviders.of(SearchFragment.this).get(WeatherViewModel.class);
        viewModel.setViewModelParams(cityname, null,null, Utils.getDateToday());
        viewModel.getWeatherObservable(true).observe(SearchFragment.this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather == null){
                    Log.d(TAG, "failed to get from DB, getting from service");
                    viewModel.getWeatherObservable(false).observe(SearchFragment.this, new Observer<Weather>() {
                        @Override
                        public void onChanged(Weather weather) {
                            if (weather != null) {
                                reloadUIWithWeather(viewModel, weather);
                            } else {
                                Log.e(TAG, "null weather");
                                Toast.makeText(getContext(),getActivity().getString(R.string.weather_failed),Toast.LENGTH_LONG).show();
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
        viewModel.setWeather(weather);

        adapter.forecasts = weather.getForecasts();
        adapter.notifyDataSetChanged();

        for (int i = 0; i < weather.getForecasts().size(); i++) {
            Weather.Forecasts forecasts = weather.getForecasts().get(i);
        }
    }
}
