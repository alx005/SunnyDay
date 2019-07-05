package com.google.sunnyday.service.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.utils.Constants;
import com.google.sunnyday.utils.Utils;

import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static String TAG = WeatherRepository.class.getSimpleName();
    private static String appid = "e324535fa70cc7197fbc91fa6dcb573c";
    private static String units = "metric";
    private static String limitResults = "33";

    //DB
    private WeatherDao weatherDao;
    private static WeatherDatabase weatherDb;

    //constructor
    public WeatherRepository(Application application){
        weatherDb = WeatherDatabase.getDatabase(application);
        weatherDao = weatherDb.weatherDao();
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

    public LiveData<Weather> getWeatherForecastFromDB(String cityname, String lat, String lon, String fetchedDate) {
        return callWeatherFromDB(cityname, lat, lon, fetchedDate);
    }

    public LiveData<Weather> getWeatherForecast(String cityname, String lat, String lon, String fetchedDate) {
        return callWeatherService(cityname, lat, lon);
    }

    public LiveData<List<Weather>> getAllWeather(String cityname, String lat, String lon, String fetchedDate) {

        LiveData<List<Weather>> fetchedWeather;
        //retrieve from DB
        fetchedWeather = weatherDao.getAllWeather(fetchedDate);
        return fetchedWeather;
    }

    private LiveData<Weather> callWeatherFromDB(String cityname, String lat, String lon, String fetchedDate) {
        Log.d(TAG, "Fetching from DAO "+cityname +" "+ lat +","+lon+ " " + fetchedDate);
        if (cityname == null) {
            return weatherDao.getWeatherWithCoordinates(lat, lon, fetchedDate);
        } else {
            return weatherDao.getWeatherWithCityName(cityname, fetchedDate);
        }
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
                if (response != null) {
                    Log.d(TAG, "Weather results :\n"+ response.body());

                    //modify weather
                    Weather weather = response.body();
                    //today
                    String currentDate = Utils.getDateToday();
                    String currentForecastDate = "";

                    ArrayList<Weather.Forecasts> newForecast = new ArrayList<>();

                    for (int i = 0; i < weather.getForecasts().size(); i++) {
                        Weather.Forecasts forecasts = weather.getForecasts().get(i);
                        Date rawDate = new Date(Long.valueOf(forecasts.getTimestamp()) * 1000);

                        String dateFormatted = Utils.getDateFromFormat(Constants.DATE_FORMAT_M_D, rawDate);
                        if (currentForecastDate.equals(dateFormatted)) {
                            //add somewhere
                            continue;
                        } else {
                            currentForecastDate = dateFormatted;
                            newForecast.add(forecasts);
                        }

                        Log.d(TAG,"Weathers : "+forecasts.getWeatherList().get(0).weather_description());
                    }

                    weather.setForecasts(newForecast);
                    weather.setDatefetched(currentDate);

                    weather.setLat(lat);
                    weather.setLon(lon);
                    if (cityname != null) {
                        weather.setCityname(cityname.toLowerCase());
                    }
                    //save to db
                    insert(weather);

                    data.setValue(weather);

                }

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    //DB
    public void insert(Weather weather) {
        new insertAsyncTask(weatherDao).execute(weather);
    }

    private static class insertAsyncTask extends AsyncTask<Weather, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        insertAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Weather... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
