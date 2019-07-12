package com.google.sunnyday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.sunnyday.databinding.ActivityMainBinding;
import com.google.sunnyday.service.model.Settings;
import com.google.sunnyday.utils.FragmentUtils;
import com.google.sunnyday.utils.Utils;

import java.util.Set;

import static com.google.sunnyday.utils.FragmentUtils.TRANSITION_NONE;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Utils.saveStringToPref(R.string.search, null, this);

        NavController navController = Navigation.findNavController(this, R.id.mainNavFragment);
        NavigationView navView = binding.navigationView;
        NavigationUI.setupWithNavController(navView, navController);

        Settings settings = Utils.getSettingsPreference(this);
        if (settings == null) {
            settings = new Settings();
            settings.setFahrenheit(false);
            settings.setLightTheme(true);

            settings.setCelsius(true);
            settings.setDarkTheme(false);
            Utils.saveSettingsPreference(this, settings);
        }

        if (settings.getLightTheme() ? true : false) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppThemeDark);
        }

    }
}
