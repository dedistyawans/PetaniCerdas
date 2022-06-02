package com.dekastudio.database;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dekastudio.asyncronus.DataInsert;
import com.dekastudio.asyncronus.ImageSave;
import com.dekastudio.helper.Constant;
import com.dekastudio.model.ModelFotoPenyakit;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelFotoTanaman;

import org.json.JSONException;

import java.util.List;

/**
 * Created by DekaStudio on 11/6/2017.
 */

public class FirstTimeDownload {

    private DatabaseDownloadListener ddl;
    private DatabaseHelper dbhelper;
    private RequestQueue requestQueue;
    private int versi;

    public FirstTimeDownload(DatabaseHelper dbhelper, RequestQueue requestQueue, DatabaseDownloadListener ddl) {
        this.dbhelper = dbhelper;
        this.ddl = ddl;
        this.requestQueue = requestQueue;
    }

    String[] semuaFotoTanaman;
    String[] semuaFotoTanah;
    String[] semuaFotoPenyakit;
    public void eksekusi(){
        //TAG untuk request ini
        String TAG = Constant.TAG;

        //membuat JsonObjectrequest menggunakan volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.API_URL, null, response -> {

            //tampung versi database ini
            try {
                versi = response.getInt(Constant.API_VERSI_SERVER);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new DataInsert(dbhelper, () -> {
                //baca semua data dari tabel foto tanaman
                List<ModelFotoTanaman> semuaDataFotoTanaman = dbhelper.bacaSemuaData(new ModelFotoTanaman());
                //data semua nama file utk foto tanaman
                semuaFotoTanaman = new String[semuaDataFotoTanaman.size()];
                for(int i =0; i<semuaFotoTanaman.length; i++){
                    semuaFotoTanaman[i] = semuaDataFotoTanaman.get(i).getNamaFile();
                }

                //baca semua data dari tabel foto tanah
                List<ModelFotoTanah> semuaDataFotoTanah = dbhelper.bacaSemuaData(new ModelFotoTanah());
                //data semua nama file utk foto tanaman
                semuaFotoTanah = new String[semuaDataFotoTanah.size()];
                for(int i =0; i<semuaFotoTanah.length; i++){
                    semuaFotoTanah[i] = semuaDataFotoTanah.get(i).getNamaFile();
                }

                //baca semua data dari tabel foto penyakit
                List<ModelFotoPenyakit> semuaDataFotoPenyakit = dbhelper.bacaSemuaData(new ModelFotoPenyakit());
                //data semua nama file utk foto tanaman
                semuaFotoPenyakit = new String[semuaDataFotoPenyakit.size()];
                for(int i =0; i<semuaFotoPenyakit.length; i++){
                    semuaFotoPenyakit[i] = semuaDataFotoPenyakit.get(i).getNamaFile();
                }

                //jumlah download gambar yang akan di download
                jumlahGambar = semuaFotoTanaman.length;
                //download semua gambar utk tabel tanaman
                downloadSemuaGambar(Gambar.TANAMAN, 0);


            }).execute(response);

        }, error -> {
            //terjadi kegagalan saat mendownload database dari server, eksekusi interface onGagalDownload();
             ddl.onGagalDownload();
        });

        //Tag untuk request ini, berfungsi utk cancel request jika masih pending/dalam proses
        jsonObjectRequest.setTag(TAG);
        //tambahkan requestqueue ke volley request
        requestQueue.add(jsonObjectRequest);
    }




    private void downloadSemuaGambar(Gambar gambar, int posisi){

        String[] namaFile;
        if(gambar == Gambar.TANAMAN){
            namaFile = semuaFotoTanaman;
        }else if(gambar == Gambar.TANAH){
            namaFile = semuaFotoTanah;
        }else{
            namaFile = semuaFotoPenyakit;
        }

        //deklarasi urlgambar dan lokasiGambar dg nilai awal null
        String urlGambar = null, lokasiGambar = null;

        //pastikan length dari array lebih dr 0
        if(namaFile.length>0) {
            //menampung nama file
            String nama = namaFile[posisi];
            //percabangan utk menentukan darimana dan dimana file akan disimpan
            switch (gambar) {
                case TANAMAN:
                    urlGambar = Constant.FOTO_TANAMAN_URL + nama;
                    lokasiGambar = Constant.TANAMAN_FOLDER;
                    break;
                case TANAH:
                    urlGambar = Constant.FOTO_TANAH_URL + nama;
                    lokasiGambar = Constant.TANAH_FOLDER;
                    break;
                case PENYAKIT:
                    urlGambar = Constant.FOTO_PENYAKIT_URL + nama;
                    lokasiGambar = Constant.PENYAKIT_FOLDER;
                    break;
            }

            //variabel di akses melalui innerclass dalam kasus ini lamda, dibutuhkan final
            final String finalLokasiGambar = lokasiGambar;
            //request gambar dengan volley
            ImageRequest imageRequest = new ImageRequest(urlGambar, response -> {
                //simpan gambar ke sdcard dengan nama file tanpa format utk menghindari ditampilkan di galeri oleh android
                simpanKeSDCard(response, finalLokasiGambar, nama.substring(0, nama.length() - 4), gambar);
                //lakukan download gambar berikutnya
                if (posisi + 1 != namaFile.length) {
                    downloadSemuaGambar(gambar, posisi + 1);
                }

            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, error -> {
                //terjadi kegagalan saat mengunduh gambar dengan volley
                simpanKeSDCard(null, null, null, gambar);
                //lakukan download gambar berikutnya
                if (posisi + 1 != namaFile.length) {
                    downloadSemuaGambar(gambar, posisi + 1);
                }
            });
            //set TAG utk request ini dengan nama file, berfungsi utk cancel request ketika masih dalam keadaan pending/proses
            imageRequest.setTag(nama);
            //tambahkan ke requestqueue volley
            requestQueue.add(imageRequest);
        }

    }

    //variabel penampung untuk menampung total gambar yang akan di download
    private int jumlahGambar;

    //fungsi syncronized utk menyimpan gambar ke SD card, butuh syncronizhed karena fungsi ini akan di eksekusi oleh multithread dan tidak bisa secara atomic
    private synchronized void simpanKeSDCard(Bitmap bitmap, String lokasiFile, String namaFile, Gambar gambar){
        Log.d(Constant.TAG, "Menyimpan " + namaFile);
        //proses menyimpan bitmap ke SdCard android, pastikan bitmap dan namaFile tidak null
        if(bitmap!=null && lokasiFile!=null && namaFile!=null){
            new ImageSave(lokasiFile, namaFile, () -> {
                //berhubung volley RequestQueue tidak memberikan api callback ketika semua request telah selesai maka bisa di akali dengan increment decrement
                jumlahGambar--;

                //jika jumlah gambar sudah 0 artinya semua gambar yg berhasil diunduh tersimpan ke sdcard, entah sukses atau ada yg gagal
                if(jumlahGambar == 0 && gambar == Gambar.TANAMAN){
                    //jumlah download gambar yang akan di download
                    jumlahGambar = semuaFotoTanah.length;
                    //download semua gambar utk tabel penyakit
                    downloadSemuaGambar(Gambar.TANAH, 0);
                }else if(jumlahGambar == 0 && gambar == Gambar.TANAH){
                    //jumlah download gambar yang akan di download
                    jumlahGambar = semuaFotoPenyakit.length;
                    //download semua gambar utk tabel penyakit
                    downloadSemuaGambar(Gambar.PENYAKIT, 0);
                }else if(jumlahGambar == 0 && gambar == Gambar.PENYAKIT){
                    //saat selesai semua gambar sudah terdownload eksekusi onSelesaiDownload(); dengan mengirim parameter versi database yg baru saja selesai di download
                    ddl.onSelesaiDownload(versi);
                }
            }).execute(bitmap);
        }else {
            //berhubung volley RequestQueue tidak memberikan api callback ketika semua request telah selesai maka bisa di akali dengan increment decrement
            jumlahGambar--;
            //jika jumlah gambar sudah 0 artinya semua gambar yg berhasil diunduh tersimpan ke sdcard, entah sukses atau ada yg gagal
            if(jumlahGambar == 0 && gambar == Gambar.TANAMAN){
                //jumlah download gambar yang akan di download
                jumlahGambar = semuaFotoTanah.length;
                //download semua gambar utk tabel penyakit
                downloadSemuaGambar(Gambar.TANAH, 0);
            }else if(jumlahGambar == 0 && gambar == Gambar.TANAH){
                //jumlah download gambar yang akan di download
                jumlahGambar = semuaFotoPenyakit.length;
                //download semua gambar utk tabel penyakit
                downloadSemuaGambar(Gambar.PENYAKIT, 0);
            }else if(jumlahGambar == 0 && gambar == Gambar.PENYAKIT){
                //saat selesai semua gambar sudah terdownload eksekusi onSelesaiDownload(); dengan mengirim parameter versi database yg baru saja selesai di download
                ddl.onSelesaiDownload(versi);
            }
        }
    }
     
    //enumeration utk Gambar yang akan di download
    private enum Gambar{
        TANAMAN,
        TANAH,
        PENYAKIT
    }
}
