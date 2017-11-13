package com.example.vallsa7.jpmcweather.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Weather data that represents all the weather info of the current day.
 */

public class WeatherData {

    @SerializedName("name")
    public String name;

    @SerializedName("dt")
    public long dt;

    @SerializedName("weather")
    public ArrayList<Weather> weather;

    @SerializedName("main")
    public Main main;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("wind")
    public Wind wind;


    public class Main {

        @SerializedName("temp")
        public float temp;

        @SerializedName("pressure")
        public float pressure;

        @SerializedName("ic_humidity")
        public int humidity;

        @SerializedName("temp_min")
        public float temp_min;

        @SerializedName("temp_max")
        public float temp_max;

    }

    public class Sys {

        @SerializedName("country")
        public String country;
    }

    public class Weather {

        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public class Wind {

        @SerializedName("speed")
        public float speed;
    }

}





