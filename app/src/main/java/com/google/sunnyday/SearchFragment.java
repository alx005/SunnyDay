package com.google.sunnyday;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.sunnyday.databinding.FragmentSearchBinding;
import com.google.sunnyday.service.model.City;

import java.io.IOException;
import java.io.InputStream;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding binding;
    private androidx.appcompat.widget.SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        Log.i(TAG, "onCreateOptionsMenu");

        //Load JSON
        String mJsonString = loadJSONFromAsset(getContext());

        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(mJsonString);
        Gson gson = new Gson();
        City cities[] = gson.fromJson(mJson, City[].class);

        Log.i(TAG, "City");
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        Log.i(TAG, "getSystemService");
        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        Log.i(TAG, "findItem");
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(TAG, query);
                return false;
            }
        });

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.citylist);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
