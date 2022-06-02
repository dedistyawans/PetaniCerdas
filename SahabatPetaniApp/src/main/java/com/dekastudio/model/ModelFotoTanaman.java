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

public class ModelFotoTanaman extends ModelDatabase<ModelFotoTanaman>{
    public static final String TABLE_NAME = "foto_tanaman";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TANAMAN = "id_tanaman";
    public static final String COLUMN_NAMA_FILE = "nama_file";

    private int id;
    private int idTanaman;
    private String namaFile;

    //konstruktor dengan parameter id
    public ModelFotoTanaman(int id) {
        this.id = id;
    }

    //default constructor
    public ModelFotoTanaman() {

    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel foto_tanaman");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_TANAMAN+"`INTEGER NOT NULL," +
                "`"+COLUMN_NAMA_FILE+"`TEXT NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAMAN+"`) REFERENCES `"+ModelTanaman.TABLE_NAME+"`(`"+ModelTanaman.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_ID_TANAMAN, getIdTanaman());
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<ModelFotoTanaman> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelFotoTanaman> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel
                ModelFotoTanaman mft = new ModelFotoTanaman();
                mft.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mft.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
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
    public ModelFotoTanaman bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setNamaFile(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_FILE)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model
        return this;
    }

    public static List<ModelFotoTanaman> bacaFotoTanaman(SQLiteDatabase dbWritable, int idTanaman) {
        //deklarasi array list
        List<ModelFotoTanaman> semuaData = new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + COLUMN_ID_TANAMAN + "=" +
                String.valueOf(idTanaman);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel
                ModelFotoTanaman mft = new ModelFotoTanaman();
                mft.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mft.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
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

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTanaman() {
        return idTanaman;
    }

    public void setIdTanaman(int idTanaman) {
        this.idTanaman = idTanaman;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }
}
