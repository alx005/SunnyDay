package com.google.sunnyday.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.WorkManager;

import java.util.List;

import com.google.sunnyday.service.model.City;
import com.google.sunnyday.service.repository.LocalRepository;


public class CityViewModel extends AndroidViewModel {
    private static String TAG = CityViewModel.class.getSimpleName();
    private WorkManager mWorkManager;

//    // New instance variable for the WorkInfo
//    private LiveData<List<WorkInfo>> mSavedWorkInfo;
//
//    // Add a getter method for mSavedWorkInfo
//    public LiveData<List<WorkInfo>> getOutputWorkInfo() { return mSavedWorkInfo; }
//
//    public CityViewModel() {
//        Log.d(TAG, "CityViewModel initialized");
//        mWorkManager = WorkManager.getInstance();
//        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT);
//    }
//
//
//    public void getJSONString() {
//        Log.d(TAG, "getJSONString");
//        // Add WorkRequest to save the image to the filesystem
//        OneTimeWorkRequest getJSON = new OneTimeWorkRequest.Builder(JSONWorker.class)
//                .addTag(Constants.TAG_OUTPUT)
//                .build();
//        mWorkManager.enqueue(getJSON);
//    }

    private LocalRepository repository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<City>> allCity;

    public CityViewModel(Application application) {
        super(application);
        repository = new LocalRepository(application);
        allCity = repository.getAllCity();
    }

    public LiveData<List<City>> getAllCity() {
        return allCity;
    }

    public void insert(City city) {
        repository.insert(city);
    }
}
