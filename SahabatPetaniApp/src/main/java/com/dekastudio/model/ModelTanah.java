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

public class ModelTanah extends ModelDatabase<ModelTanah>{
    //informasi nama kolom
    public static final String TABLE_NAME = "tanah";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_BOOKMARK = "bookmark";

    private int id;
    private String nama;
    private String deskripsi;
    private int bookmark = -1;

    //konstruktor dengan parameter id
    public ModelTanah(int id) {
        this.id = id;
    }

    //default constructor
    public ModelTanah() {
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel tanah");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_NAMA+"`TEXT NOT NULL," +
                "`"+COLUMN_DESKRIPSI+"`TEXT NOT NULL," +
                "`"+COLUMN_BOOKMARK+"`INTEGER NOT NULL" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_NAMA, getNama());
        cv.put(COLUMN_DESKRIPSI, getDeskripsi());
        if(getBookmark()!=-1) {
            cv.put(COLUMN_BOOKMARK, getBookmark());
        }
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

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }


    public static List<ModelTanah> bacaDataBookmark(SQLiteDatabase dbWritable){
        //query utk select TanamanTanah berdasar idTanaman
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_BOOKMARK + "=" + String.valueOf(1);
        //deklarasi array list
        List<ModelTanah> semuaData= new ArrayList<>();
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelTanah mt = new ModelTanah();
                mt.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mt.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mt.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                mt.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mt);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    public static List<ModelTanah> bacaTanahByIdTanaman(SQLiteDatabase dbWritable, int idTanaman){
        //query utk select TanamanTanah berdasar idTanaman
        String selectQuery = "SELECT  t.id as id, t.nama as nama, t.deskripsi as deskripsi FROM " + ModelTanamanTanah.TABLE_NAME + " tt "
                + "JOIN " + TABLE_NAME + " t ON tt.id_tanah=t.id"
                + " WHERE tt." + ModelTanamanTanah.COLUMN_ID_TANAMAN +"=" + String.valueOf(idTanaman);
        //deklarasi array list
        List<ModelTanah> semuaData= new ArrayList<>();
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelTanah mt = new ModelTanah();
                mt.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mt.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mt.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mt);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public List<ModelTanah> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelTanah> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelTanah mt = new ModelTanah();
                mt.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mt.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mt.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                mt.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mt);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelTanah bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
