package com.google.sunnyday.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.sunnyday.R;
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
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWeatherHolder holder, int position) {
        Weather.Forecasts obj = forecasts.get(position);
        holder.bind(obj);
    }

    @Override
    public int getItemCount() {
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
