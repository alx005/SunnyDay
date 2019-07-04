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
    private static String appid = "e324535fa70cc7197fbc91fa6dcb573c";
    private static String units = "metric";
    private static String limitResults = "5";
    //private constructor.
    private WeatherRepository(){}

    public static WeatherRepository getInstance() {
        return sSoleInstance;
    }

    public LiveData<Weather> getCurrentWeather(String lat, String lon) {
        final MutableLiveData<Weather> data = new MutableLiveData<>();

        WeatherService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);

        Log.d(TAG, "getting weather for "+ lat + " " + lon);
        Call<Weather> call = service.getCurrentWeather(lat, lon, appid, units);
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

    public LiveData<Weather> getWeatherForecast(String cityname, String lat, String lon) {
        return callWeatherService(cityname, lat, lon);
    }

    private MutableLiveData<Weather> callWeatherService(String cityname, String lat, String lon) {
        final MutableLiveData<Weather> data = new MutableLiveData<>();
        WeatherService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<Weather> call;

        if (cityname != null) {
            Log.d(TAG, "getting weather for "+ cityname);
            call = service.getWeatherForecastFromCity(cityname, appid, units, limitResults);
        } else {
            call = service.getWeatherForecastFromCoordinate(lat, lon, appid, units, limitResults);
        }

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d(TAG, "Weather results :\n"+ response.body());
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
