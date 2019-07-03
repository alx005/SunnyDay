package com.google.sunnyday;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import java.io.IOException;
import java.io.InputStream;


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
}
