package com.google.sunnyday.service.model;

import com.google.sunnyday.R;
import com.google.sunnyday.utils.Utils;

public class Settings {
    public boolean isCelsius;
    public boolean isFahrenheit;
    public boolean isDarkTheme;
    public boolean isLightTheme;

    public boolean getCelsius() {
        return isCelsius;
    }

    public void setCelsius(boolean celsius) {
        isCelsius = celsius;
    }

    public boolean getDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public boolean getFahrenheit() {
        return isFahrenheit;
    }

    public void setFahrenheit(boolean fahrenheit) {
        isFahrenheit = fahrenheit;
    }

    public void setLightTheme(boolean lightTheme) {
        isLightTheme = lightTheme;
    }

    public boolean getLightTheme() {
        return isLightTheme;
    }

    public int getColor (boolean isChecked){
        return R.attr.colorControlHighlight;
//        int highlightColor = Utils.colorOfAttribute(getContext(), R.attr.colorControlHighlight);
//        int highlightedTextColor = Utils.colorOfAttribute(getContext(), R.attr.colorPrimary);
//
//        int normalTextColor = Utils.colorOfAttribute(getContext(), R.attr.colorOnPrimary);
//        int normalColor = Utils.colorOfAttribute(getContext(), R.attr.colorPrimary);
    }
}
