package com.google.sunnyday.service.repository;

import com.google.gson.JsonElement;
import com.google.sunnyday.service.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    String HTTPS_API_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Call<Weather> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid, @Query("units") String units);

    @GET("forecast")
    Call<Weather> getWeatherForecastFromCoordinate(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid, @Query("units") String units, @Query("cnt") String cnt);

    @GET("forecast")
    Call<Weather> getWeatherForecastFromCity(@Query("q") String cityname, @Query("appid") String appid, @Query("units") String units, @Query("cnt") String cnt);
}
