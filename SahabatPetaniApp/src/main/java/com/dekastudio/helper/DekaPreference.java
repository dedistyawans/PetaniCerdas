package com.dekastudio.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DekaPreference {
    private SharedPreferences preferences;

    public DekaPreference(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String latKey = "latKey";
    public void simpanLat(float lat){
        preferences.edit().putFloat(latKey, lat).apply();
    }

    public float getLat(){
        return preferences.getFloat(latKey, 0.0f);
    }

    private String lngKey = "lngKey";
    public void simpanLng(float lng){
        preferences.edit().putFloat(lngKey, lng).apply();
    }

    public float getLng(){
        return preferences.getFloat(lngKey, 0.0f);
    }

    private String daerahKey = "daerahKey";
    public void setDaerah(String daerah){
        preferences.edit().putString(daerahKey, daerah).apply();
    }

    public String getDaerah(){
        return preferences.getString(daerahKey, "");
    }

    private String tinggiKey = "tinggiKey";
    public void setTinggiTanah(float meter){
        preferences.edit().putFloat(tinggiKey, meter).apply();
    }

    public float getTinggiTanah(){
        return preferences.getFloat(tinggiKey, 0.0f);
    }
}
