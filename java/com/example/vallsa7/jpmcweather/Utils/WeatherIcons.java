package com.example.vallsa7.jpmcweather.Utils;

import com.example.vallsa7.jpmcweather.R;

/**
 * Instead of making network calls every time user asks for the forecast and weather data, the images which are most probably constant
 * are stored and mapped to the icon id.
 */

public class WeatherIcons {

    public int getWeatherIcon(String icon) {
        int id = 0;
            switch (icon) {
                case "01d" :
                    id = R.drawable.clear_sky;
                    break;
                case "02d" :
                    id = R.drawable.few_clouds_day;
                    break;
                case "03d" :
                    id = R.drawable.scattered_clouds;
                    break;
                case "04d" :
                    id = R.drawable.broken_clouds_1;
                    break;
                case "09d" :
                    id = R.drawable.shower_rain;
                    break;
                case "10d" :
                    id = R.drawable.rain;
                    break;
                case "11d" :
                    id = R.drawable.thunderstrom;
                    break;
                case "13d" :
                    id = R.drawable.snow;
                    break;
                case "50d" :
                    id = R.drawable.mist;
                    break;
                case "01n" :
                    id = R.drawable.clear_sky_night;
                    break;
                case "02n" :
                    id = R.drawable.few_clouds_night;
                    break;
                case "03n" :
                    id = R.drawable.scattered_clouds_night;
                    break;
                case "04n" :
                    id = R.drawable.broken_clouds_night;
                    break;
                case "09n" :
                    id = R.drawable.shower_rain_night;
                    break;
                case "10n" :
                    id = R.drawable.rain_night;
                    break;
                case "11n" :
                    id = R.drawable.thunderstrom_night;
                    break;
                case "13n" :
                    id = R.drawable.snow_night;
                    break;
                case "50n" :
                    id = R.drawable.mist_night;
                    break;

            }
        return id;
    }



}
