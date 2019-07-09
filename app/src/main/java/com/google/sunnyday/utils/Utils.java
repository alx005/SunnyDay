package com.google.sunnyday.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.WorkerThread;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.sunnyday.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


public final class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private Utils()
    {
    }

    public static int getResourceId(Context context, String resourceName, String resourceType) {
        return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
    }

    @WorkerThread
    public static String loadJSONFromAsset(Context context) {
        Log.d(TAG, "loadJSONFromAsset");
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.citylist);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d(TAG, "loadJSONFromAsset err "+ ex.getLocalizedMessage());
            return null;
        }
        return json;

    }

    public static String getImageForWeather(String weather) {

        String resourceName = weather;

        switch (weather) {
            case "clear sky":
                resourceName = "clear";
                break;
            case "few clouds": case "scattered clouds":
                resourceName = "clouds";
                break;
            case "mist":
                break;
            case "shower rain": case "broken clouds": case "rain": case "light rain":
                resourceName = "rain";
                break;
            case "snow":
                break;
            case "thunderstorm":
                break;
            default:
                resourceName = "clear";

        }

        return resourceName;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDateFromFormat(String dateFormat, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    public static String getDateToday() {
        return Utils.getDateFromFormat(Constants.DATE_FORMAT_M_D, java.sql.Date.valueOf(LocalDate.now().toString()));
    }

    public static String camelCase(String stringToConvert) {
        if (TextUtils.isEmpty(stringToConvert))
        {return "";}
        return Character.toUpperCase(stringToConvert.charAt(0)) +
                stringToConvert.substring(1).toLowerCase();
    }

    public static int colorOfAttribute(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attributeId, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static void setTintColor(View view, int color) {
        Drawable drawable = view.getBackground();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
    }

    public static String getSavedStringWithKey(int resourceId, String defValue, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(activity.getString(resourceId), defValue);
    }

    public static int getSavedIntWithKey(String key, int defValue, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defValue);
    }

    public static void saveIntToPref(int resourceId, int value, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(resourceId), value);
        editor.commit();
    }

    public static void saveStringToPref(int resourceId, String value, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(resourceId), value);
        editor.commit();
    }

    public static int getThemeId(Context context) {
        try {
            Class<?> wrapper = Context.class;
            Method method = wrapper.getMethod("getThemeResId");
            method.setAccessible(true);
            return (Integer) method.invoke(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
