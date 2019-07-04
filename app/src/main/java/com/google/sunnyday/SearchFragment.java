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
import androidx.work.WorkInfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.sunnyday.databinding.FragmentSearchBinding;
import com.google.sunnyday.service.model.City;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.viewmodel.CityViewModel;
import com.google.sunnyday.viewmodel.WeatherViewModel;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding binding;
    private androidx.appcompat.widget.SearchView searchView;
    private CityViewModel cViewModel;
    private ArrayList<City> cities = new ArrayList<>();
    private ListView listView;
    private RecyclerViewWeatherAdapter adapter;
    private RecyclerView recyclerView;

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

        // Get the ViewModel
//        cViewModel = ViewModelProviders.of(SearchFragment.this).get(CityViewModel.class);
//
//        City city = new City("Alexis");
//
//        cViewModel.getAllCity().observe(this, new Observer<List<City>>() {
//            @Override
//            public void onChanged(List<City> cities) {
//
//                for (int i = 0; i < cities.size(); i++){
//                    City retrievedcity = cities.get(i);
//                    Log.d(TAG, "Alexis CITY: " + retrievedcity.get_id());
//                    Log.d(TAG, "Alexis CITY: " + retrievedcity.getName());
//                }
//            }
//        });
//
//        Log.d(TAG, "adding city"+ city.toString());
//        cViewModel.insert(city);
//
//
        //Load JSON
//        String mJsonString = loadJSONFromAsset(getContext());
//        createRecyclerView(cities);

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
        viewModel.setViewModelParams(cityname, null,null);
        viewModel.getWeatherObservable().observe(SearchFragment.this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather == null || adapter == null){
                    Log.e(TAG, "null weather");
                    weather = null;
                } else {
                    viewModel.setWeather(weather);

                    adapter.forecasts = weather.getForecasts();
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < weather.getForecasts().size(); i++) {
                        Weather.Forecasts forecasts = weather.getForecasts().get(i);
                        Log.d(TAG,"Weathers : "+forecasts.getWeatherList().get(0).weather_description());
                    }
                }

            }
        });
    }

}
