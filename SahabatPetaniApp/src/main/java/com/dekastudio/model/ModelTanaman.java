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

public class ModelTanaman extends ModelDatabase<ModelTanaman>{

    public static final String TABLE_NAME = "tanaman";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_UMUR = "umur";
    public static final String COLUMN_MUSIM = "musim";
    public static final String COLUMN_KETINGGIAN_MIN = "ketinggian_min";
    public static final String COLUMN_KETINGGIAN_MAX = "ketinggian_max";
    public static final String COLUMN_CURAH_HUJAN_MIN = "curah_hujan_min";
    public static final String COLUMN_CURAH_HUJAN_MAX = "curah_hujan_max";
    public static final String COLUMN_SUHU_MIN = "suhu_min";
    public static final String COLUMN_SUHU_MAX = "suhu_max";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_REKOMENDASI_MENANAM = "rekomendasi_menanam";
    public static final String COLUMN_BOOKMARK = "bookmark";

    private int id;
    private String nama;
    private int umur;
    private String musim;
    private int ketinggianMin;
    private int ketinggianMax;
    private int curahHujanMin;
    private int curahHujanMax;
    private int suhuMin;
    private int suhuMax;
    private String deskripsi;
    private String rekomendasiMenanam;
    private int bookmark = -1;

    //default konstruktor
    public ModelTanaman() {

    }

    //konstruktor dengan parameter id
    public ModelTanaman(int id) {
        this.id = id;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    @Override
    public int getId() {
        return id;
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

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public String getMusim() {
        return musim;
    }

    public void setMusim(String musim) {
        this.musim = musim;
    }

    public int getKetinggianMin() {
        return ketinggianMin;
    }

    public void setKetinggianMin(int ketinggianMin) {
        this.ketinggianMin = ketinggianMin;
    }

    public int getKetinggianMax() {
        return ketinggianMax;
    }

    public void setKetinggianMax(int ketinggianMax) {
        this.ketinggianMax = ketinggianMax;
    }


    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getRekomendasiMenanam() {
        return rekomendasiMenanam;
    }

    public void setRekomendasiMenanam(String rekomendasiMenanam) {
        this.rekomendasiMenanam = rekomendasiMenanam;
    }

    public int getCurahHujanMin() {
        return curahHujanMin;
    }

    public void setCurahHujanMin(int curahHujanMin) {
        this.curahHujanMin = curahHujanMin;
    }

    public int getCurahHujanMax() {
        return curahHujanMax;
    }

    public void setCurahHujanMax(int curahHujanMax) {
        this.curahHujanMax = curahHujanMax;
    }

    public int getSuhuMin() {
        return suhuMin;
    }

    public void setSuhuMin(int suhuMin) {
        this.suhuMin = suhuMin;
    }

    public int getSuhuMax() {
        return suhuMax;
    }

    public void setSuhuMax(int suhuMax) {
        this.suhuMax = suhuMax;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, getId());
        cv.put(COLUMN_NAMA, getNama());
        cv.put(COLUMN_UMUR, getUmur());
        cv.put(COLUMN_MUSIM, getMusim());
        cv.put(COLUMN_KETINGGIAN_MIN, getKetinggianMin());
        cv.put(COLUMN_KETINGGIAN_MAX, getKetinggianMax());
        cv.put(COLUMN_CURAH_HUJAN_MIN, getCurahHujanMin());
        cv.put(COLUMN_CURAH_HUJAN_MAX, getCurahHujanMax());
        cv.put(COLUMN_SUHU_MIN, getSuhuMin());
        cv.put(COLUMN_SUHU_MAX, getSuhuMax());
        cv.put(COLUMN_DESKRIPSI, getDeskripsi());
        cv.put(COLUMN_REKOMENDASI_MENANAM, getRekomendasiMenanam());
        if (getBookmark() != -1) {
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


    //query untuk create table
    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel tanaman");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                " `"+COLUMN_ID+"` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                " `"+COLUMN_NAMA+"` TEXT NOT NULL," +
                " `"+COLUMN_UMUR+"` INTEGER NOT NULL," +
                " `"+COLUMN_MUSIM+"` TEXT NOT NULL," +
                " `"+COLUMN_KETINGGIAN_MIN+"` INTEGER NOT NULL," +
                " `"+COLUMN_KETINGGIAN_MAX+"` INTEGER NOT NULL," +
                " `"+COLUMN_CURAH_HUJAN_MIN+"` INTEGER NOT NULL," +
                " `"+COLUMN_CURAH_HUJAN_MAX+"` INTEGER NOT NULL," +
                " `"+COLUMN_SUHU_MIN+"` INTEGER NOT NULL," +
                " `"+COLUMN_SUHU_MAX+"` INTEGER NOT NULL," +
                " `"+COLUMN_DESKRIPSI+"` TEXT NOT NULL," +
                " `"+COLUMN_REKOMENDASI_MENANAM+"` TEXT NOT NULL," +
                " `"+COLUMN_BOOKMARK+"` INTEGER NOT NULL" +
                ");";
        db.execSQL(query);
    }

    public static List<ModelTanaman> bacaDataBookmark(SQLiteDatabase dbWritable){
        //deklarasi array list Modeltanaman
        List<ModelTanaman> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_BOOKMARK +"=" + String.valueOf(1);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada / minimal 1 row
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel modelTanaman
                ModelTanaman modelTanaman = new ModelTanaman();
                modelTanaman.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                modelTanaman.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                modelTanaman.setUmur(cursor.getInt(cursor.getColumnIndex(COLUMN_UMUR)));
                modelTanaman.setMusim(cursor.getString(cursor.getColumnIndex(COLUMN_MUSIM)));
                modelTanaman.setKetinggianMin(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MIN)));
                modelTanaman.setKetinggianMax(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MAX)));
                modelTanaman.setCurahHujanMin(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MIN)));
                modelTanaman.setCurahHujanMax(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MAX)));
                modelTanaman.setSuhuMin(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MIN)));
                modelTanaman.setSuhuMax(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MAX)));
                modelTanaman.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                modelTanaman.setRekomendasiMenanam(cursor.getString(cursor.getColumnIndex(COLUMN_REKOMENDASI_MENANAM)));
                modelTanaman.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(modelTanaman);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data tanaman
        return  semuaData;
    }

    public static List<ModelTanaman> bacaDataByIdTanah(SQLiteDatabase dbWritable, int idTanah){
        //deklarasi array list Modeltanaman
        List<ModelTanaman> semuaData = new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  t.id, t.nama, t.musim, t.umur," +
                " t.ketinggian_min, t.ketinggian_max, t.curah_hujan_min," +
                " t.curah_hujan_max, t.suhu_min, t.suhu_max FROM " + ModelTanamanTanah.TABLE_NAME +" tt "
                + "JOIN " + TABLE_NAME + " t "
                + "ON tt.id_tanaman=t.id "
                + "WHERE tt.id_tanah=" + String.valueOf(idTanah);
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada / minimal 1 row
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel modelTanaman
                ModelTanaman mt = new ModelTanaman();
                mt.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mt.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                mt.setUmur(cursor.getInt(cursor.getColumnIndex(COLUMN_UMUR)));
                mt.setMusim(cursor.getString(cursor.getColumnIndex(COLUMN_MUSIM)));
                mt.setKetinggianMin(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MIN)));
                mt.setKetinggianMax(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MAX)));
                mt.setCurahHujanMin(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MIN)));
                mt.setCurahHujanMax(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MAX)));
                mt.setSuhuMin(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MIN)));
                mt.setSuhuMax(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MAX)));
//                mt.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
//                mt.setRekomendasiMenanam(cursor.getString(cursor.getColumnIndex(COLUMN_REKOMENDASI_MENANAM)));
//                mt.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mt);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data tanaman
        return  semuaData;
    }

    @Override
    public List<ModelTanaman> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list Modeltanaman
        List<ModelTanaman> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada / minimal 1 row
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //deklarasi variabel modelTanaman
                ModelTanaman modelTanaman = new ModelTanaman();
                modelTanaman.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                modelTanaman.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                modelTanaman.setUmur(cursor.getInt(cursor.getColumnIndex(COLUMN_UMUR)));
                modelTanaman.setMusim(cursor.getString(cursor.getColumnIndex(COLUMN_MUSIM)));
                modelTanaman.setKetinggianMin(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MIN)));
                modelTanaman.setKetinggianMax(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MAX)));
                modelTanaman.setCurahHujanMin(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MIN)));
                modelTanaman.setCurahHujanMax(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MAX)));
                modelTanaman.setSuhuMin(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MIN)));
                modelTanaman.setSuhuMax(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MAX)));
                modelTanaman.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                modelTanaman.setRekomendasiMenanam(cursor.getString(cursor.getColumnIndex(COLUMN_REKOMENDASI_MENANAM)));
                modelTanaman.setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(modelTanaman);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data tanaman
        return  semuaData;
    }

    @Override
    public ModelTanaman bacaDataDenganId(SQLiteDatabase dbWritable) {
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
                setUmur(cursor.getInt(cursor.getColumnIndex(COLUMN_UMUR)));
                setMusim(cursor.getString(cursor.getColumnIndex(COLUMN_MUSIM)));
                setKetinggianMin(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MIN)));
                setKetinggianMax(cursor.getInt(cursor.getColumnIndex(COLUMN_KETINGGIAN_MAX)));
                setCurahHujanMin(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MIN)));
                setCurahHujanMax(cursor.getInt(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MAX)));
                setSuhuMin(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MIN)));
                setSuhuMax(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MAX)));
                setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_DESKRIPSI)));
                setRekomendasiMenanam(cursor.getString(cursor.getColumnIndex(COLUMN_REKOMENDASI_MENANAM)));
                setBookmark(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKMARK)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model tanaman
        return this;
    }
}
