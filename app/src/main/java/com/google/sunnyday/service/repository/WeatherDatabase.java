package com.google.sunnyday.service.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.sunnyday.service.model.Weather;

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile WeatherDatabase INSTANCE;

    static WeatherDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherDatabase.class, "weather_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
