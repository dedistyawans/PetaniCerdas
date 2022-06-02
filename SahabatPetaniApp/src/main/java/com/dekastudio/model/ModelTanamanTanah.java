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

public class ModelTanamanTanah extends ModelDatabase<ModelTanamanTanah>{
    //informasi nama kolom
    public static final String TABLE_NAME = "tanaman_tanah";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TANAMAN = "id_tanaman";
    public static final String COLUMN_ID_TANAH = "id_tanah";

    private int id;
    private int idTanaman;
    private int idTanah;

    //konstruktor dengan parameter id
    public ModelTanamanTanah(int id) {
        this.id = id;
    }

    //default constructor
    public ModelTanamanTanah() {
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel tanaman_tanah");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_TANAMAN+"`INTEGER NOT NULL," +
                "`"+COLUMN_ID_TANAH+"`INTEGER NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAH+"`) REFERENCES `"+ModelTanah.TABLE_NAME+"`(`"+ModelTanah.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAMAN+"`) REFERENCES `"+ModelTanaman.TABLE_NAME+"`(`"+ModelTanaman.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_ID_TANAMAN, getIdTanaman());
        cv.put(COLUMN_ID_TANAH, getIdTanah());
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<ModelTanamanTanah> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelTanamanTanah> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelTanamanTanah mtt = new ModelTanamanTanah();
                mtt.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mtt.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                mtt.setIdTanah(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAH)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mtt);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelTanamanTanah bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                setIdTanah(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAH)));
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

    public int getIdTanaman() {
        return idTanaman;
    }

    public void setIdTanaman(int idTanaman) {
        this.idTanaman = idTanaman;
    }

    public int getIdTanah() {
        return idTanah;
    }

    public void setIdTanah(int idTanah) {
        this.idTanah = idTanah;
    }
}
