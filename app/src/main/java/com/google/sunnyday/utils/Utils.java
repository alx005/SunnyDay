package com.google.sunnyday.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.sunnyday.R;

import java.io.IOException;
import java.io.InputStream;
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
}
