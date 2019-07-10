package com.google.sunnyday.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.sunnyday.service.model.Weather;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WeatherDao {
    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from weather_table WHERE lat =:lat AND lon =:lon AND datefetched =:fetchedDate")
    LiveData<Weather> getWeatherWithCoordinates(String lat, String lon, String fetchedDate);

    @Query("SELECT * from weather_table WHERE cityname =lower(:cityname) AND datefetched =:fetchedDate")
    LiveData<Weather> getWeatherWithCityName(String cityname, String fetchedDate);

    @Query("SELECT * from weather_table WHERE datefetched = :fetchedDate")
    LiveData<List<Weather>> getAllWeather(String fetchedDate);

    @Query("SELECT DISTINCT cityname from weather_table WHERE favorite == 1")
    LiveData<List<String>> getFavoriteStrings();

    @Query("SELECT DISTINCT * from weather_table WHERE favorite == 1")
    LiveData<List<Weather>> getAllFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Weather weather);

    @Update
    void update(Weather weather);

    @Query("DELETE FROM weather_table")
    void deleteAll();
}
