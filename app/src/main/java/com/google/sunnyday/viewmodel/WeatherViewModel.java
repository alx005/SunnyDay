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


public class WeatherViewModel extends AndroidViewModel {
    private LiveData<Weather> weatherObservable = new LiveData<Weather>() {
    };

    private final MutableLiveData<String> lat,lon,appid,units;
    private static String TAG = WeatherViewModel.class.getSimpleName();

    public ObservableField<Weather> weather = new ObservableField<Weather>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        this.lat = new MutableLiveData<>();
        this.lon = new MutableLiveData<>();
        this.appid = new MutableLiveData<>();
        this.units = new MutableLiveData<>();

    }

    public LiveData<Weather> getWeatherObservable() {
        WeatherRepository weatherRepository = WeatherRepository.getInstance();
        weatherObservable = weatherRepository.getCurrentWeather(lat.getValue(), lon.getValue(), appid.getValue(), units.getValue());
        return weatherObservable;
    }
    public void setWeather(Weather weather) {
        this.weather.set(weather);
    }


    public void setViewModelParams(String lat, String lon, String appid, String units){
        this.lat.setValue(lat);
        this.lon.setValue(lon);
        this.appid.setValue(appid);
        this.units.setValue(units);
    }

    public void lat(String lat) {
        this.lat.setValue(lat);
    }

    public void lon(String lon) {
        this.lon.setValue(lon);
    }

    public void appid(String appid) {
        this.appid.setValue(appid);
    }

    public void units(String units) {
        this.units.setValue(units);
    }

    @BindingAdapter({"load_image"})
    public static void setImageViewResource(ImageView view, String resource) {
        String imageName = resource;

        if (resource == null) {

        } else {
            switch (imageName) {
                case "clear sky":
                    imageName = "clear";
                    break;
                case "few clouds": case "scattered clouds":
                    imageName = "clouds";
                    break;
                case "mist":
                    break;
                case "shower rain": case "broken clouds": case "rain": case "light rain":
                    imageName = "rain";
                    break;
                case "snow":
                    break;
                case "thunderstorm":
                    break;
                default:
                    imageName = "clear";

            }

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
