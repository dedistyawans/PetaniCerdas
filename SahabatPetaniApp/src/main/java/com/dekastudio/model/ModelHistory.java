package com.dekastudio.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.util.ArrayList;
import java.util.List;

public class ModelHistory extends ModelDatabase<ModelHistory> {
    //informasi nama kolom
    public static final String TABLE_NAME = "history";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TANAMAN = "id_tanaman";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_JAM = "jam";

    private int id;
    private int idTanaman;
    private String tanggal;
    private String jam;

    //default construktor
    public ModelHistory() {
    }

    public ModelHistory(int id) {
        this.id = id;
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel history");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_TANAMAN+"`INTEGER NOT NULL," +
                "`"+COLUMN_TANGGAL+"`TEXT NOT NULL," +
                "`"+COLUMN_JAM+"`TEXT NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAMAN+"`) REFERENCES `"+ModelTanaman.TABLE_NAME+"`(`"+ModelTanaman.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_ID_TANAMAN, getIdTanaman());
        cv.put(COLUMN_TANGGAL, getTanggal());
        cv.put(COLUMN_JAM, getJam());
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
    public List<ModelHistory> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelHistory> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelHistory mh = new ModelHistory();
                mh.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mh.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                mh.setTanggal(cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL)));
                mh.setJam(cursor.getString(cursor.getColumnIndex(COLUMN_JAM)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mh);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelHistory bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setTanggal(cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL)));
                setJam(cursor.getString(cursor.getColumnIndex(COLUMN_JAM)));
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

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}
