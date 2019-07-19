package com.google.sunnyday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.sunnyday.databinding.FragmentSettingsBinding;
import com.google.sunnyday.service.model.Settings;
import com.google.sunnyday.service.model.Weather;
import com.google.sunnyday.utils.Utils;
import com.google.sunnyday.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private List<Weather> favorites = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FragmentSettingsBinding binding;
    private static String TAG = SettingsFragment.class.getSimpleName();
    private Settings settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();

        settings = Utils.getSettingsPreference(getActivity());
        binding.setSettings(settings);

        binding.temperatureToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Log.d(TAG, "onButtonChecked: "+ checkedId + " isChecked: "+ Boolean.toString(isChecked));

                MaterialButton selectedBtn = getActivity().findViewById(checkedId);

                if (isChecked) {
                    if (selectedBtn == binding.celsius) {
                        settings.setCelsius(true);
                        settings.setFahrenheit(false);
                    } else {
                        settings.setCelsius(false);
                        settings.setFahrenheit(true);
                    }

                    binding.invalidateAll();
                    Utils.saveSettingsPreference(getActivity(), settings);
                }
            }
        });

        binding.theme.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                MaterialButton selectedBtn = getActivity().findViewById(checkedId);
                if (isChecked) {
                    if (selectedBtn == binding.lightTheme && settings.getLightTheme() == false) {
                        settings.setLightTheme(true);
                        settings.setDarkTheme(false);
                        getActivity().recreate();
                    } else if (selectedBtn == binding.darkTheme && settings.getDarkTheme() == false) {
                        settings.setLightTheme(false);
                        settings.setDarkTheme(true);
                        getActivity().recreate();
                    }

                    Utils.saveSettingsPreference(getActivity(), settings);
                }
            }
        });

        getFavorites();

        return view;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void switchSelectedBtn (MaterialButton selectedBtn, MaterialButton oppositeBtn) {
        int highlightColor = Utils.colorOfAttribute(getContext(), R.attr.colorControlHighlight);
        int highlightedTextColor = Utils.colorOfAttribute(getContext(), R.attr.colorPrimary);

        int normalTextColor = Utils.colorOfAttribute(getContext(), R.attr.colorOnPrimary);
        int normalColor = Utils.colorOfAttribute(getContext(), R.attr.colorPrimary);

        selectedBtn.setTextColor(highlightedTextColor);
        Utils.setTintColor(selectedBtn, highlightColor);

        oppositeBtn.setChecked(false);
        oppositeBtn.setTextColor(normalTextColor);
        Utils.setTintColor(oppositeBtn, normalColor);
    }

    private void getFavorites() {

        Log.d(TAG, "getFavoriteStrings");
        ListView listView = binding.favList;
        Fragment lifecycleOwner = SettingsFragment.this;
        WeatherViewModel viewModel = getViewModel();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {
                    String weather = (String) parent.getAdapter().getItem(position);
                    SettingsFragmentDirections.FavoritesTappedAction action = SettingsFragmentDirections.favoritesTappedAction(weather);
                    NavHostFragment.findNavController(SettingsFragment.this).navigate(action);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.remove_favorite_dialog);
                builder.setItems(new String[]{"Yes", "No"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                        {
                            removeFavorite(position);

                        }
                    }
                });
                builder.show();
                return true;
            }
        });

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_checked);
        listView.setAdapter(adapter);


        viewModel.getAllFavorites().observe(lifecycleOwner, new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weathers) {
                favorites = weathers;
                adapter.clear();
                List<String> tempList = new ArrayList<>();
                for (Weather weather : weathers) {
                    tempList.add(Utils.camelCase(weather.getCityname()));
                }
                adapter.addAll(tempList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void removeFavorite (int index) {
        Weather weather = favorites.get(index);
        weather.setFavorite(false);
        getViewModel().updateWeatherFavorite(weather);
    }

    private WeatherViewModel getViewModel () {
        Fragment lifecycleOwner = SettingsFragment.this;
        final WeatherViewModel viewModel = ViewModelProviders.of(lifecycleOwner).get(WeatherViewModel.class);
        return viewModel;
    }

}
