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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.sunnyday.databinding.FragmentHomeBinding;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.utils.Utils;
import com.google.sunnyday.view.adapter.RecyclerViewWeatherAdapter;
import com.google.sunnyday.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

//Get current location
public class HomeFragment extends Fragment {

    private static final int ACCESS_FINE_LOCATION_R_CODE = 1001;
    private LocationManager mLocationManager;
    private long LOCATION_REFRESH_TIME = 60;
    private float LOCATION_REFRESH_DISTANCE = 60;
    private final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    private RecyclerViewWeatherAdapter adapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        recyclerView = binding.weatherRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        adapter = new RecyclerViewWeatherAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        verifyPermissions();
    }

    //PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"permission granted "+ requestCode + " " + ACCESS_FINE_LOCATION_R_CODE + " " + grantResults[0]);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_R_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();
                } else {
                    Toast.makeText(getContext(), R.string.location_permission_failed_text, Toast.LENGTH_SHORT);
                }
                return;
            }
        }
    }

    private void getWeatherWithLocation(Location location) {
        String lat = Double.toString(location.getLatitude()), lon = Double.toString(location.getLongitude());

        final WeatherViewModel viewModel = ViewModelProviders.of(HomeFragment.this).get(WeatherViewModel.class);
        binding.setWeatherViewModel(viewModel);
        viewModel.setViewModelParams(null, lat, lon, Utils.getDateToday());
        viewModel.getWeatherObservable(true).observe(HomeFragment.this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather == null){
                    Log.d(TAG, "failed to get from DB, getting from service");
                    viewModel.getWeatherObservable().removeObserver(this);
                    viewModel.getWeatherObservable(false).observe(HomeFragment.this, new Observer<Weather>() {
                        @Override
                        public void onChanged(Weather weather) {
                            if (weather != null) {
                                reloadUIWithWeather(viewModel, weather);
                                viewModel.getWeatherObservable().removeObserver(this);
                            } else {
                                Log.e(TAG, "null weather");
                                Toast.makeText(getContext(),getActivity().getString(R.string.weather_failed),Toast.LENGTH_LONG).show();
                                binding.loadingProgress.setVisibility(View.GONE);
                                viewModel.getWeatherObservable().removeObserver(this);
                            }
                        }
                    });
                } else {
                    reloadUIWithWeather(viewModel, weather);
                }
            }
        });

    }

    private void reloadUIWithWeather(WeatherViewModel viewModel, Weather weather) {
        viewModel.setWeather(weather);

        adapter.forecasts = weather.getForecasts();
        adapter.notifyDataSetChanged();
        binding.loadingProgress.setVisibility(View.GONE);

    }

    //LOCATION
    private void verifyPermissions (){
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"requesting permission");

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_R_CODE);

        } else  {
            Log.d(TAG,"permission granted");
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.d(TAG,"getCurrentLocation");
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG,"onLocationChanged");
            getWeatherWithLocation(location);

//            Call<Weather> call = service.getCurrentWeather(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), appid, units);
//            call.enqueue(new Callback<Weather>() {
//                @Override
//                public void onResponse(Call<Weather> call, Response<Weather> response) {
//                    Weather weather = response.body();
//                    ArrayList<Weather.WeatherObject> weatherObject = weather.getWeatherList();
//                    Weather.WeatherObject currentWeather = weatherObject.get(0);
//                    Log.d(TAG, currentWeather.weather_description());
//                }
//
//                @Override
//                public void onFailure(Call<Weather> call, Throwable t) {
//
//                }
//            });
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
