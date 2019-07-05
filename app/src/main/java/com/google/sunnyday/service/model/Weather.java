package com.google.sunnyday.service.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
@Entity(tableName = "weather_table")
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
        @SerializedName("dt")
        private String timestamp;
        public String getTimestamp() {
            return timestamp;
        }

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


    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "datefetched")
    private String datefetched;

    @ColumnInfo(name = "cityname")
    private String cityname;

    @ColumnInfo(name = "lat")
    private String lat;

    @ColumnInfo(name = "lon")
    private String lon;


    public String getDatefetched() {
        return datefetched;
    }

    public void setDatefetched(String date) {
        this.datefetched = date;
    }

    public void setCityname(String cityname) {
        if (cityname != null)
        this.cityname = cityname;
    }

    public String getCityname() {
        return cityname;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setLat(String lat) {
        if (lat != null)
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLon(String lon) {
        if (lon != null)
        this.lon = lon;
    }

    public String getLon() {
        return lon;
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


