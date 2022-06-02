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

public class ModelPenyakit extends ModelDatabase<ModelPenyakit>{

    public static final String TABLE_NAME = "penyakit";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TANAMAN = "id_tanaman";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_CIRI_CIRI = "ciri_ciri";
    public static final String COLUMN_CARA_MENANGANI = "cara_menangani";
    public static final String COLUMN_BOOKMARK = "bookmark";

    private int id;
    private int idTanaman;
    private String nama;
    private String deskripsi;
    private String ciriCiri;
    private String caraMenangani;
    private int bookmark = -1;

    //konstruktor dengan parameter id
    public ModelPenyakit(int id) {
        this.id = id;
    }

    public ModelPenyakit() {
    }

    //query untuk create table
    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel penyakit");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_TANAMAN+"`INTEGER NOT NULL," +
                "`"+COLUMN_NAMA+"`TEXT NOT NULL," +
                "`"+COLUMN_DESKRIPSI+"`TEXT NOT NULL," +
                "`"+COLUMN_CIRI_CIRI+"`TEXT NOT NULL," +
                "`"+COLUMN_CARA_MENANGANI+"`TEXT NOT NULL," +
                "`"+COLUMN_BOOKMARK+"`INTEGER NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_TANAMAN+"`) REFERENCES `"+ModelTanaman.TABLE_NAME+"`(`"+ModelTanaman.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_ID_TANAMAN, getIdTanaman());
        cv.put(COLUMN_NAMA, getNama());
        cv.put(COLUMN_DESKRIPSI, getDeskripsi());
        cv.put(COLUMN_CIRI_CIRI, getCiriCiri());
        cv.put(COLUMN_CARA_MENANGANI, getCaraMenangani());
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

    public static List<ModelPenyakit> bacaDataBookmark(SQLiteDatabase dbWritable){
        //deklarasi array list
        List<ModelPenyakit> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_BOOKMARK + "=" + String.valueOf(1);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel
                ModelPenyakit mp = new ModelPenyakit();
                mp.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mp.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                mp.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mp.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                mp.setCiriCiri(cursor.getString(cursor.getColumnIndex(COLUMN_CIRI_CIRI)));
                mp.setCaraMenangani(cursor.getString(cursor.getColumnIndex(COLUMN_CARA_MENANGANI)));
                mp.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mp);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    public static List<ModelPenyakit> bacaPenyakitByIdTanaman(SQLiteDatabase dbWritable, int idTanaman){
        //deklarasi array list
        List<ModelPenyakit> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_TANAMAN + "=" + String.valueOf(idTanaman);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel
                ModelPenyakit mp = new ModelPenyakit();
                mp.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mp.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                mp.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mp.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                mp.setCiriCiri(cursor.getString(cursor.getColumnIndex(COLUMN_CIRI_CIRI)));
                mp.setCaraMenangani(cursor.getString(cursor.getColumnIndex(COLUMN_CARA_MENANGANI)));
                mp.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mp);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public List<ModelPenyakit> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelPenyakit> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel
                ModelPenyakit mp = new ModelPenyakit();
                mp.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mp.setIdTanaman(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_TANAMAN)));
                mp.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mp.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                mp.setCiriCiri(cursor.getString(cursor.getColumnIndex(COLUMN_CIRI_CIRI)));
                mp.setCaraMenangani(cursor.getString(cursor.getColumnIndex(COLUMN_CARA_MENANGANI)));
                mp.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mp);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelPenyakit bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                setCiriCiri(cursor.getString(cursor.getColumnIndex(COLUMN_CIRI_CIRI)));
                setCaraMenangani(cursor.getString(cursor.getColumnIndex(COLUMN_CARA_MENANGANI)));
                setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model tanaman
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

    public String getCiriCiri() {
        return ciriCiri;
    }

    public void setCiriCiri(String ciriCiri) {
        this.ciriCiri = ciriCiri;
    }

    public String getCaraMenangani() {
        return caraMenangani;
    }

    public void setCaraMenangani(String caraMenangani) {
        this.caraMenangani = caraMenangani;
    }
}
