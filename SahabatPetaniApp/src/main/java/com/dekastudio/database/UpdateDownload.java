package com.dekastudio.database;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dekastudio.asyncronus.DataUpdate;
import com.dekastudio.core.Preference;
import com.dekastudio.helper.Constant;

import org.json.JSONObject;

/**
 * Created by DekaStudio on 11/7/2017.
 */

public class UpdateDownload {
    private DatabaseDownloadListener ddl;
    private DatabaseHelper dbhelper;
    private RequestQueue requestQueue;
    private int versiClient;
    private Preference preference;

    public UpdateDownload(DatabaseDownloadListener ddl, Preference preference, DatabaseHelper dbhelper, RequestQueue requestQueue, int versiClient) {
        this.ddl = ddl;
        this.dbhelper = dbhelper;
        this.requestQueue = requestQueue;
        this.versiClient = versiClient;
        this.preference = preference;
    }

    public void eksekusi(){
        JsonObjectRequest jar = new JsonObjectRequest(Constant.API_VERSION_URL + String.valueOf(versiClient), null, this::reqDataUpdate, error -> {
            //terjadi kegagalan saat mendownload database pembaruan
            ddl.onGagalDownload();
        });
        //tambahkan ke requestqueue volley
        requestQueue.add(jar);
    }


    private void reqDataUpdate(JSONObject response){
        new DataUpdate(requestQueue, dbhelper, versiClient, new DataUpdate.DataUpdateListener() {
            @Override
            public void onSelesaiUpdate(int versi) {
                //set versi client ke versi baru yg berhasil di instal
                preference.setVersiClient(versi);
                ddl.onSelesaiDownload(versi);
            }

            @Override
            public void onGagalUpdate() {
                //terjadi kegagalan dalam menginstal pembaruan
                ddl.onGagalDownload();
            }
        }
        ).execute(response);
    }

}
