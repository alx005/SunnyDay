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

    //api.openweathermap.org/data/2.5/weather?lat=35&lon=139
    @GET("weather")
    Call<Weather> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid);

//    api.openweathermap.org/data/2.5/
}
