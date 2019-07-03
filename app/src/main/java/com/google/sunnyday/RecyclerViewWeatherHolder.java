package com.google.sunnyday;

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

public class RecyclerViewWeatherHolder extends RecyclerView.ViewHolder {

    public final WeatherLayoutBinding binding;
    private static String TAG = RecyclerViewWeatherHolder.class.getSimpleName();
    private ArrayList<Weather.Forecasts> forecastsArrayList;


    public RecyclerViewWeatherHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }


    public RecyclerViewWeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (parent == null || parent.getContext() == null) {
            Log.d(TAG,"null");
            return null;
        }
        Log.d(TAG,"parent"+parent.toString());
        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder1");
        WeatherLayoutBinding itemBinding = WeatherLayoutBinding.inflate(layoutInflater, parent, false);
        Log.d(TAG,"RecyclerViewWeatherHolder onCreateViewHolder2");
        return new RecyclerViewWeatherHolder(itemBinding);
    }

    public void onBindViewHolder(@NonNull RecyclerViewWeatherAdapter.ViewHolder holder, int position) {

        Weather.Forecasts forecasts = forecastsArrayList.get(position);
        holder.weatherLayoutBinding.setForecasts(forecasts);
//        holder.weatherLayoutBinding.setItemClickListener(this);
    }


    public RecyclerViewWeatherHolder(WeatherLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Weather.Forecasts item) {

        binding.setForecasts(item);
        binding.executePendingBindings();
    }
}
