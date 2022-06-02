package com.dekastudio.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DekaStudio on 10/11/2017.
 */

public class ModelFotoPenyakit extends ModelDatabase<ModelFotoPenyakit>{

    //informasi nama kolom
    public static final String TABLE_NAME = "foto_penyakit";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_PENYAKIT = "id_penyakit";
    public static final String COLUMN_NAMA_FILE = "nama_file";

    private int id;
    private int idPenyakit;
    private String namaFile;

    //konstruktor dengan parameter id
    public ModelFotoPenyakit(int id) {
        this.id = id;
    }

    //default constructor
    public ModelFotoPenyakit() {
    }

    //query untuk create table
    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel foto_penyakit");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_PENYAKIT+"`INTEGER NOT NULL," +
                "`"+COLUMN_NAMA_FILE+"`TEXT NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_PENYAKIT+"`) REFERENCES `"+ModelPenyakit.TABLE_NAME+"`(`"+ModelPenyakit.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<ModelFotoPenyakit> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelFotoPenyakit> semuaData = new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelFotoPenyakit mfp = new ModelFotoPenyakit();
                mfp.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mfp.setIdPenyakit(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PENYAKIT)));
                mfp.setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mfp);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    public static List<ModelFotoPenyakit> bacaFotoPenyakit(SQLiteDatabase dbWritable, int idPenyakit){
        //deklarasi array list
        List<ModelFotoPenyakit> semuaData = new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + COLUMN_ID_PENYAKIT + "=" +
                String.valueOf(idPenyakit);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelFotoPenyakit mfp = new ModelFotoPenyakit();
                mfp.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mfp.setIdPenyakit(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PENYAKIT)));
                mfp.setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mfp);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelFotoPenyakit bacaDataDenganId(SQLiteDatabase dbWritable) {
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + COLUMN_ID + "=" +
                String.valueOf(getId());
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                setIdPenyakit(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PENYAKIT)));
                setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPenyakit() {
        return idPenyakit;
    }

    public void setIdPenyakit(int idPenyakit) {
        this.idPenyakit = idPenyakit;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_ID_PENYAKIT, getIdPenyakit());
        cv.put(COLUMN_NAMA_FILE, getNamaFile());
        return cv;
    }

    @Override
    public String getNamaTabel() {
        return TABLE_NAME;
    }

    @Override
    public String getKolomPrimary() {
        return COLUMN_ID;
    }

}
