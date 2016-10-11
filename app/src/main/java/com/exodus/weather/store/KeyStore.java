package com.exodus.weather.store;


import android.app.Application;
import android.content.SharedPreferences;


public class KeyStore {
    SharedPreferences sharedPreferences;

    public KeyStore(Application application) {
        sharedPreferences = application.getSharedPreferences("KEY_STORE", 0);
    }

    public void setCityListParsed(boolean isParsed) {
        sharedPreferences.edit().putBoolean("is_city_list_parsed", isParsed).apply();
    }

    public boolean isCityListParsed() {
        return sharedPreferences.getBoolean("is_city_list_parsed", false);
    }
}
