package com.google.sunnyday.service.model;

public class WeatherWrapper {

    public enum STATUS {
        SUCCESS,
        FAILED
    }

    public Weather weather;
    public Throwable err;
    public STATUS status;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setErr(Throwable error) {
        this.err = error;
    }

    public Throwable getErr() {
        return err;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
