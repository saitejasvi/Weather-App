package com.example.vallsa7.jpmcweather.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vallsa7.jpmcweather.Utils.Constants;

/**
 * Stores the launch mode preferences. For the first time, it displays the location of (0,0) co-ordinates by default.
 */

public class Prefs {

    private static SharedPreferences launchPreference;
    private static SharedPreferences coordPreference;
    private static Coord coord;

    public Prefs(Context context) {
        launchPreference = PreferenceManager.getDefaultSharedPreferences(context);
        coordPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPrefs() {
        return launchPreference;
    }

    public Boolean isFirstTimeLaunch() {
        return launchPreference.getBoolean(Constants.LAUNCH , true);
    }

    public void setFirstTimeLaunch(Boolean firstTimeLaunch) {
        SharedPreferences.Editor prefsEditor = launchPreference.edit();
        prefsEditor.putBoolean(Constants.LAUNCH , firstTimeLaunch);
        prefsEditor.apply();
    }

    public  float getLatitude() {
        return coordPreference.getFloat(Constants.LATITUDE , 0);
    }

    public  float getLongitude() {
        return coordPreference.getFloat(Constants.LONGITUDE , 0);
    }

    public void setCoord(Coord coord) {
        SharedPreferences.Editor prefsEditor = coordPreference.edit();
        prefsEditor.putFloat(Constants.LATITUDE , coord.lat);
        prefsEditor.putFloat(Constants.LONGITUDE, coord.lon);
        prefsEditor.apply();    }

    public static class Coord {
        float lat;
        float lon;

        public Coord(float lat, float lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}
