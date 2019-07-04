package com.google.sunnyday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.sunnyday.utils.FragmentUtils;

import static com.google.sunnyday.utils.FragmentUtils.TRANSITION_NONE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.mainNavFragment);
        NavigationView navView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
