package com.example.vallsa7.jpmcweather.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Forecast data that represents all the weather info for 5 days from today tbhrough a list.
 */

public class ForecastData {

    @SerializedName("list")
    public ArrayList<FList> list = new ArrayList<FList>();

    public class FList {

        @SerializedName("dt")
        public long dt;

        @SerializedName("main")
        public Main main;

        @SerializedName("weather")
        public ArrayList<Weather> weather;

    }

    public class Main {

        @SerializedName("temp_min")
        public float temp_min;

        @SerializedName("temp_max")
        public float temp_max;

    }

    public class Weather {

        @SerializedName("icon")
        public String icon;
    }

}
