package com.google.sunnyday.service.repository;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.sunnyday.service.model.Weather;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeatherForecastsConverter {

    @TypeConverter
    public ArrayList<Weather.Forecasts> storedStringToMyObjects(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Weather.Forecasts>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(List<Weather.Forecasts> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


//    public CountryLangs storedStringToLanguages(String value) {
//        List<String> langs = Arrays.asList(value.split("\\s*,\\s*"));
//        return new CountryLangs(langs);
//    }
//
//    @TypeConverter
//    public String languagesToStoredString(CountryLangs cl) {
//        String value = "";
//
//        for (String lang :cl.getCountryLangs())
//            value += lang + ",";
//
//        return value;
//    }
}
