package com.google.sunnyday.service.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.sunnyday.service.model.City;

import java.util.List;

@Dao
public interface CityDAO {
    @Query("SELECT * FROM city_table ORDER BY name ASC")
    public LiveData<List<City>> getCityList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(City city);

    @Query("DELETE FROM city_table")
    void deleteAll();
}
