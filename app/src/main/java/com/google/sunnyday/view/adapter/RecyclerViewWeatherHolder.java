package com.google.sunnyday.view.adapter;

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


    public RecyclerViewWeatherHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }


    public void bind(Weather.Forecasts item) {

        binding.setForecasts(item);
        binding.executePendingBindings();
    }
}
