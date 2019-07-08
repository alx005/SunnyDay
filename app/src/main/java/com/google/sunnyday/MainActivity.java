package com.google.sunnyday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.sunnyday.utils.FragmentUtils;
import com.google.sunnyday.utils.Utils;

import static com.google.sunnyday.utils.FragmentUtils.TRANSITION_NONE;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavController navController = Navigation.findNavController(this, R.id.mainNavFragment);
        NavigationView navView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navView, navController);

        if (Utils.getSavedIntWithKey(getString(R.string.theme), 1, this) == 0 ? false : true) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppThemeDark);
        }

    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        // you could also use a switch if you have many themes that could apply
        return theme;
    }

}
