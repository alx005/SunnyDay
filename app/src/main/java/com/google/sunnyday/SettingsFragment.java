package com.google.sunnyday;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.sunnyday.databinding.FragmentSettingsBinding;
import com.google.sunnyday.utils.Utils;

import okhttp3.internal.Util;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private static String TAG = SettingsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();

        binding.celsius.setChecked(Utils.getSavedIntWithKey(getActivity().getString(R.string.temperature), 1, getActivity()) == 0 ? false : true);
        binding.lightTheme.setChecked(Utils.getSavedIntWithKey(getActivity().getString(R.string.theme), 1, getActivity()) == 0 ? false : true);


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
                        getActivity().setTheme(R.style.AppTheme);
                    } else {
                        switchSelectedBtn(selectedBtn, binding.lightTheme);
                        Utils.saveIntToPref(R.string.theme, 0, getActivity());
                        getActivity().setTheme(R.style.AppThemeDark);
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

}
