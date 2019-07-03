package com.google.sunnyday;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.sunnyday.databinding.WeatherLayoutBinding;
import com.google.sunnyday.service.model.Weather;

import java.util.ArrayList;

public class RecyclerViewWeatherAdapter extends RecyclerView.Adapter<RecyclerViewWeatherHolder> {

    private static String TAG = RecyclerViewWeatherAdapter.class.getSimpleName();
    public ArrayList<Weather.Forecasts> forecasts;

    public RecyclerViewWeatherAdapter(ArrayList<Weather.Forecasts> forecasts) {
        this.forecasts = forecasts;
    }

    @NonNull
    @Override
    public RecyclerViewWeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View statusContainer = inflater.inflate(R.layout.weather_layout, parent, false);
        return new RecyclerViewWeatherHolder(statusContainer);
//
//        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder");
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder1");
//        WeatherLayoutBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
//        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder2");
//
//
//
//        return new RecyclerViewWeatherHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWeatherHolder holder, int position) {
        Weather.Forecasts obj = forecasts.get(position);
        holder.bind(obj);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "item count : "+forecasts.size());
        return forecasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public WeatherLayoutBinding weatherLayoutBinding;

        public ViewHolder(WeatherLayoutBinding weatherLayoutBinding) {
            super(weatherLayoutBinding.getRoot());
            this.weatherLayoutBinding = weatherLayoutBinding;
        }
    }
}
