package com.google.sunnyday.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Weather {


//    {
//        "coord": {
//        "lon": -122.08,
//                "lat": 37.42
//    },
//        "weather": [
//        {
//            "id": 801,
//                "main": "Clouds",
//                "description": "few clouds",
//                "icon": "02n"
//        }
//  ],
//        "base": "stations",
//            "main": {
//        "temp": 288.94,
//                "pressure": 1013,
//                "humidity": 59,
//                "temp_min": 285.93,
//                "temp_max": 292.15
//    },
//        "visibility": 16093,
//            "wind": {
//        "speed": 3.6,
//                "deg": 360
//    },
//        "clouds": {
//        "all": 20
//    },
//        "dt": 1561529724,
//            "sys": {
//        "type": 1,
//                "id": 5845,
//                "message": 0.0114,
//                "country": "US",
//                "sunrise": 1561466924,
//                "sunset": 1561519980
//    },
//        "timezone": -25200,
//            "id": 5375480,
//            "name": "Mountain View",
//            "cod": 200
//    }

    @SerializedName("weather")
    private ArrayList<WeatherObject> weatherList;

    public Weather() {
    }

    public void setWeatherList(ArrayList<WeatherObject> weatherList) {
        this.weatherList = weatherList;
    }

    public ArrayList<WeatherObject> getWeatherList() {
        return weatherList;
    }

    public class WeatherObject {
        @SerializedName("description")
        private String weather_description;

        public String weather_description (){
            return weather_description;
        }

        @SerializedName("main")
        private String weather_title;
        public String weather_title (){
            return weather_title;
        }

    }

}


