package com.google.sunnyday.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.sunnyday.R;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.service.repository.WeatherRepository;
import com.google.sunnyday.utils.Utils;

import okhttp3.internal.Util;


public class WeatherViewModel extends AndroidViewModel {
    private LiveData<Weather> weatherObservable = new LiveData<Weather>() {
    };


    private final MutableLiveData<String> lat,lon;
    private final MutableLiveData<String> cityname;
    private static String TAG = WeatherViewModel.class.getSimpleName();

    public ObservableField<Weather> weather = new ObservableField<Weather>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        this.lat = new MutableLiveData<>();
        this.lon = new MutableLiveData<>();
        this.cityname = new MutableLiveData<>();

    }

    public LiveData<Weather> getWeatherObservable() {
        WeatherRepository weatherRepository = WeatherRepository.getInstance();
        weatherObservable = weatherRepository.getWeatherForecast(cityname.getValue(), lat.getValue(), lon.getValue());
        return weatherObservable;
    }

    public void setWeather(Weather weather) {
        this.weather.set(weather);
    }


    public void setViewModelParams(String cityname, String lat, String lon){
        this.cityname.setValue(cityname);
        this.lat.setValue(lat);
        this.lon.setValue(lon);
    }

    public void lat(String lat) {
        this.lat.setValue(lat);
    }
    public void lon(String lon) {
        this.lon.setValue(lon);
    }

    @BindingAdapter({"load_image"})
    public static void setImageViewResource(ImageView view, String resource) {
        String imageName = "";

        if (resource == null) {
            Log.d(TAG, "resource is null");
        } else {
            imageName = Utils.getImageForWeather(resource);

            if (view == null) {
                Log.d(TAG, "view is null");
            }
            if (resource == null) {
                Log.d(TAG, "imageUrl is null");
            }

            Log.d(TAG, "loading image "+imageName);

            Glide.with(view.getContext())
                    .asGif()
                    .load(view.getContext().getResources().getIdentifier(imageName, "drawable", view.getContext().getPackageName()))
                    .into(view);
        }




    }
}
