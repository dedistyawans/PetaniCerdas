package com.dekastudio.database;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.dekastudio.core.Preference;

/**
 * Created by DekaStudio on 10/17/2017.
 */

public class DatabaseManagement {

    private Context context;
    private RequestQueue requestQueue;

    public DatabaseManagement(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
    }

    public void updateDatabase(int versiClient, DatabaseDownloadListener ddl){
        //object updatedownload dan eksekusi update
        new UpdateDownload(ddl, new Preference(context, requestQueue), new DatabaseHelper(context), requestQueue, versiClient).eksekusi();
    }

    public void downloadDatabase(DatabaseDownloadListener ddl){
        //object FirsTimeDownload dan eksekusi method eksekusi(); untuk mendowonload database dan image pertama kalinya
        new FirstTimeDownload(new DatabaseHelper(context), requestQueue,  ddl).eksekusi();
    }


}
