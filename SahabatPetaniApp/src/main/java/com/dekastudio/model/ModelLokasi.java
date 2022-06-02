package com.dekastudio.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.util.ArrayList;
import java.util.List;

public class ModelLokasi extends ModelDatabase<ModelLokasi> {

    public static final String TABLE_NAME = "lokasi";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA_DAERAH = "daerah";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ALTITUDE = "altitude";

    private int id;
    private String namaDaerah;
    private double latitude;
    private double longitude;
    private double altitude;

    //default construktor
    public ModelLokasi() {
    }

    public ModelLokasi(int id) {
        this.id = id;
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel lokasi");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_NAMA_DAERAH+"`TEXT NOT NULL," +
                "`"+COLUMN_LATITUDE+"`REAL NOT NULL," +
                "`"+COLUMN_LONGITUDE+"`REAL NOT NULL," +
                "`"+COLUMN_ALTITUDE+"`REAL NOT NULL" +
                ");";
        db.execSQL(query);
    }


    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMA_DAERAH, getNamaDaerah());
        cv.put(COLUMN_LATITUDE, getLatitude());
        cv.put(COLUMN_LONGITUDE, getLongitude());
        cv.put(COLUMN_ALTITUDE, getAltitude());
        return cv;
    }

    public static int bacaIdByLokasi(SQLiteDatabase dbWritable, ModelLokasi lokasi){
        int kembalian = -1;
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME +" WHERE "
                + COLUMN_LATITUDE + "=" + String.valueOf(lokasi.getLatitude())
                + " AND "
                + COLUMN_LONGITUDE + "=" + String.valueOf(lokasi.getLongitude());
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                kembalian = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();
        //return semua data
        return kembalian;
    }

    public static boolean apakahMasihKosong(SQLiteDatabase dbWritable){
        List<ModelLokasi> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelLokasi ml = new ModelLokasi();
                ml.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ml.setNamaDaerah(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_DAERAH)));
                ml.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
                ml.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
                ml.setAltitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTITUDE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(ml);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();
        //return semua data
        return semuaData.size()==0;
    }

    @Override
    public String getNamaTabel() {
        return TABLE_NAME;
    }

    @Override
    public String getKolomPrimary() {
        return COLUMN_ID;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<ModelLokasi> bacaSemuaData(SQLiteDatabase dbWritable) {
        List<ModelLokasi> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelLokasi ml = new ModelLokasi();
                ml.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ml.setNamaDaerah(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_DAERAH)));
                ml.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
                ml.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
                ml.setAltitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTITUDE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(ml);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();
        //return semua data
        return  semuaData;
    }

    @Override
    public ModelLokasi bacaDataDenganId(SQLiteDatabase dbWritable) {
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE "  + COLUMN_ID + "=" + String.valueOf(getId());
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                setNamaDaerah(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_DAERAH)));
                setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
                setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
                setAltitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTITUDE)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model
        return this;
    }

    public static ModelLokasi bacaDataByKoordinat(SQLiteDatabase dbWritable, double latitude, double longitude){
        ModelLokasi ml = null;
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE "  + COLUMN_LATITUDE +"=" + String.valueOf(latitude) + " AND " + COLUMN_LONGITUDE + "=" + String.valueOf(longitude);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                ml = new ModelLokasi();
                ml.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ml.setNamaDaerah(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_DAERAH)));
                ml.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
                ml.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
                ml.setAltitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTITUDE)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model, jika return null artinya data tidak ada di database
        return ml;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaDaerah() {
        return namaDaerah;
    }

    public void setNamaDaerah(String namaDaerah) {
        this.namaDaerah = namaDaerah;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

}
