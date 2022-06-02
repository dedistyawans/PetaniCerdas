package com.dekastudio.asyncronus;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
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

import java.io.File;
import java.util.List;

/**
 * Created by DekaStudio on 11/8/2017.
 */

public class DataUpdate extends AsyncTask<JSONObject, Void, Integer> {
    private DataUpdateListener dul;
    private DatabaseHelper dbhelper;
    private int versi;
    private boolean berhasil = true;
    private RequestQueue requestQueue;

    public DataUpdate(RequestQueue requestQueue, DatabaseHelper dbhelper, int versiClient, DataUpdateListener dul) {
        this.requestQueue = requestQueue;
        this.dul = dul;
        this.dbhelper = dbhelper;
        this.versi = versiClient;
    }

    @Override
    protected Integer doInBackground(JSONObject... jos) {
        JSONObject job = jos[0];
        try {
            JSONArray uarray = job.getJSONArray(Constant.API_UPDATE);
            for(int i = 0; i<uarray.length(); i++) {
                JSONObject update = uarray.getJSONObject(i);
                JSONObject data = job.getJSONObject(Constant.API_DATA_UPDATE);

                String aksi = update.getString(Constant.API_AKSI);
                String tabel = update.getString(Constant.API_NAMA_TABEL);
                int idDataServer = update.getInt(Constant.API_ID_DATA);

                versi = update.getInt(Constant.API_VERSI_SERVER);
                String key = "key"+String.valueOf(i);
                //periksa apakah status ok untuk menjalankan update
                if(data.getJSONObject(tabel).getJSONObject(key).getString("status").equals("ok")){
                    switch (aksi) {
                        case Constant.AKSI_INSERT:
                            Log.d("lacak_aksi", "insert");
                            insert(tabel, idDataServer, data, key);
                            break;
                        case Constant.AKSI_UPDATE:
                            Log.d("lacak_aksi", "update");
                            update(tabel, idDataServer, data, key);
                            break;
                        case Constant.AKSI_DELETE:
                            Log.d("lacak_aksi", "delete");
                            delete(tabel, idDataServer, data, key);
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JUpdate1", e.toString());
        }
        return versi;
    }


    @Override
    protected void onPostExecute(Integer integer) {
       if(berhasil){
           dul.onSelesaiUpdate(integer);
       }else{
           dul.onGagalUpdate();
       }
    }

    private void insert(String namaTabel, int idDataServer, JSONObject jo, String pemanggil){
        switch (namaTabel){
            case ModelTanaman.TABLE_NAME:
                iudTanaman(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelTanamanTanah.TABLE_NAME:
                iudTanamanTanah(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelTanah.TABLE_NAME:
                iudTanah(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelPenyakit.TABLE_NAME:
                iudPenyakit(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelFotoPenyakit.TABLE_NAME:
                iudFotoPenyakit(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanah.TABLE_NAME:
                iudFotoTanah(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanaman.TABLE_NAME:
                iudFotoTanaman(InsertUpdateDelete.INSERT, idDataServer, jo, pemanggil);
                break;
        }
    }



    private void update(String namaTabel, int idDataServer, JSONObject jo, String pemanggil){
        switch (namaTabel){
            case ModelTanaman.TABLE_NAME:
                Log.d("lacak", "dua");
                iudTanaman(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelTanamanTanah.TABLE_NAME:
                iudTanamanTanah(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelTanah.TABLE_NAME:
                iudTanah(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelPenyakit.TABLE_NAME:
                iudPenyakit(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoPenyakit.TABLE_NAME:
                iudFotoPenyakit(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanah.TABLE_NAME:
                iudFotoTanah(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanaman.TABLE_NAME:
                iudFotoTanaman(InsertUpdateDelete.UPDATE, idDataServer, jo, pemanggil);
                break;
        }
    }

    private void delete(String namaTabel, int idDataServer, JSONObject jo, String pemanggil){
        switch (namaTabel){
            case ModelTanaman.TABLE_NAME:
                iudTanaman(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelTanamanTanah.TABLE_NAME:
                iudTanamanTanah(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelTanah.TABLE_NAME:
                iudTanah(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelPenyakit.TABLE_NAME:
                iudPenyakit(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoPenyakit.TABLE_NAME:
                iudFotoPenyakit(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanah.TABLE_NAME:
                iudFotoTanah(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
            case ModelFotoTanaman.TABLE_NAME:
                iudFotoTanaman(InsertUpdateDelete.DELETE, idDataServer, jo, pemanggil);
                break;
        }
    }

    private void iudTanaman(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //sebelum data dihapus dari databse simpan id dulu tuk kebutuhan hapus foto nantinya
            int idTanaman = dbhelper.bacaDataDenganId(new ModelTanaman(idDataServer)).getId();
            //Hapus foto tanaman dengan idTanaman ini (test)
            List<ModelFotoTanaman> listFoto = ModelFotoTanaman.bacaFotoTanaman(dbhelper.getOpenDatabase(), idTanaman);
            for(ModelFotoTanaman fotoTanaman : listFoto){
                hapusFoto(Constant.TANAMAN_FOLDER, fotoTanaman.getNamaFile());
            }
            //delete data
            dbhelper.deleteData(new ModelTanaman(idDataServer));
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelTanaman.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                ModelTanaman mt = new ModelTanaman();
                mt.setId(response.getInt(ModelTanaman.COLUMN_ID));
                mt.setNama(response.getString(ModelTanaman.COLUMN_NAMA));
                mt.setUmur(response.getInt(ModelTanaman.COLUMN_UMUR));
                mt.setMusim(response.getString(ModelTanaman.COLUMN_MUSIM));
                mt.setKetinggianMin(response.getInt(ModelTanaman.COLUMN_KETINGGIAN_MIN));
                mt.setKetinggianMax(response.getInt(ModelTanaman.COLUMN_KETINGGIAN_MAX));
                mt.setCurahHujanMin(response.getInt(ModelTanaman.COLUMN_CURAH_HUJAN_MIN));
                mt.setCurahHujanMax(response.getInt(ModelTanaman.COLUMN_CURAH_HUJAN_MAX));
                mt.setSuhuMin(response.getInt(ModelTanaman.COLUMN_SUHU_MIN));
                mt.setSuhuMax(response.getInt(ModelTanaman.COLUMN_SUHU_MAX));
                mt.setDeskripsi(response.getString(ModelTanaman.COLUMN_DESKRIPSI));
                mt.setRekomendasiMenanam(response.getString(ModelTanaman.COLUMN_REKOMENDASI_MENANAM));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    mt.setBookmark(0);
                    dbhelper.insertData(mt);
                } else {
                    Log.d("lacak", "tiga");
                    dbhelper.updateData(mt, idDataServer);
                }
            } catch (JSONException e) { //exception terjadi jika Json kosong, tak perlu lakukan apapun
                //periksa apakah semua proses berjalan dengan lancar, jika iya maka eksekusi onSelesaiUpdate();
                berhasil = false;
                Log.e("JUpdateIUDTanaman", e.toString());
            }

        }
    }

    private void iudTanamanTanah(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //delete data
            dbhelper.deleteData(new ModelTanamanTanah(idDataServer));
        }else{
            try {
                JSONObject response = main.getJSONObject(ModelTanamanTanah.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                //deklarasi model dan isi data dari json server
                ModelTanamanTanah mtt = new ModelTanamanTanah();
                mtt.setId(response.getInt(ModelTanamanTanah.COLUMN_ID));
                mtt.setIdTanah(response.getInt(ModelTanamanTanah.COLUMN_ID_TANAH));
                mtt.setIdTanaman(response.getInt(ModelTanamanTanah.COLUMN_ID_TANAMAN));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    dbhelper.insertData(mtt);
                } else {
                    dbhelper.updateData(mtt, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                //periksa apakah semua proses berjalan dengan lancar, jika iya maka eksekusi onSelesaiUpdate();
                berhasil = false;
                Log.e("JUpdateIUDTanamanTanah", e.toString());
            }
        }
    }

    private void iudTanah(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //sebelum data dihapus dari database simpan dulu id nya utk keperluan hapus foto
            int idTanah = dbhelper.bacaDataDenganId(new ModelTanah(idDataServer)).getId();

            // Hapus foto tanah dg idTanah ini (test)
            List<ModelFotoTanah> listFoto = ModelFotoTanah.bacaFotoTanah(dbhelper.getOpenDatabase(), idTanah);
            for(ModelFotoTanah fotoTanah : listFoto){
                hapusFoto(Constant.TANAH_FOLDER, fotoTanah.getNamaFile());
            }
            //delete data
            dbhelper.deleteData(new ModelTanah(idDataServer));
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelTanah.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                //deklarasi model dan isi data dari json server
                ModelTanah mt = new ModelTanah();
                mt.setId(response.getInt(ModelTanah.COLUMN_ID));
                mt.setNama(response.getString(ModelTanah.COLUMN_NAMA));
                mt.setDeskripsi(response.getString(ModelTanah.COLUMN_DESKRIPSI));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    mt.setBookmark(0);
                    dbhelper.insertData(mt);
                } else {
                    dbhelper.updateData(mt, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                berhasil = false;
                Log.e("JUpdateIUDTanah", e.toString());
            }
        }
    }

    private void iudPenyakit(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //sebelum data dihapus dari database simpan dulu id nya utk keperluan hapus foto
            int idPenyakit = dbhelper.bacaDataDenganId(new ModelPenyakit(idDataServer)).getId();

            //hapus foto penyakit dg id penyakit ini
            List<ModelFotoPenyakit> listFoto = ModelFotoPenyakit.bacaFotoPenyakit(dbhelper.getOpenDatabase(), idPenyakit);
            for(ModelFotoPenyakit fotoPenyakit : listFoto){
                hapusFoto(Constant.PENYAKIT_FOLDER, fotoPenyakit.getNamaFile());
            }
            //delete data
            dbhelper.deleteData(new ModelPenyakit(idDataServer));
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelPenyakit.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                ModelPenyakit mp = new ModelPenyakit();
                mp.setId(response.getInt(ModelPenyakit.COLUMN_ID));
                mp.setIdTanaman(response.getInt(ModelPenyakit.COLUMN_ID_TANAMAN));
                mp.setCaraMenangani(response.getString(ModelPenyakit.COLUMN_CARA_MENANGANI));
                mp.setCiriCiri(response.getString(ModelPenyakit.COLUMN_CIRI_CIRI));
                mp.setDeskripsi(response.getString(ModelPenyakit.COLUMN_DESKRIPSI));
                mp.setNama(response.getString(ModelPenyakit.COLUMN_NAMA));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    mp.setBookmark(0);
                    dbhelper.insertData(mp);
                } else {
                    dbhelper.updateData(mp, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                berhasil = false;
                Log.e("JUpdateIUDPenyakit", e.toString());
            }
        }
    }

    private void iudFotoPenyakit(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //dapatkan nama file foto sebalum di hapus
            String namaFile = dbhelper.bacaDataDenganId(new ModelFotoPenyakit(idDataServer)).getNamaFile();
            //delete data
            dbhelper.deleteData(new ModelFotoPenyakit(idDataServer));
            //Hapus foto penyakit dan jika sukses eksekusi onSelesaiUpdate();
            hapusFoto(Constant.PENYAKIT_FOLDER, namaFile);
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelFotoPenyakit.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                //deklarasi model dan isi data dari json server
                ModelFotoPenyakit mfp = new ModelFotoPenyakit();
                mfp.setId(response.getInt(ModelFotoPenyakit.COLUMN_ID));
                mfp.setIdPenyakit(response.getInt(ModelFotoPenyakit.COLUMN_ID_PENYAKIT));
                mfp.setNamaFile(response.getString(ModelFotoPenyakit.COLUMN_NAMA_FILE));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    dbhelper.insertData(mfp);
                    String namaFile = response.getString(ModelFotoPenyakit.COLUMN_NAMA_FILE);
                    //variabel utk menyimpan urlGambar yang akan di download
                    String urlGambar = Constant.FOTO_PENYAKIT_URL + namaFile;
                    //variabel letak dimana gambar kana disimpan di SdCard
                    String lokasiGambar = Constant.PENYAKIT_FOLDER;
                    //request gambar dengan volley
                    ImageRequest imageRequest = new ImageRequest(urlGambar, responses -> {
                        //simpan gambar ke sdcard dengan nama file tanpa format utk menghindari ditampilkan di galeri oleh android
                        new ImageSave(lokasiGambar, namaFile.substring(0, namaFile.length()-4), () -> {
                            //gambar sukses disimpan
                        }).execute(responses);
                    }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, error -> {
                        //terjadi kegagalan saat mengunduh gambar dengan volley
                    });
                    //tambahkan ke requestqueue volley
                    requestQueue.add(imageRequest);
                } else {
                    dbhelper.updateData(mfp, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                berhasil = false;
                Log.e("JUpdateIUDFotoPenyakit", e.toString());
            }
        }
    }

    private void iudFotoTanah(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //dapatkan nama file foto sebelum dihapus
            String namaFile = dbhelper.bacaDataDenganId(new ModelFotoTanah(idDataServer)).getNamaFile();
            //delete data
            dbhelper.deleteData(new ModelFotoTanah(idDataServer));
            //hapus foto tanah
            hapusFoto(Constant.TANAH_FOLDER, namaFile);
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelFotoTanah.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                //deklarasi model dan isi data dari json server
                ModelFotoTanah mft = new ModelFotoTanah();
                mft.setId(response.getInt(ModelFotoTanah.COLUMN_ID));
                mft.setIdTanah(response.getInt(ModelFotoTanah.COLUMN_ID_TANAH));
                mft.setNamaFile(response.getString(ModelFotoTanah.COLUMN_NAMA_FILE));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    dbhelper.insertData(mft);
                    String namaFile = response.getString(ModelFotoTanah.COLUMN_NAMA_FILE);
                    //variabel utk menyimpan urlGambar yang akan di download
                    String urlGambar = Constant.FOTO_TANAH_URL + namaFile;
                    //variabel letak dimana gambar kana disimpan di SdCard
                    String lokasiGambar = Constant.TANAH_FOLDER;
                    //request gambar dengan volley
                    ImageRequest imageRequest = new ImageRequest(urlGambar, responses -> {
                        //simpan gambar ke sdcard dengan nama file tanpa format utk menghindari ditampilkan di galeri oleh android
                        new ImageSave(lokasiGambar, namaFile.substring(0, namaFile.length()-4), () -> {
                            //gambar sukses disimpan
                        }).execute(responses);
                    }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, error -> {
                        //terjadi kegagalan saat mengunduh gambar dengan volley
                    });
                    //tambahkan ke requestqueue volley
                    requestQueue.add(imageRequest);
                } else {
                    dbhelper.updateData(mft, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                //periksa apakah semua proses berjalan dengan lancar, jika iya maka eksekusi onSelesaiUpdate();
                berhasil = false;
                Log.e("JUpdateIUDFotoTanah", e.toString());
            }
        }
    }

    private void iudFotoTanaman(InsertUpdateDelete iud, int idDataServer, JSONObject main, String pemanggil){
        if(iud == InsertUpdateDelete.DELETE){
            //dapatkan nama file sebelum data di hapus dari database
            String namaFile = dbhelper.bacaDataDenganId(new ModelFotoTanaman(idDataServer)).getNamaFile();
            //delete data
            dbhelper.deleteData(new ModelFotoTanaman(idDataServer));
            //hapus foto tanaman
            hapusFoto(Constant.TANAMAN_FOLDER, namaFile);
        }else {
            try {
                JSONObject response = main.getJSONObject(ModelFotoTanaman.TABLE_NAME).getJSONObject(pemanggil).getJSONArray("value").getJSONObject(0);
                //deklarasi model dan isi data dari json server
                ModelFotoTanaman mft = new ModelFotoTanaman();
                mft.setId(response.getInt(ModelFotoTanaman.COLUMN_ID));
                mft.setIdTanaman(response.getInt(ModelFotoTanaman.COLUMN_ID_TANAMAN));
                mft.setNamaFile(response.getString(ModelFotoTanaman.COLUMN_NAMA_FILE));
                //masukkan ke database
                if (iud == InsertUpdateDelete.INSERT) {
                    dbhelper.insertData(mft);
                    String namaFile = response.getString(ModelFotoTanaman.COLUMN_NAMA_FILE);
                    //variabel utk menyimpan urlGambar yang akan di download
                    String urlGambar = Constant.FOTO_TANAMAN_URL + namaFile;
                    //variabel letak dimana gambar kana disimpan di SdCard
                    String lokasiGambar = Constant.TANAMAN_FOLDER;
                    //request gambar dengan volley
                    ImageRequest imageRequest = new ImageRequest(urlGambar, responses -> {
                        //simpan gambar ke sdcard dengan nama file tanpa format utk menghindari ditampilkan di galeri oleh android
                        new ImageSave(lokasiGambar, namaFile.substring(0, namaFile.length()-4), () -> {
                            //gambar sukses disimpan
                        }).execute(responses);
                    }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, error -> {
                        //terjadi kegagalan saat mengunduh gambar dengan volley
                    });
                    //tambahkan ke requestqueue volley
                    requestQueue.add(imageRequest);
                } else {
                    dbhelper.updateData(mft, idDataServer);
                }
            } catch (JSONException e) {//exception terjadi jika Json kosong, tak perlu lakukan apapun
                //periksa apakah semua proses berjalan dengan lancar, jika iya maka eksekusi onSelesaiUpdate();
                berhasil = false;
                Log.e("JUpdateIUDFotoTanaman", e.toString());
            }
        }
    }

    private void hapusFoto(String folder, String namaFile){
        String root = Environment.getExternalStorageDirectory().toString();
        File folderFile = new File(root + "/" + folder);
        File foto = new File(folderFile, namaFile.substring(0, namaFile.length()-4));
        if(foto.exists()){
            if(foto.delete()){
                Log.d(Constant.TAG, "Terhapus " + folder + "/" + namaFile);
            }
        }
    }

    //interface utk menangani callback saat aksi asyncronus telah selesai
    public interface DataUpdateListener{
        //akan di eksekusi saat semua proses berjalan dengan sukses
        void onSelesaiUpdate(int versi);
        //kan di eksekusi jika terjadi masalah saat mengunduh pembaruan atau parsing json
        void onGagalUpdate();
    }

    //enum utk menentukan aksi termasuk insert atau update atau delete data
    private enum InsertUpdateDelete{
        //tipe utk insert
        INSERT,
        //tipe utk update
        UPDATE,
        //tipe utk delete
        DELETE
    }

}
