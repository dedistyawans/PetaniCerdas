package com.dekastudio.core;

import android.content.Context;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dekastudio.helper.Constant;

import org.json.JSONException;

/**
 * Created by DekaStudio on 11/8/2017.
 */

public class Preference {

    private Context context;
    private RequestQueue requestQueue;

    public Preference(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
    }

    public void setVersiClient(int versiClient){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Constant.PREF_APP_VERSION, versiClient).apply();
    }


    public int getVersiClient(){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Constant.PREF_APP_VERSION, 0);
    }

    public void getVersiServer(ServerListener sl){
        JsonObjectRequest jor = new JsonObjectRequest(Constant.API_VERSION_URL, null, response -> {
            try {
                sl.onSuksesRequestVersiServer(response.getInt(Constant.API_VERSI_SERVER));
            } catch (JSONException e) {
                //terjadi kesalahan saat parsing json
                sl.onGagalRequestVersiServer();
                e.printStackTrace();
            }
        }, error -> {
            //terjadi kegagalan saat mendapatkan informasi versi dari server
            sl.onGagalRequestVersiServer();
        });
        requestQueue.add(jor);
    }

    public boolean apakahPerluUpdate(int versiServer){
        return getVersiClient() < versiServer;
    }

    public interface ServerListener{
        void onSuksesRequestVersiServer(int versiServer);
        void onGagalRequestVersiServer();
    }
}
