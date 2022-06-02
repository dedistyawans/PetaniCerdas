package com.dekastudio.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.dekastudio.core.AppCore;
import com.dekastudio.core.Preference;
import com.dekastudio.customview.DekaDialog;
import com.dekastudio.database.CuacaDatabase;
import com.dekastudio.database.DatabaseDownloadListener;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.database.DatabaseManagement;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.PermissionHelper;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.sahabatpetanicerdas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class SplashActivity extends AppCompatActivity {

    //object preference utk mengatur versi client
    Preference preference;
    //Object permission helper utk meminta runtime permission
    PermissionHelper permissionHelper;
    //object Application
    private AppCore appCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //inisialisasi object application
        appCore = (AppCore) getApplication();

        //inisialisasi preference dg context activity ini
        preference = new Preference(SplashActivity.this, ((AppCore)getApplication()).getRequestQueue());

        //Runtime permission dibutuhakan mulai dari android 23 keatas
        Log.d(Constant.TAG, "Meminta ijin...");
        permissionHelper = new PermissionHelper(this, PermissionHelper.WRITE_EXTERNAL_STORAGE, PermissionHelper.READ_EXTERNAL_STORAGE, PermissionHelper.ACCESS_COARSE_LOCATION, PermissionHelper.ACCESS_FINE_LOCATION);
        permissionHelper.setPermissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionGranted() { //dieksekusi ketika semua permission yang diminta di grant
                appCore.requestLocation();
                aksiSetelahPermisiDiIjinkan();
                Log.d(Constant.TAG, "Semua permission di granted");
            }

            @Override
            public void onPermissionDenied() { //dieksekusi ketika salah satu/semua permission yg diminta di denied
                Toast.makeText(SplashActivity.this, "Ijin diperlukan agar aplikasi berjalan normal!", Toast.LENGTH_SHORT).show();
                permissionHelper.requestPermission();
                Log.d(Constant.TAG, "Ada permission di denied");
            }
        });
        permissionHelper.requestPermission();
    }

    String namaLokasi;
    double latitude, longitude, altitude = 0d;
    @SuppressLint("SetTextI18n")
    private void aksiSetelahPermisiDiIjinkan(){
        if(!appCore.apakahGpsOn() && ModelLokasi.apakahMasihKosong(new DatabaseHelper(SplashActivity.this).getOpenDatabase())){
            //alert butuh informasi lokasi ->hidupkan GPS
            AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("GPS dibutuhkan!");
            alertDialog.setMessage("GPS harus dihidupkan untuk mendapatkan prakiraan cuaca, ketinggian tanah dan lokasi anda untuk yang pertama kali.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Coba Lagi", (dialog, which) -> {
                aksiSetelahPermisiDiIjinkan();
            });
            //atur intergace onClick untuk Button Positif
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Keluar", (dialog, which) -> {
                //tutup alertdialog
                dialog.dismiss();
                //keluar dari activity ini (keluar dr aplikasi)
                finish();
            });
            //atur intergace onClick untuk Button Positif
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hidupkan GPS", (dialog, which) -> {
                //intent untuk mengarahkan ke pengaturan lokasi Android
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                aksiSetelahPermisiDiIjinkan();
            });
            alertDialog.show();
        }else if(appCore.apakahLokasiNull() && ModelLokasi.apakahMasihKosong(new DatabaseHelper(SplashActivity.this).getOpenDatabase())){
            //aksi tambahkan lokasi disini
            latitude = 0d;
            longitude = 0d;
            altitude = 0d;

            DekaDialog dekaDialog = new DekaDialog(SplashActivity.this, R.layout.dialog_add_lokasi);
            dekaDialog.setBatalPadaSisiLuar(false);
            dekaDialog.setDapatDiBatal(false);

            Button btnSimpan = dekaDialog.getView().findViewById(R.id.dialogBtnSimpan);
            Button btnBatal = dekaDialog.getView().findViewById(R.id.dialogBtnBatal);
            btnBatal.setVisibility(View.GONE);

            EditText editAddres = dekaDialog.getView().findViewById(R.id.dialogEditAddress);
            Button btnCari = dekaDialog.getView().findViewById(R.id.dialogBtnCari);
            ProgressBar progressCari = dekaDialog.getView().findViewById(R.id.dialogProgressCari);

            RadioButton radioBtnAddress = dekaDialog.getView().findViewById(R.id.dialogRadioAddress);
            RadioButton radioBtnGps = dekaDialog.getView().findViewById(R.id.dialogRadioGps);

            TextView txtKoordinat = dekaDialog.getView().findViewById(R.id.dialogTxtKoordinate);
            TextView txtAltitude = dekaDialog.getView().findViewById(R.id.dialogTxtAltitude);

            TextView txtUnduhCuaca = dekaDialog.getView().findViewById(R.id.dialogTxtUnduh);
            ProgressBar progressSimpan = dekaDialog.getView().findViewById(R.id.dialogProgressSimpan);

            EditText editNama = dekaDialog.getView().findViewById(R.id.dialogEditNama);

            btnSimpan.setOnClickListener(v -> {
                if(!editNama.getText().toString().isEmpty()){
                    editAddres.setEnabled(false);
                    btnCari.setEnabled(false);
                    radioBtnAddress.setEnabled(false);
                    radioBtnGps.setEnabled(false);
                    editNama.setEnabled(false);

                    appCore.hapusListenerTerbaik();

                    btnSimpan.setVisibility(View.GONE);
                    txtUnduhCuaca.setVisibility(View.VISIBLE);
                    progressSimpan.setVisibility(View.VISIBLE);
                    CuacaDatabase cd = new CuacaDatabase(SplashActivity.this, ((AppCore)getApplication()).getRequestQueue());
                    Location l = new Location("lok");
                    l.setLatitude(latitude);
                    l.setLongitude(longitude);
                    l.setAltitude(altitude);
                    cd.downloadCuaca(l, editNama.getText().toString());
                    cd.setListenerCuaca(new CuacaDatabase.ListenerCuaca() {
                        @Override
                        public void onSelesaiDownload(ModelLokasi lokasi) {
                            dekaDialog.tutupDialog();
                            Toast.makeText(appCore, "Berhasil menyimpan lokasi baru", Toast.LENGTH_SHORT).show();
                            aksiSetelahPermisiDiIjinkan();
                        }

                        @Override
                        public void onGagalDownload() {
                            dekaDialog.tutupDialog();
                            Toast.makeText(appCore, "Lokasi gagal disimpan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(SplashActivity.this, "Nama lokasi tak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
            });

            radioBtnAddress.setOnClickListener(v -> {
                appCore.hapusListenerTerbaik();
                radioBtnAddress.setChecked(true);
                radioBtnGps.setChecked(false);
                editAddres.setEnabled(true);
                btnCari.setEnabled(true);
            });

            radioBtnGps.setOnClickListener(v -> {
                if(appCore.apakahGpsOn() && appCore.getLokasiTerbaik()!=null) {
                    radioBtnAddress.setChecked(false);
                    radioBtnGps.setChecked(true);
                    editAddres.setEnabled(false);
                    btnCari.setEnabled(false);
                    Location loc = appCore.getLokasiTerbaik();
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    altitude = loc.getAltitude();
                    txtKoordinat.setText("Koordinat : " + new DecimalFormat(".##").format(loc.getLatitude()) +"," +new DecimalFormat(".##").format(loc.getLongitude()));
                    txtAltitude.setText("Ketinggian : " + new DecimalFormat(".##").format(loc.getAltitude()) + " MDPL");
                    appCore.setListenerLokasiTerbaik(location -> {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        altitude = location.getAltitude();
                        txtKoordinat.setText("Koordinat : " + new DecimalFormat(".##").format(location.getLatitude()) +"," +new DecimalFormat(".##").format(location.getLongitude()));
                        txtAltitude.setText("Ketinggian : " + new DecimalFormat(".##").format(location.getAltitude()) + " MDPL");
                    });
                    btnSimpan.setEnabled(true);
                }else{
                    radioBtnGps.setChecked(false);
                    radioBtnAddress.setChecked(true);
                    editAddres.setEnabled(true);
                    btnCari.setEnabled(true);
                    Toast.makeText(SplashActivity.this, "Hidupkan GPS untuk menggunakan fitur ini!", Toast.LENGTH_LONG).show();
                }
            });


            btnCari.setOnClickListener(v -> {
                if(!editAddres.getText().toString().equals("")) {
                    btnCari.setVisibility(View.GONE);
                    progressCari.setVisibility(View.VISIBLE);
                    radioBtnAddress.setEnabled(false);
                    radioBtnGps.setEnabled(false);
                    String urlGeoFix = Constant.GEOCODING_API_URL
                            + editAddres.getText().toString().replaceAll(" ", "+")
                            + Constant.GOOGLE_API_KEY;
                    Log.d(Constant.TAG, "Req Geocoding ->" + urlGeoFix);
                    JsonObjectRequest jor = new JsonObjectRequest(urlGeoFix, null, response -> {
                        try {
                            if(response.getString("status").equals("OK")){
                                JSONObject jo = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                latitude = jo.getDouble("lat");
                                longitude = jo.getDouble("lng");
                                String urlEleFix = Constant.ELEVATION_API_URL + String.valueOf(latitude)+","+String.valueOf(longitude)+Constant.GOOGLE_API_KEY;
                                Log.d(Constant.TAG, "Req Elevation ->" + urlEleFix);
                                JsonObjectRequest jorl = new JsonObjectRequest(urlEleFix, null, response1 -> {
                                    try {
                                        if(response.getString("status").equals("OK")) {
                                            JSONObject jol = response1.getJSONArray("results").getJSONObject(0);
                                            altitude = jol.getDouble("elevation");
                                            latitude = jol.getJSONObject("location").getDouble("lat");
                                            longitude = jol.getJSONObject("location").getDouble("lng");
                                            txtKoordinat.setText("Koordinat : " + new DecimalFormat(".####").format(latitude) + "," + new DecimalFormat(".####").format(longitude));
                                            txtAltitude.setText("Ketinggian : "+ new DecimalFormat(".##").format(altitude) +" MDPL");
                                            btnSimpan.setEnabled(true);
                                            btnCari.setVisibility(View.VISIBLE);
                                            progressCari.setVisibility(View.GONE);
                                            radioBtnAddress.setEnabled(true);
                                            radioBtnGps.setEnabled(true);
                                            btnSimpan.setEnabled(true);
                                        }else{
                                            btnCari.setVisibility(View.VISIBLE);
                                            progressCari.setVisibility(View.GONE);
                                            radioBtnAddress.setEnabled(true);
                                            radioBtnGps.setEnabled(true);
                                            Toast.makeText(SplashActivity.this, "Ketinggian tanah tak diketahui!", Toast.LENGTH_LONG).show();
                                            Log.d(Constant.TAG, "Elevation Fail ->" + "status tidak OK");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        btnCari.setVisibility(View.VISIBLE);
                                        progressCari.setVisibility(View.GONE);
                                        radioBtnAddress.setEnabled(true);
                                        radioBtnGps.setEnabled(true);
                                        Toast.makeText(SplashActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                                        Log.d(Constant.TAG, "Elevation Fail ->" + e.toString());
                                    }

                                }, error -> {
                                    btnCari.setVisibility(View.VISIBLE);
                                    progressCari.setVisibility(View.GONE);
                                    radioBtnAddress.setEnabled(true);
                                    radioBtnGps.setEnabled(true);
                                    Toast.makeText(SplashActivity.this, "Error : "+error.toString(), Toast.LENGTH_LONG).show();
                                    Log.d(Constant.TAG, "Elevation Fail ->" + error.toString());
                                });
                                ((AppCore)getApplication()).getRequestQueue().add(jorl);
                            }else{
                                btnCari.setVisibility(View.VISIBLE);
                                progressCari.setVisibility(View.GONE);
                                radioBtnAddress.setEnabled(true);
                                radioBtnGps.setEnabled(true);
                                Toast.makeText(SplashActivity.this, "Lokasi tak ditemukan!", Toast.LENGTH_LONG).show();
                                Log.d(Constant.TAG, "Geo Fail ->" + "status tidak OK");
                            }
                        } catch (JSONException e) {
                            btnCari.setVisibility(View.VISIBLE);
                            progressCari.setVisibility(View.GONE);
                            radioBtnAddress.setEnabled(true);
                            radioBtnGps.setEnabled(true);
                            e.printStackTrace();
                            Toast.makeText(SplashActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                            Log.d(Constant.TAG, "Geo Fail ->" + e.toString());
                        }
                    }, error -> {
                        btnCari.setVisibility(View.VISIBLE);
                        progressCari.setVisibility(View.GONE);
                        radioBtnAddress.setEnabled(true);
                        radioBtnGps.setEnabled(true);
                        Toast.makeText(SplashActivity.this, "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.d(Constant.TAG, "Geo Fail ->" + error.toString());
                    });
                    ((AppCore)getApplication()).getRequestQueue().add(jor);
                }else{
                    Toast.makeText(SplashActivity.this, "Pencarian tak boleh kosong!", Toast.LENGTH_LONG).show();
                }
            });
            dekaDialog.tampilkanDialog();
        } else {
            //database manajemen
            manajemenDatabaseUpdate();
//            //request info lokasi
//            appCore.requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.setOnRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.setOnActivityResult(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean apakahTerkoneksiKeInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private void manajemenDatabaseUpdate(){
        //object database management dengan nama dbManagement
        DatabaseManagement dbManagement = new DatabaseManagement(this, ((AppCore)getApplication()).getRequestQueue());

        //periksa apakah device terhubung ke internet
        if(apakahTerkoneksiKeInternet()){
            if(preference.getVersiClient() == 0){
                //set waktu splash ke 0
                waktuSplah = 0;
                //download database untuk pertama kali
                dbManagement.downloadDatabase(new DatabaseDownloadListener() {
                    @Override
                    public void onSelesaiDownload(int versi) {
                        //atur versi client ke veri database yang berhasil terinstal
                        preference.setVersiClient(versi);
                        //Ketika berhasil mendownload database buka menu activity
                        bukaMenuActivity();
                    }

                    @Override
                    public void onGagalDownload() {
                        //ketika gagal mendownload database
                        tampilkanAllertError("Error!", "Terjadi kesalahan saat mengunduh data dari server!");
                    }
                });
            }else{
                preference.getVersiServer(new Preference.ServerListener() {
                    @Override
                    public void onSuksesRequestVersiServer(int versiServer) {
                        if(preference.apakahPerluUpdate(versiServer)){
                            //tampilkan dialog ke user untuk menyetujui update, jika user menolak update -> MenuActivity
                            tampilkanAllertUpdate(versiServer);
                        }else{
                            //set waktu splash ke 2000
                            waktuSplah = 2000;
                            //versi client uptodate dengan server, -> MenuActivity
                            bukaMenuActivity();
                        }
                    }
                    @Override
                    public void onGagalRequestVersiServer() {
                        //terjadi kegagalan saat mendapatkan informasi versi dari server
                        bukaMenuActivity();
                    }
                });
            }
        }else if(preference.getVersiClient() == 0){
            //tampilkan pesan tidak dapat memproses, cobalagi -> exit
            tampilkanAllertError("Koneksi Bermasalah", "Aplikasi membutuhkan koneksi internet untuk mengunduh database untuk yang pertama kali, periksa koneksi internet anda dan coba lagi.");
        }else{
            //set waktu splash ke 2000
            waktuSplah = 2000;
            //client tak terhubung ke internet namun memiliki versi database sebelumnya, -> MenuActivity
            bukaMenuActivity();
        }
    }


    private void tampilkanAllertUpdate(int versiServer){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Kabar Baik");
        alertDialog.setMessage("Versi database terbaru tersedia di server, apakah anda ingin mengunduhnya?" +
                "\n\n" +
                "Versi saat ini: " + String.valueOf(preference.getVersiClient()) + "\n" +
                "Versi terbaru : " + String.valueOf(versiServer));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Lain Kali", (dialog, which) -> {
                    dialog.dismiss();
                    //buka MenuActivity
                    bukaMenuActivity();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ya, Perbarui", (dialog, which) -> {
                    dialog.dismiss();
                    //proses update
                    perbaruiDatabase();
                });
        alertDialog.show();
    }

    private void perbaruiDatabase() {
        //set waktu splash ke 0
        waktuSplah = 0;
        DatabaseManagement dm = new DatabaseManagement(this, ((AppCore)getApplication()).getRequestQueue());
        dm.updateDatabase(preference.getVersiClient(), new DatabaseDownloadListener() {
            @Override
            public void onSelesaiDownload(int versi) {
                //set versi client ke versi terbaru
                preference.setVersiClient(versi);
                Toast.makeText(SplashActivity.this, "Database berhasil diperbarui", Toast.LENGTH_SHORT).show();
                bukaMenuActivity();
            }

            @Override
            public void onGagalDownload() {
                //terjadi kegagalan saat update database
                Toast.makeText(SplashActivity.this, "Terjadi kegagalan saat update database", Toast.LENGTH_SHORT).show();
                bukaMenuActivity();
            }

        });
    }


    private void tampilkanAllertError(String judul, String isi){
        //deklarasi AlertDialof=g
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        //set judul alert dialog
        alertDialog.setTitle(judul);
        //set isi pesan alert dialog
        alertDialog.setMessage(isi);
        //atur intergace onClick untuk Button Positif
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Keluar", (dialog, which) -> {
                    //tutup alertdialog
                    dialog.dismiss();
                    //keluar dari activity ini (keluar dr aplikasi)
                    finish();
                });
        //atur interface onClick utk btn negatif
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Coba Lagi", (dialog, which) -> {
                    //tutup alertdialog
                    dialog.dismiss();
                    //mengecek ulang database
                    manajemenDatabaseUpdate();
                });
        //tampilkan alert dialog
        alertDialog.show();
    }

    private int waktuSplah = 2000;
    private void bukaMenuActivity(){
        CuacaDatabase cuacaDatabase = new CuacaDatabase(this, ((AppCore)getApplication()).getRequestQueue());
        cuacaDatabase.setListenerCuaca(new CuacaDatabase.ListenerCuaca() {
            @Override
            public void onSelesaiDownload(ModelLokasi lokasi) {
                Log.d(Constant.TAG, "Sukses dl cuaca lokasi " + lokasi.getNamaDaerah());
                startAktifitAS();
            }

            @Override
            public void onGagalDownload() {
                Log.d(Constant.TAG, "Gagal dl cuaca lokasi");
                startAktifitAS();
            }
        });
        if(ModelLokasi.apakahMasihKosong(new DatabaseHelper(this).getOpenDatabase())){
            if(appCore.getLokasiTerbaik()!=null) {
                cuacaDatabase.downloadCuaca(appCore.getLokasiTerbaik(), null);
            }else{
                Location l = new Location("l");
                l.setAltitude(altitude);
                l.setLongitude(longitude);
                l.setLatitude(latitude);
                cuacaDatabase.downloadCuaca(l, namaLokasi);
            }
        }else if(cuacaDatabase.apakahPerluUpdate() && !ModelLokasi.apakahMasihKosong(new DatabaseHelper(this).getOpenDatabase())){
            cuacaDatabase.updateCuaca();
        }else{
            new Handler().postDelayed(() -> {
                //start menu activity
               startAktifitAS();
            }, waktuSplah);
        }
    }

    private void startAktifitAS(){
        //intent untuk membuka MenuActivity
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
       //disable aksi onbackPressed pada saat splash berjalan
    }
}
