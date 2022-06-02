package com.dekastudio.asyncronus;

import android.os.AsyncTask;
import android.util.Log;

import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.model.ModelFotoPenyakit;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.model.ModelTanamanTanah;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DekaStudio on 11/7/2017.
 */

public class DataInsert extends AsyncTask<JSONObject, Void, Void> {
    private DatabaseHelper dbhelper;
    private ListenerDataInsert ldi;

    public DataInsert(DatabaseHelper dbhelper, ListenerDataInsert ldi) {
        this.dbhelper = dbhelper;
        this.ldi = ldi;
    }

    //doinbackground akan dieksekusi melalui background threat dengan parameter varargs (variabel arguments) JsonObject
    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {
        for(JSONObject response: jsonObjects) {
            //proses insert data dari Json server ke database lokal sqlite android
            try {
                //insert data ke tabel tanaman utk pertama kali
                insertTanaman(dbhelper, response.getJSONArray(ModelTanaman.TABLE_NAME));
                //insert data ke tabel foto tanaman utk pertama kali
                insertFotoTanaman(dbhelper, response.getJSONArray(ModelFotoTanaman.TABLE_NAME));
                //insert data ke tabel penyakit
                insertPenyakit(dbhelper, response.getJSONArray(ModelPenyakit.TABLE_NAME));
                //insert data ke tabel foto penyakit
                insertFotoPenyakit(dbhelper, response.getJSONArray(ModelFotoPenyakit.TABLE_NAME));
                //insert data ke tabel tanah
                insertTanah(dbhelper, response.getJSONArray(ModelTanah.TABLE_NAME));
                //insert data ke tabel foto tanah
                insertFotoTanah(dbhelper, response.getJSONArray(ModelFotoTanah.TABLE_NAME));
                //insert data ke tabel tanaman tanah
                insertTanamanTanah(dbhelper, response.getJSONArray(ModelTanamanTanah.TABLE_NAME));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ldi.onSelesaiInsert();
    }

    private void insertTanaman(DatabaseHelper dbhelper, JSONArray jsonArray) {
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelTanaman mt = new ModelTanaman();
                mt.setId(jsonObject.getInt(ModelTanaman.COLUMN_ID));
                mt.setNama(jsonObject.getString(ModelTanaman.COLUMN_NAMA));
                mt.setUmur(jsonObject.getInt(ModelTanaman.COLUMN_UMUR));
                mt.setMusim(jsonObject.getString(ModelTanaman.COLUMN_MUSIM));
                mt.setKetinggianMin(jsonObject.getInt(ModelTanaman.COLUMN_KETINGGIAN_MIN));
                mt.setKetinggianMax(jsonObject.getInt(ModelTanaman.COLUMN_KETINGGIAN_MAX));
                mt.setCurahHujanMin(jsonObject.getInt(ModelTanaman.COLUMN_CURAH_HUJAN_MIN));
                mt.setCurahHujanMax(jsonObject.getInt(ModelTanaman.COLUMN_CURAH_HUJAN_MAX));
                mt.setSuhuMin(jsonObject.getInt(ModelTanaman.COLUMN_SUHU_MIN));
                mt.setSuhuMax(jsonObject.getInt(ModelTanaman.COLUMN_SUHU_MAX));
                mt.setDeskripsi(jsonObject.getString(ModelTanaman.COLUMN_DESKRIPSI));
                mt.setRekomendasiMenanam(jsonObject.getString(ModelTanaman.COLUMN_REKOMENDASI_MENANAM));
                mt.setBookmark(0);
                //insert data
                dbhelper.insertData(mt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertFotoTanaman(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelFotoTanaman mft = new ModelFotoTanaman();
                mft.setId(jsonObject.getInt(ModelFotoTanaman.COLUMN_ID));
                mft.setIdTanaman(jsonObject.getInt(ModelFotoTanaman.COLUMN_ID_TANAMAN));
                mft.setNamaFile(jsonObject.getString(ModelFotoTanaman.COLUMN_NAMA_FILE));
                //insert data
                dbhelper.insertData(mft);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertPenyakit(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelPenyakit mp = new ModelPenyakit();
                mp.setId(jsonObject.getInt(ModelPenyakit.COLUMN_ID));
                mp.setIdTanaman(jsonObject.getInt(ModelPenyakit.COLUMN_ID_TANAMAN));
                mp.setCaraMenangani(jsonObject.getString(ModelPenyakit.COLUMN_CARA_MENANGANI));
                mp.setCiriCiri(jsonObject.getString(ModelPenyakit.COLUMN_CIRI_CIRI));
                mp.setDeskripsi(jsonObject.getString(ModelPenyakit.COLUMN_DESKRIPSI));
                mp.setNama(jsonObject.getString(ModelPenyakit.COLUMN_NAMA));
                mp.setBookmark(0);
                //insert data
                dbhelper.insertData(mp);
            }
        } catch (JSONException e) {
            Log.e("DekaError", e.toString());
            e.printStackTrace();
        }
    }

    private void insertFotoPenyakit(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelFotoPenyakit mfp = new ModelFotoPenyakit();
                mfp.setId(jsonObject.getInt(ModelFotoPenyakit.COLUMN_ID));
                mfp.setIdPenyakit(jsonObject.getInt(ModelFotoPenyakit.COLUMN_ID_PENYAKIT));
                mfp.setNamaFile(jsonObject.getString(ModelFotoPenyakit.COLUMN_NAMA_FILE));
                //insert data
                dbhelper.insertData(mfp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertTanah(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelTanah mt = new ModelTanah();
                mt.setId(jsonObject.getInt(ModelTanah.COLUMN_ID));
                mt.setNama(jsonObject.getString(ModelTanah.COLUMN_NAMA));
                mt.setDeskripsi(jsonObject.getString(ModelTanah.COLUMN_DESKRIPSI));
                mt.setBookmark(0);
                //insert data
                dbhelper.insertData(mt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertFotoTanah(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelFotoTanah mft = new ModelFotoTanah();
                mft.setId(jsonObject.getInt(ModelFotoTanah.COLUMN_ID));
                mft.setIdTanah(jsonObject.getInt(ModelFotoTanah.COLUMN_ID_TANAH));
                mft.setNamaFile(jsonObject.getString(ModelFotoTanah.COLUMN_NAMA_FILE));
                //insert data
                dbhelper.insertData(mft);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertTanamanTanah(DatabaseHelper dbhelper, JSONArray jsonArray){
        try{
            //baca json dan masukkan data
            for(int i =0; i<jsonArray.length(); i++) {
                //deklarasi json
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //deklarasi model dan isi data dari json server
                ModelTanamanTanah mtt = new ModelTanamanTanah();
                mtt.setId(jsonObject.getInt(ModelTanamanTanah.COLUMN_ID));
                mtt.setIdTanah(jsonObject.getInt(ModelTanamanTanah.COLUMN_ID_TANAH));
                mtt.setIdTanaman(jsonObject.getInt(ModelTanamanTanah.COLUMN_ID_TANAMAN));
                //insert data
                dbhelper.insertData(mtt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //interface listener utk Asyntask ini
    public interface ListenerDataInsert{
        void onSelesaiInsert();
    }
}
