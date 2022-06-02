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

public class ModelFotoTanah extends ModelDatabase<ModelFotoTanah>{
    //informasi nama kolom
    public static final String TABLE_NAME = "foto_tanah";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TANAH = "id_tanah";
    public static final String COLUMN_NAMA_FILE = "nama_file";

    private int id;
    private int idTanah;
    private String namaFile;

    //konstruktor dengan parameter id
    public ModelFotoTanah(int id) {
        this.id = id;
    }

    //default constructor
    public ModelFotoTanah() {
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel foto_tanah");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_TANAH+"`INTEGER NOT NULL," +
                "`"+COLUMN_NAMA_FILE+"`TEXT NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAH+"`) REFERENCES `"+ModelTanah.TABLE_NAME+"`(`"+ModelTanah.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<ModelFotoTanah> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelFotoTanah> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelFotoTanah mft = new ModelFotoTanah();
                mft.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mft.setIdTanah(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAH)));
                mft.setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mft);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    public static List<ModelFotoTanah> bacaFotoTanah(SQLiteDatabase dbWritable, int idTanah){
        //deklarasi array list
        List<ModelFotoTanah> semuaData = new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + COLUMN_ID_TANAH + "=" +
                String.valueOf(idTanah);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelFotoTanah mft = new ModelFotoTanah();
                mft.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mft.setIdTanah(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAH)));
                mft.setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mft);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelFotoTanah bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setIdTanah(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAH)));
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

    public int getIdTanah() {
        return idTanah;
    }

    public void setIdTanah(int idTanah) {
        this.idTanah = idTanah;
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
        cv.put(COLUMN_ID_TANAH, getIdTanah());
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
