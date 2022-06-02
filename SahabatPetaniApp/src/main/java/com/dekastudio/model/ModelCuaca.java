package com.dekastudio.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.util.ArrayList;
import java.util.List;

public class ModelCuaca extends ModelDatabase<ModelCuaca> {
    //informasi nama kolom
    public static final String TABLE_NAME = "cuaca";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_LOKASI = "id_lokasi";
    public static final String COLUMN_UNIX_TIME = "unix_time";
    public static final String COLUMN_LAMA_HUJAN_SIANG = "lama_hujan_siang";
    public static final String COLUMN_LAMA_HUJAN_MALAM = "lama_hujan_malam";
    public static final String COLUMN_SUHU_SIANG = "suhu_siang";
    public static final String COLUMN_SUHU_MALAM = "suhu_malam";
    public static final String COLUMN_ANGIN_SIANG = "angin_siang";
    public static final String COLUMN_ANGIN_MALAM = "angin_malam";
    public static final String COLUMN_DETAIL_SIANG = "detail_siang";
    public static final String COLUMN_DETAIL_MALAM = "detail_malam";
    public static final String COLUMN_CURAH_HUJAN_SIANG = "curah_hujan_siang";
    public static final String COLUMN_CURAH_HUJAN_MALAM = "curah_hujan_malam";

    private int id;
    private int idLokasi;
    private long unixTime;
    private float lamaHujanSiang;
    private float lamaHujanMalam;
    private int suhuSiang;
    private int suhuMalam;
    private String anginSiang;
    private String anginMalam;
    private String detailSiang;
    private String detailMalam;
    private double curahHujanSiang;
    private double curahHujanMalam;

    //default construktor
    public ModelCuaca() {
    }

    public ModelCuaca(int id) {
        this.id = id;
    }

    public static void createTable(SQLiteDatabase db){
        Log.d(Constant.TAG, "membuat tabel cuaca");
        String query = "CREATE TABLE `"+TABLE_NAME+"` (" +
                "`"+COLUMN_ID+"`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "`"+COLUMN_ID_LOKASI+"`INTEGER NOT NULL," +
                "`"+COLUMN_UNIX_TIME+"`INTEGER NOT NULL," +
                "`"+COLUMN_LAMA_HUJAN_SIANG+"`REAL NOT NULL," +
                "`"+COLUMN_LAMA_HUJAN_MALAM+"`REAL NOT NULL," +
                "`"+COLUMN_SUHU_SIANG+"`INTEGER NOT NULL," +
                "`"+COLUMN_SUHU_MALAM+"`INTEGER NOT NULL," +
                "`"+COLUMN_ANGIN_SIANG+"`TEXT NOT NULL," +
                "`"+COLUMN_ANGIN_MALAM+"`TEXT NOT NULL," +
                "`"+COLUMN_DETAIL_SIANG+"`TEXT NOT NULL," +
                "`"+COLUMN_DETAIL_MALAM+"`TEXT NOT NULL," +
                "`"+COLUMN_CURAH_HUJAN_SIANG+"`REAL NOT NULL," +
                "`"+COLUMN_CURAH_HUJAN_MALAM+"`REAL NOT NULL," +
                "FOREIGN KEY(`"+COLUMN_ID_LOKASI+"`) REFERENCES `"+ ModelLokasi.TABLE_NAME+"`(`"+ ModelLokasi.COLUMN_ID+"`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";
        db.execSQL(query);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_LOKASI, getIdLokasi());
        cv.put(COLUMN_UNIX_TIME, getUnixTime());
        cv.put(COLUMN_LAMA_HUJAN_SIANG, getLamaHujanSiang());
        cv.put(COLUMN_LAMA_HUJAN_MALAM, getLamaHujanMalam());
        cv.put(COLUMN_SUHU_SIANG, getSuhuSiang());
        cv.put(COLUMN_SUHU_MALAM, getSuhuMalam());
        cv.put(COLUMN_ANGIN_SIANG, getAnginSiang());
        cv.put(COLUMN_ANGIN_MALAM, getAnginMalam());
        cv.put(COLUMN_DETAIL_SIANG, getDetailSiang());
        cv.put(COLUMN_DETAIL_MALAM, getDetailMalam());
        cv.put(COLUMN_CURAH_HUJAN_SIANG, getCurahHujanSiang());
        cv.put(COLUMN_CURAH_HUJAN_MALAM, getCurahHujanMalam());
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
    public List<ModelCuaca> bacaSemuaData(SQLiteDatabase dbWritable) {
        //deklarasi array list
        List<ModelCuaca> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelCuaca mc = new ModelCuaca();
                mc.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mc.setIdLokasi(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_LOKASI)));
                mc.setUnixTime(cursor.getInt(cursor.getColumnIndex(COLUMN_UNIX_TIME)));
                mc.setLamaHujanSiang(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_SIANG)));
                mc.setLamaHujanMalam(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_MALAM)));
                mc.setSuhuSiang(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_SIANG)));
                mc.setSuhuMalam(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MALAM)));
                mc.setAnginSiang(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_SIANG)));
                mc.setAnginMalam(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_MALAM)));
                mc.setDetailSiang(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_SIANG)));
                mc.setDetailMalam(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_MALAM)));
                mc.setCurahHujanSiang(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_SIANG)));
                mc.setCurahHujanMalam(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MALAM)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mc);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return semua data
        return  semuaData;
    }

    @Override
    public ModelCuaca bacaDataDenganId(SQLiteDatabase dbWritable) {
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE "+COLUMN_ID+"="+String.valueOf(getId());
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                setIdLokasi(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_LOKASI)));
                setUnixTime(cursor.getInt(cursor.getColumnIndex(COLUMN_UNIX_TIME)));
                setLamaHujanSiang(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_SIANG)));
                setLamaHujanMalam(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_MALAM)));
                setSuhuSiang(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_SIANG)));
                setSuhuMalam(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MALAM)));
                setAnginSiang(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_SIANG)));
                setAnginMalam(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_MALAM)));
                setDetailSiang(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_SIANG)));
                setDetailMalam(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_MALAM)));
                setCurahHujanSiang(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_SIANG)));
                setCurahHujanMalam(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MALAM)));
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();

        //return data model
        return this;
    }

    public static int bacaTerahirUpdateByLokasi(SQLiteDatabase dbWritable, ModelLokasi lokasi){
        Log.d(Constant.TAG, "baca terahir update by idLokasi ->" +TABLE_NAME);
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT max(unix_time) as max FROM " + TABLE_NAME;
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        int max = 0;
        if (cursor.moveToFirst()) {
            max = cursor.getInt(cursor.getColumnIndex("max"));
        }
        //close cursor untuk membebaskan memori
        cursor.close();
        //return semua data
        return max;
    }

    public static List<ModelCuaca> bacaDataByLokasi(SQLiteDatabase dbWritable, ModelLokasi lokasi){
        Log.d(Constant.TAG, "baca cuaca by idLokasi ->" +TABLE_NAME);
        //deklarasi array list
        List<ModelCuaca> semuaData= new ArrayList<>();
        //query untuk select dari database sqlite android
        String selectQuery = "SELECT  * FROM " + TABLE_NAME
                +" WHERE "+ COLUMN_ID_LOKASI + "=" + String.valueOf(lokasi.getId()) + " ORDER BY " + COLUMN_UNIX_TIME + " ASC";
        //baca data dengan cursor
        Cursor cursor = dbWritable.rawQuery(selectQuery, null);
        // pastikan data ada, cursor.MoveTofirst() akan return true jika data ada
        if (cursor.moveToFirst()) {
            //perulangan untuk membaca data menggunakan do while
            do {
                //set model dengan data dari database
                ModelCuaca mc = new ModelCuaca();
                mc.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                mc.setIdLokasi(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_LOKASI)));
                mc.setUnixTime(cursor.getInt(cursor.getColumnIndex(COLUMN_UNIX_TIME)));
                mc.setLamaHujanSiang(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_SIANG)));
                mc.setLamaHujanMalam(cursor.getFloat(cursor.getColumnIndex(COLUMN_LAMA_HUJAN_MALAM)));
                mc.setSuhuSiang(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_SIANG)));
                mc.setSuhuMalam(cursor.getInt(cursor.getColumnIndex(COLUMN_SUHU_MALAM)));
                mc.setAnginSiang(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_SIANG)));
                mc.setAnginMalam(cursor.getString(cursor.getColumnIndex(COLUMN_ANGIN_MALAM)));
                mc.setDetailSiang(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_SIANG)));
                mc.setDetailMalam(cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL_MALAM)));
                mc.setCurahHujanSiang(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_SIANG)));
                mc.setCurahHujanMalam(cursor.getDouble(cursor.getColumnIndex(COLUMN_CURAH_HUJAN_MALAM)));
                // menambahkan data tanaman ke ArrayList
                semuaData.add(mc);
            } while (cursor.moveToNext());
        }
        //close cursor untuk membebaskan memori
        cursor.close();
        Log.d(Constant.TAG, "panjang data cuaca " + String.valueOf(semuaData.size()));
        //return semua data
        return  semuaData;
    }

    public double getCurahHujanSiang() {
        return curahHujanSiang;
    }

    public void setCurahHujanSiang(double curahHujanSiang) {
        this.curahHujanSiang = curahHujanSiang;
    }

    public double getCurahHujanMalam() {
        return curahHujanMalam;
    }

    public void setCurahHujanMalam(double curahHujanMalam) {
        this.curahHujanMalam = curahHujanMalam;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(int idLokasi) {
        this.idLokasi = idLokasi;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public float getLamaHujanSiang() {
        return lamaHujanSiang;
    }

    public void setLamaHujanSiang(float lamaHujanSiang) {
        this.lamaHujanSiang = lamaHujanSiang;
    }

    public float getLamaHujanMalam() {
        return lamaHujanMalam;
    }

    public void setLamaHujanMalam(float lamaHujanMalam) {
        this.lamaHujanMalam = lamaHujanMalam;
    }

    public int getSuhuSiang() {
        return suhuSiang;
    }

    public void setSuhuSiang(int suhuSiang) {
        this.suhuSiang = suhuSiang;
    }

    public int getSuhuMalam() {
        return suhuMalam;
    }

    public void setSuhuMalam(int suhuMalam) {
        this.suhuMalam = suhuMalam;
    }

    public String getAnginSiang() {
        return anginSiang;
    }

    public void setAnginSiang(String anginSiang) {
        this.anginSiang = anginSiang;
    }

    public String getAnginMalam() {
        return anginMalam;
    }

    public void setAnginMalam(String anginMalam) {
        this.anginMalam = anginMalam;
    }

    public String getDetailSiang() {
        return detailSiang;
    }

    public void setDetailSiang(String detailSiang) {
        this.detailSiang = detailSiang;
    }

    public String getDetailMalam() {
        return detailMalam;
    }

    public void setDetailMalam(String detailMalam) {
        this.detailMalam = detailMalam;
    }
}
