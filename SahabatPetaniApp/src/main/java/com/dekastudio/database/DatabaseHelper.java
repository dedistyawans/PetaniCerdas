package com.dekastudio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.dekastudio.helper.Constant;
import com.dekastudio.model.ModelCuaca;
import com.dekastudio.model.ModelDatabase;
import com.dekastudio.model.ModelFotoPenyakit;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelHistory;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.model.ModelTanamanTanah;

import java.util.List;

/**
 * Created by DekaStudio on 10/17/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TAG = Constant.TAG;

    public DatabaseHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "process create database");
        //enable foreignkey di sqlite
        enableForeignKey(db);
        //create table tanaman
        ModelTanaman.createTable(db);
        //create table foto tanaman
        ModelFotoTanaman.createTable(db);
        //create table penyakit
        ModelPenyakit.createTable(db);
        //create table foto penyakit
        ModelFotoPenyakit.createTable(db);
        //create table tanah
        ModelTanah.createTable(db);
        //create table foto tanah
        ModelFotoTanah.createTable(db);
        //create table tanaman_tanah
        ModelTanamanTanah.createTable(db);
        //create table history konsultasi
        ModelHistory.createTable(db);
        //create table lokasi
        ModelLokasi.createTable(db);
        //create table cuaca
        ModelCuaca.createTable(db);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelDatabase> T bacaDataDenganId(T model){
        Log.d(TAG, "baca data dengan id -> " + model.getNamaTabel() + " : " + String.valueOf(model.getId()));
        return (T) model.bacaDataDenganId(getWritableDatabase());
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelDatabase> List<T> bacaSemuaData(T model){
        Log.d(TAG, "baca semua data -> " + model.getNamaTabel());
        return model.bacaSemuaData(getWritableDatabase());
    }

    public <T extends ModelDatabase> int insertData(T model){
        Log.d(TAG, "insert data -> " + model.getNamaTabel() + " : " + String.valueOf(model.getId()));
        return model.insertData(getWritableDatabase());
    }

    public <T extends ModelDatabase> void updateData(T model, int id){
        Log.d(TAG, "update data -> " + model.getNamaTabel() + " : " + String.valueOf(model.getId()));
        model.updateData(getWritableDatabase(), id);
    }

    public <T extends ModelDatabase> void deleteData(T model){
        Log.d(TAG, "delete data -> " + model.getNamaTabel() + " : " + String.valueOf(model.getId()));
        model.deleteData(getWritableDatabase());
    }

    public <T extends ModelDatabase> void deleteSemuaData(T model){
        Log.d(TAG, "delete semua data -> " + model.getNamaTabel());
        model.deleteSemuaData(getWritableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        enableForeignKey(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        enableForeignKey(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        enableForeignKey(db);
    }

    public SQLiteDatabase getOpenDatabase(){
        return getWritableDatabase();
    }

    //fungsi untuk enable foreignkey, karena secara default fitur foreign key OFF
    private void enableForeignKey(SQLiteDatabase db){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            String query = String.format ("PRAGMA foreign_keys = %s","ON");
            db.execSQL(query);
        }
    }

}
