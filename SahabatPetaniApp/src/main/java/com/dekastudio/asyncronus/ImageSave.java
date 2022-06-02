package com.dekastudio.asyncronus;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DekaStudio on 11/6/2017.
 */

public class ImageSave extends AsyncTask<Bitmap, Void, Void>{

    private String lokasiFile, namaFile;
    private ListenerImageSave lis;

    public ImageSave(String lokasiFile, String namaFile, ListenerImageSave lis) {
        this.lokasiFile = lokasiFile;
        this.namaFile = namaFile;
        this.lis = lis;
    }

    //doinbackground akan dieksekusi melalui background threat dengan parameter varargs (variable arguments) Bitmap
    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        for(Bitmap bitmap : bitmaps) {
            //mendapatkan root folder sdcard
            String root = Environment.getExternalStorageDirectory().toString();
            //deklarasi File direktori tempat penyimpanan gambar
            File myDir = new File(root + "/" + lokasiFile);
            //periksa apakah folder yg dimaksud ada
            if (!myDir.exists()) {
                //buat folder jika folder itu blm ada
                myDir.mkdirs();
            }

            //deklarasi file dengan lokasi/path dan nama filenya
            File file = new File(myDir, namaFile);
            //jika file sebelumnya ada, hapus file tersebut karena akan digantikan dg yg baru
            if (file.exists()) file.delete();
            //proses penyimpanan memungkinkan menimbulkan throwable IOException, dibutuhkan try catch
            try {
                //output stream utk file
                FileOutputStream out = new FileOutputStream(file);
                //compress bitmap dengan format PNG utk mendukung Alpha Transparan, quality 100, ke output stream out
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                //flush bitmap
                out.flush();
                //close output stream
                out.close();
            } catch (IOException e) {
                //proses penyimpanan ke sdcard mengalami IOExceptin
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(Constant.TAG, "Menyimpan " + lokasiFile + "/" + namaFile);
        lis.onSelesaiSave();
    }

    public interface ListenerImageSave{
        void onSelesaiSave();
    }

}
