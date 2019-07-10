package com.google.sunnyday.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.sunnyday.R;
import com.google.sunnyday.utils.Constants;
import com.google.sunnyday.utils.Utils;

import java.util.Date;

public class CustomBindingAdapter {
    private static String TAG = CustomBindingAdapter.class.getSimpleName();
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

    @BindingAdapter({"set_text"})
    public static void setStringDate(TextView view, String resource) {
        Date updatedate = new Date(Long.valueOf(resource) * 1000);
        view.setText(Utils.getDateFromFormat(Constants.DATE_FORMAT_WITH_TIME, updatedate));
    }

    @BindingAdapter({"camel_case_string"})
    public static void setStringToCamelCase(TextView view, String resource) {
        view.setText(Utils.camelCase(resource));
    }
    @BindingAdapter({"set_temp"})
    public static void setTemperature(TextView view, String resource) {
        Activity activity = (Activity) view.getContext();

        //celsius
        if (Utils.getSavedIntWithKey(activity.getString(R.string.temperature), 1, activity) == 0 ? false : true) {
            view.setText(resource+" "+ activity.getString(R.string.celsius));
        } else {
            float fahrenheight = (Float.valueOf(resource) * 9/5) + 32;
            view.setText(String.valueOf(fahrenheight) +" "+ activity.getString(R.string.fahrenheit));

        }
    }
    @BindingAdapter({"custom_src"})
    public static void loadImageByTheme (ImageView view, String test) {
        Context context = view.getContext();
        switch (Utils.getThemeId(context)) {
            case R.style.AppTheme:
                view.setImageDrawable(context.getDrawable(R.drawable.temperature_black));
                break;
            case R.style.AppThemeDark:
                view.setImageDrawable(context.getDrawable(R.drawable.temperature_white));
                break;
        }
    }

    //TODO: not called
    @BindingAdapter({"set_toggle_image"})
    public static void setToggleImage(ToggleButton view, String resource) {


        Context context = view.getContext();

        switch (Utils.getThemeId(context)) {
            case R.style.AppTheme:
                view.setBackgroundDrawable(context.getDrawable(R.drawable.favorite_black));
                break;
            case R.style.AppThemeDark:
                view.setBackgroundDrawable(context.getDrawable(R.drawable.favorite_white));
                break;
        }
    }
}
