package com.example.vallsa7.jpmcweather.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vallsa7.jpmcweather.Utils.Constants;

/**
 * Stores the city preference using shared preferences and shows the information of that city next time user opens the app.
 */

public class CityPrefs {

    private static SharedPreferences cityPreference;

    public CityPrefs(Context context) {
        cityPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPrefs() {
        return cityPreference;
    }

    public String getCity() {
        return cityPreference.getString(Constants.CITY , null);
    }

    public void setCity(String city) {
        SharedPreferences.Editor prefsEditor = cityPreference.edit();
        prefsEditor.putString(Constants.CITY , city);
        prefsEditor.apply();
    }

}
