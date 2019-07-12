package com.google.sunnyday.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.sunnyday.R;
import com.google.sunnyday.service.model.Settings;
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
        Settings settings = Utils.getSettingsPreference(activity);
        //celsius
        if (settings.getCelsius() ? true : false) {
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

    @BindingAdapter({"img_selected_bg"})
    public static void setImageSelectedBackground(MaterialButton view, boolean state) {
        Context context = view.getContext();

        int highlightColor = Utils.colorOfAttribute(context, R.attr.colorControlHighlight);
        int highlightedTextColor = Utils.colorOfAttribute(context, R.attr.colorPrimary);

        int normalTextColor = Utils.colorOfAttribute(context, R.attr.colorOnPrimary);
        int normalColor = Utils.colorOfAttribute(context, R.attr.colorPrimary);

        if (state) {
            view.setTextColor(highlightedTextColor);
            Utils.setTintColor(view, highlightColor);
        } else {
            view.setTextColor(normalTextColor);
            Utils.setTintColor(view, normalColor);
        }

    }
}
