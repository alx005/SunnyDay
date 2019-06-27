package com.google.sunnyday.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.service.repository.WeatherRepository;


public class WeatherViewModel extends AndroidViewModel {
    private LiveData<Weather> weatherObservable = new LiveData<Weather>() {
    };

    private final MutableLiveData<String> lat,lon,appid,units;
    private static String TAG = WeatherViewModel.class.getSimpleName();


    public WeatherViewModel(@NonNull Application application) {
        super(application);

        this.lat = new MutableLiveData<>();
        this.lon = new MutableLiveData<>();
        this.appid = new MutableLiveData<>();
        this.units = new MutableLiveData<>();

    }

    public LiveData<Weather> getWeatherObservable() {
        WeatherRepository weatherRepository = WeatherRepository.getInstance();
        weatherObservable = weatherRepository.getCurrentWeather(lat.getValue(), lon.getValue(), appid.getValue(), units.getValue());
        return weatherObservable;
    }

    public void setViewModelParams(String lat, String lon, String appid, String units){
        this.lat.setValue(lat);
        this.lon.setValue(lon);
        this.appid.setValue(appid);
        this.units.setValue(units);
    }

    public void lat(String lat) {
        this.lat.setValue(lat);
    }

    public void lon(String lon) {
        this.lon.setValue(lon);
    }

    public void appid(String appid) {
        this.appid.setValue(appid);
    }

    public void units(String units) {
        this.units.setValue(units);
    }
}
