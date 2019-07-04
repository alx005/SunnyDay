package com.google.sunnyday.service.model;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Weather {

    public Weather() {
    }

    @SerializedName("list")
    private ArrayList<Forecasts> forecasts;

    public ArrayList<Forecasts> getForecasts() {
        return forecasts;
    }

    public void setForecasts(ArrayList<Forecasts> forecasts) {
        this.forecasts = forecasts;
    }

    public class Forecasts {
        @SerializedName("weather")
        private ArrayList<WeatherObject> weatherList;

        public void setWeatherList(ArrayList<WeatherObject> weatherList) {
            this.weatherList = weatherList;
        }

        public ArrayList<WeatherObject> getWeatherList() {
            return weatherList;
        }

        public class WeatherObject {
            @SerializedName("description")
            private String weather_description;

            public String weather_description (){
                return weather_description;
            }

            @SerializedName("main")
            private String weather_title;
            public String weather_title (){
                return weather_title;
            }

        }
    }

//    @Override
//    public String toString()
//    {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (int i = 0; i < forecasts.size(); i++){
//            stringBuilder.append()
//        }
//        return "WeatherClass [count = "+forecasts.size()+"]";
//    }

}


