package com.google.sunnyday;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.sunnyday.utils.Constants;
import com.google.sunnyday.utils.Utils;

public class JSONWorker extends Worker {
    private static final String TAG = JSONWorker.class.getSimpleName();

    public JSONWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();

        try {
            Log.e(TAG, "JSONWorker");

//            JsonParser parser = new JsonParser();
//            JsonElement mJson =  parser.parse(Utils.loadJSONFromAsset(applicationContext));
//            Gson gson = new Gson();
//
//            Type collectionType = new TypeToken<Collection<City>>(){}.getType();
//            ArrayList<City> cities = gson.fromJson(mJson, collectionType);


            // If there were no errors, return SUCCESS
            Data outputData = new Data.Builder()
                    .putString(Constants.JSON_WORKER_URI, Utils.loadJSONFromAsset(applicationContext))
                    .build();
            return Result.success(outputData);

        } catch (Throwable throwable) {

            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "error fetching JSON", throwable);
            return Result.failure();
        }

    }
}
