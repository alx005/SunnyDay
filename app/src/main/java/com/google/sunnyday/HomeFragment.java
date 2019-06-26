package com.google.sunnyday;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.service.repository.RetrofitClientInstance;
import com.google.sunnyday.service.repository.WeatherService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

//Get current location
public class HomeFragment extends Fragment {

    private static final int ACCESS_FINE_LOCATION_R_CODE = 1001;
    private LocationManager mLocationManager;
    private long LOCATION_REFRESH_TIME = 60;
    private float LOCATION_REFRESH_DISTANCE = 60;
    private final String TAG = "Alex_HOME";

    private static final String KEY_PROJECT_ID = "project_id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.d(TAG,"requesting permission");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_R_CODE);
        } else  {
            Log.d(TAG,"permission granted");
            getCurrentLocation();
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    //PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_R_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getCurrentLocation();
                } else {
                    Toast.makeText(getContext(), R.string.location_permission_failed_text, Toast.LENGTH_SHORT);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    //LOCATION
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.d(TAG,"getCurrentLocation");
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.d(TAG,"onLocationChanged");
            WeatherService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
            String appid = "e324535fa70cc7197fbc91fa6dcb573c";
            String units = "metric";
            Call<Weather> call = service.getCurrentWeather(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), appid, units);
            call.enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {
                    Weather weather = response.body();
                    ArrayList<Weather.WeatherObject> weatherObject = weather.getWeatherList();
                    Weather.WeatherObject currentWeather = weatherObject.get(0);
                    Log.d(TAG, currentWeather.weather_description());
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {

                }
            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
