package com.google.sunnyday.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.service.repository.WeatherRepository;
import java.util.List;


public class WeatherViewModel extends AndroidViewModel {
    private LiveData<Weather> weatherObservable = new LiveData<Weather>() {
    };
    private LiveData<List<String>> favoriteObservable = new LiveData<List<String>>() {};

    private LiveData<List<Weather>> weatherObservableAll = new LiveData<List<Weather>>() {
    };


    private final MutableLiveData<String> lat,lon;
    private final MutableLiveData<String> cityname, fetchDate;
    private static String TAG = WeatherViewModel.class.getSimpleName();

    public ObservableField<Weather> weather = new ObservableField<Weather>();

    private WeatherRepository weatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(application);
        this.lat = new MutableLiveData<>();
        this.lon = new MutableLiveData<>();
        this.cityname = new MutableLiveData<>();
        this.fetchDate = new MutableLiveData<>();
    }

    public LiveData<Weather> getWeatherObservable() {
        return weatherObservable;
    }

    public LiveData<Weather> getWeatherObservable(boolean fromDB) {
        if (fromDB) {
            weatherObservable = weatherRepository.getWeatherForecastFromDB(cityname.getValue(), lat.getValue(), lon.getValue(), fetchDate.getValue());
        } else {
            weatherObservable = weatherRepository.getWeatherForecast(cityname.getValue(), lat.getValue(), lon.getValue(), fetchDate.getValue());
        }

        return weatherObservable;
    }

    public LiveData<List<Weather>> getWeatherObservableAll() {
        weatherObservableAll = weatherRepository.getAllWeather(cityname.getValue(), lat.getValue(), lon.getValue(), fetchDate.getValue());
        return weatherObservableAll;
    }

    public LiveData<List<String>> getFavoriteStrings() {
        favoriteObservable = weatherRepository.getFavoriteStrings();
        return favoriteObservable;
    }

    public LiveData<List<Weather>> getAllFavorites() {
        weatherObservableAll = weatherRepository.getAllFavorites();
        return weatherObservableAll;
    }

    public void setWeather(Weather weather) {
        this.weather.set(weather);
    }

    public void updateWeatherFavorite(Weather weather) {
        weatherRepository.update(weather);
    }


    public void setViewModelParams(String cityname, String lat, String lon, String fetchDate){
        this.cityname.setValue(cityname);
        this.lat.setValue(lat);
        this.lon.setValue(lon);
        this.fetchDate.setValue(fetchDate);
    }

    public void lat(String lat) {
        this.lat.setValue(lat);
    }
    public void lon(String lon) {
        this.lon.setValue(lon);
    }


}
