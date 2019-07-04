package com.google.sunnyday.service.repository;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.google.sunnyday.service.model.City;

import java.util.List;

public class LocalRepository {
    private CityDAO cityDAO;
    private LiveData<List<City>> mAllCity;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public LocalRepository(Application application) {
        CityDatabase db = CityDatabase.getDatabase(application);
        cityDAO = db.cityDAO();
        mAllCity = cityDAO.getCityList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<City>> getAllCity() {
        return mAllCity;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(City city) {
        new insertAsyncTask(cityDAO).execute(city);
    }

    private static class insertAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDAO mAsyncTaskDao;

        insertAsyncTask(CityDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final City... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
