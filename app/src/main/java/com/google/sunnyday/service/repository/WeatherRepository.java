package com.google.sunnyday.service.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.sunnyday.service.model.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static String TAG = WeatherRepository.class.getSimpleName();
    private static volatile WeatherRepository sSoleInstance = new WeatherRepository();

    //private constructor.
    private WeatherRepository(){}

    public static WeatherRepository getInstance() {
        return sSoleInstance;
    }

    public LiveData<Weather> getCurrentWeather(String lat, String lon, String appid, String metric) {
        final MutableLiveData<Weather> data = new MutableLiveData<>();

        WeatherService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);

        Log.d(TAG, "getting weather for "+ lat + " " + lon);
        Call<Weather> call = service.getCurrentWeather(lat, lon, appid, metric);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
