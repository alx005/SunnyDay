package com.google.sunnyday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.sunnyday.databinding.FragmentSettingsBinding;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();

        getFavorites();

        binding.celsius.setChecked(Utils.getSavedIntWithKey(getActivity().getString(R.string.temperature), 1, getActivity()) == 0 ? false : true);
        binding.lightTheme.setChecked(Utils.getSavedIntWithKey(getActivity().getString(R.string.theme), 1, getActivity()) == 0 ? false : true);

        //TODO: remove when binding adapter is working
        if (binding.celsius.isChecked()) {
            switchSelectedBtn(binding.celsius, binding.fahrenheit);
        } else  {
            switchSelectedBtn(binding.fahrenheit, binding.celsius);
        }

        if (binding.lightTheme.isChecked()) {
            switchSelectedBtn(binding.lightTheme, binding.darkTheme);
        } else  {
            switchSelectedBtn(binding.darkTheme, binding.lightTheme);
        }


        binding.temperatureToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Log.d(TAG, "onButtonChecked: "+ checkedId + " isChecked: "+ Boolean.toString(isChecked));

                MaterialButton selectedBtn = getActivity().findViewById(checkedId);

                if (isChecked) {
                    if (selectedBtn == binding.celsius) {
                        switchSelectedBtn(selectedBtn, binding.fahrenheit);
                        Utils.saveIntToPref(R.string.temperature, 1, getActivity());
                    } else {
                        switchSelectedBtn(selectedBtn, binding.celsius);
                        Utils.saveIntToPref(R.string.temperature, 0, getActivity());
                    }
                }
            }
        });

        binding.theme.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                MaterialButton selectedBtn = getActivity().findViewById(checkedId);

                if (isChecked) {
                    if (selectedBtn == binding.lightTheme) {
                        switchSelectedBtn(selectedBtn, binding.darkTheme);
                        Utils.saveIntToPref(R.string.theme, 1, getActivity());
//                        getActivity().setTheme(R.style.AppTheme);
                        getActivity().recreate();
                    } else {
                        switchSelectedBtn(selectedBtn, binding.lightTheme);
                        Utils.saveIntToPref(R.string.theme, 0, getActivity());
//                        getActivity().setTheme(R.style.AppThemeDark);
                        getActivity().recreate();
                    }
                }
            }
        });

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
