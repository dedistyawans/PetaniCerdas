package com.dekastudio.helper;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.dekastudio.asyncronus.ImageSave;

import java.io.File;

public class ImageHelper {

    //untuk mencegah pembuatan objek dari class ini
    private ImageHelper(){}

    //fungsi yang akan mengembalikan path gambar dr external storage jika gambar tersedia ? mengembalikan url gambar untuk di download
    public static String getUrl(RequestQueue requestQueue, String folderName, String namaFile){
        if(namaFile.length()<=0) return  "";
        File file = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/" + namaFile.substring(0, namaFile.length()-4));
        if(file.exists()){
            Log.d(Constant.TAG, "menampilkan gambar dari sdcard " + namaFile.substring(0, namaFile.length()-4));
            return Environment.getExternalStorageDirectory() + "/" + folderName + "/" + namaFile.substring(0, namaFile.length()-4);
        }else{
            String baseImageUrl;
            String lokasiGambar;
            switch (folderName) {
                case Constant.TANAMAN_FOLDER:
                    baseImageUrl = Constant.FOTO_TANAMAN_URL;
                    lokasiGambar = Constant.TANAMAN_FOLDER;
                    break;
                case Constant.TANAH_FOLDER:
                    baseImageUrl = Constant.FOTO_TANAH_URL;
                    lokasiGambar = Constant.TANAH_FOLDER;
                    break;
                default:
                    baseImageUrl = Constant.FOTO_PENYAKIT_URL;
                    lokasiGambar = Constant.PENYAKIT_FOLDER;
                    break;
            }

            String urlGambar = baseImageUrl+namaFile;
            ImageRequest imageRequest = new ImageRequest(urlGambar, responses -> {
                //simpan gambar ke sdcard dengan nama file tanpa format utk menghindari ditampilkan di galeri oleh android
                new ImageSave(lokasiGambar, namaFile.substring(0, namaFile.length()-4), () -> {
                    //gambar sukses disimpan
                    Log.d(Constant.TAG, "gambar disimpan melalui runtime " + namaFile);
                }).execute(responses);
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, error -> {
                //terjadi kegagalan saat mengunduh gambar dengan volley
                Log.d(Constant.TAG, "gagal menyimpan gambar melalui runtime " + namaFile + "->" + error.toString());
            });
            //tambahkan ke requestqueue volley
            requestQueue.add(imageRequest);

            Log.d(Constant.TAG, "menampilkan gambar dari url dan download");
            return urlGambar;
        }
    }
}
