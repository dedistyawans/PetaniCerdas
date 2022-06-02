package com.dekastudio.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.dekastudio.adapter.CuacaAdapter;
import com.dekastudio.adapter.SpinnerAdapter;
import com.dekastudio.core.AppCore;
import com.dekastudio.customview.DekaDialog;
import com.dekastudio.database.CuacaDatabase;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.model.ModelCuaca;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.sahabatpetanicerdas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class CuacaActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private CuacaAdapter cuacaAdapter;
    private AppCore appCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prakiraan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appCore = (AppCore) getApplication();
        if(!appCore.apakahGpsOn()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            //set judul alert dialog
            alertDialog.setTitle("Gps tidak aktif");
            //set isi pesan alert dialog
            alertDialog.setMessage("Untuk akurasi yang lebih baik, harap hidupkan GPS anda. Informasi GPS dibutuhkan untuk mendapatkan koordinat, " +
                    "ketinggian tanah, dan prakiraan cuaca di lokasi anda");
            //atur intergace onClick untuk Button Positif
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Abaikan", (dialog, which) -> {
                //tutup alertdialog
                dialog.dismiss();
            });
            //atur interface onClick utk btn negatif
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hidupkan GPS", (dialog, which) -> {
                //tutup alertdialog
                dialog.dismiss();
                //intent untuk mengarahkan ke pengaturan lokasi Android
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });
            //tampilkan alert dialog
            alertDialog.show();
        }

        databaseHelper = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        cuacaAdapter = new CuacaAdapter(this, databaseHelper);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new JarakRecycleView(10, this));
        recyclerView.setAdapter(cuacaAdapter);
        cuacaAdapter.notifyDataSetChanged();

        initView();
    }


    private TextView textLokasi, textSuhu, textDetail, textAngin, textTanggal, textLamaHujan, textSiangMalam, textKetinggianTanah;
    private ImageView imgSiangMalam;
    private Switch switchSiangMalam;
    private Spinner spinnerLokasi;
    private ConstraintLayout bungkusAtas;


    int spinnerPos = 1;
    double latitude, longitude, altitude = 0d;
    List<String> daftarNamaDaerah;
    SpinnerAdapter spinnerAdapter;
    private void initView(){
        textLokasi = findViewById(R.id.textLokasi);
        textSuhu = findViewById(R.id.textSuhu);
        textDetail = findViewById(R.id.textDetail);
        textAngin = findViewById(R.id.textAngin);
        textTanggal = findViewById(R.id.textTanggal);
        textLamaHujan = findViewById(R.id.textLamaHujan);
        textSiangMalam = findViewById(R.id.textSiangMalam);
        imgSiangMalam = findViewById(R.id.imgSiangMalam);
        switchSiangMalam = findViewById(R.id.switchSiangMalam);
        spinnerLokasi = findViewById(R.id.spinnerLokasi);
        bungkusAtas = findViewById(R.id.bungkusAtas);
        textKetinggianTanah = findViewById(R.id.textKetinggianTanah);

        daftarNamaDaerah = new ArrayList<>();
        daftarNamaDaerah.add("Tambah lokasi baru..");
        for(ModelLokasi lokasi : databaseHelper.bacaSemuaData(new ModelLokasi())){
            daftarNamaDaerah.add(lokasi.getNamaDaerah());
        }


        spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1, daftarNamaDaerah);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setSelection(1);
        cuacaAdapter.setLokasiAktif(databaseHelper.bacaSemuaData(new ModelLokasi()).get(0));
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    if(apakahTerkoneksiKeInternet()){
                        //aksi tambahkan lokasi disini
                        latitude = 0d;
                        longitude = 0d;
                        altitude = 0d;

                        DekaDialog dekaDialog = new DekaDialog(CuacaActivity.this, R.layout.dialog_add_lokasi);
                        dekaDialog.setBatalPadaSisiLuar(false);
                        dekaDialog.setDapatDiBatal(false);

                        Button btnSimpan = dekaDialog.getView().findViewById(R.id.dialogBtnSimpan);
                        Button btnBatal = dekaDialog.getView().findViewById(R.id.dialogBtnBatal);

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
                                btnBatal.setVisibility(View.GONE);
                                btnSimpan.setVisibility(View.GONE);
                                txtUnduhCuaca.setVisibility(View.VISIBLE);
                                progressSimpan.setVisibility(View.VISIBLE);
                                CuacaDatabase cd = new CuacaDatabase(CuacaActivity.this, ((AppCore) CuacaActivity.this.getApplication()).getRequestQueue());
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
                                        bacaUlangDaftarLokasi();
                                    }

                                    @Override
                                    public void onGagalDownload() {
                                        dekaDialog.tutupDialog();
                                        Toast.makeText(appCore, "Lokasi gagal disimpan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(CuacaActivity.this, "Nama lokasi tak boleh kosong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnBatal.setOnClickListener(v ->{
                            appCore.hapusListenerTerbaik();
                            dekaDialog.tutupDialog();
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
                                Toast.makeText(CuacaActivity.this, "Hidupkan GPS untuk menggunakan fitur ini!", Toast.LENGTH_LONG).show();
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
                                                        Toast.makeText(CuacaActivity.this, "Ketinggian tanah tak diketahui!", Toast.LENGTH_LONG).show();
                                                        Log.d(Constant.TAG, "Elevation Fail ->" + "status tidak OK");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    btnCari.setVisibility(View.VISIBLE);
                                                    progressCari.setVisibility(View.GONE);
                                                    radioBtnAddress.setEnabled(true);
                                                    radioBtnGps.setEnabled(true);
                                                    Toast.makeText(CuacaActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                                                    Log.d(Constant.TAG, "Elevation Fail ->" + e.toString());
                                                }

                                            }, error -> {
                                                btnCari.setVisibility(View.VISIBLE);
                                                progressCari.setVisibility(View.GONE);
                                                radioBtnAddress.setEnabled(true);
                                                radioBtnGps.setEnabled(true);
                                                Toast.makeText(CuacaActivity.this, "Error : "+error.toString(), Toast.LENGTH_LONG).show();
                                                Log.d(Constant.TAG, "Elevation Fail ->" + error.toString());
                                            });
                                            appCore.getRequestQueue().add(jorl);
                                        }else{
                                            btnCari.setVisibility(View.VISIBLE);
                                            progressCari.setVisibility(View.GONE);
                                            radioBtnAddress.setEnabled(true);
                                            radioBtnGps.setEnabled(true);
                                            Toast.makeText(CuacaActivity.this, "Lokasi tak ditemukan!", Toast.LENGTH_LONG).show();
                                            Log.d(Constant.TAG, "Geo Fail ->" + "status tidak OK");
                                        }
                                    } catch (JSONException e) {
                                        btnCari.setVisibility(View.VISIBLE);
                                        progressCari.setVisibility(View.GONE);
                                        radioBtnAddress.setEnabled(true);
                                        radioBtnGps.setEnabled(true);
                                        e.printStackTrace();
                                        Toast.makeText(CuacaActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                                        Log.d(Constant.TAG, "Geo Fail ->" + e.toString());
                                    }
                                }, error -> {
                                    btnCari.setVisibility(View.VISIBLE);
                                    progressCari.setVisibility(View.GONE);
                                    radioBtnAddress.setEnabled(true);
                                    radioBtnGps.setEnabled(true);
                                    Toast.makeText(CuacaActivity.this, "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                                    Log.d(Constant.TAG, "Geo Fail ->" + error.toString());
                                });
                                appCore.getRequestQueue().add(jor);
                            }else{
                                Toast.makeText(CuacaActivity.this, "Pencarian tak boleh kosong!", Toast.LENGTH_LONG).show();
                            }
                        });
                        dekaDialog.tampilkanDialog();
                    }else{
                        Toast.makeText(CuacaActivity.this, "Diperlukan koneksi internet!", Toast.LENGTH_LONG).show();
                    }
                    spinnerLokasi.setSelection(spinnerPos);
                }else{
                    spinnerPos = position;
                    cuacaAdapter.setLokasiAktif(databaseHelper.bacaSemuaData(new ModelLokasi()).get(position-1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerLokasi.setSelection(spinnerPos);
            }
        });

        //listener
        switchSiangMalam.setOnCheckedChangeListener((buttonView, isChecked) -> ubahDetail(cuaca, tanggal, lokasi));
    }

    private void bacaUlangDaftarLokasi(){
        daftarNamaDaerah = new ArrayList<>();
        daftarNamaDaerah.add("Tambah lokasi baru..");
        for(ModelLokasi lokasi : databaseHelper.bacaSemuaData(new ModelLokasi())){
            daftarNamaDaerah.add(lokasi.getNamaDaerah());
        }
        spinnerAdapter.setDaftarDaerah(daftarNamaDaerah);
        spinnerPos = databaseHelper.bacaSemuaData(new ModelLokasi()).size();
        spinnerLokasi.setSelection(spinnerPos);
    }

    private boolean apakahTerkoneksiKeInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private ModelCuaca cuaca;
    private String tanggal;
    private ModelLokasi lokasi;

    @SuppressLint("SetTextI18n")
    public void ubahDetail(ModelCuaca cuaca, String tanggal, ModelLokasi lokasi){
        //simpan sementara
        this.cuaca = cuaca;
        this.tanggal = tanggal;
        this.lokasi = lokasi;

        textLokasi.setText(lokasi.getNamaDaerah());
        textTanggal.setText(tanggal);
        textKetinggianTanah.setText(new DecimalFormat(".#").format(lokasi.getAltitude()) + " MDPL");
        //jika switch ke check artinya malam
        if(switchSiangMalam.isChecked()) {
            textSuhu.setText(cuaca.getSuhuMalam() + "°");
            textDetail.setText(cuaca.getDetailMalam());
            textAngin.setText(cuaca.getAnginMalam());
            if(cuaca.getLamaHujanMalam()>0f) {
                textLamaHujan.setText("Hujan " + cuaca.getLamaHujanMalam() + " Jam" +" ("+ cuaca.getCurahHujanMalam() +" mm)");
                bungkusAtas.setBackgroundResource(R.drawable.bg_hujan);
            }else {
                textLamaHujan.setText("Cerah");
                bungkusAtas.setBackgroundResource(R.drawable.bg_cerah);
            }
            textSiangMalam.setText("Malam");
            imgSiangMalam.setImageResource(R.drawable.ic_malam);
        }else{
            textSuhu.setText(cuaca.getSuhuSiang() + "°");
            textDetail.setText(cuaca.getDetailSiang());
            textAngin.setText(cuaca.getAnginSiang());
            if(cuaca.getLamaHujanSiang()>0f) {
                textLamaHujan.setText("Hujan " + cuaca.getLamaHujanSiang() + " Jam" +" ("+ cuaca.getCurahHujanSiang() +" mm)");
                bungkusAtas.setBackgroundResource(R.drawable.bg_hujan);
            }else {
                textLamaHujan.setText("Cerah");
                bungkusAtas.setBackgroundResource(R.drawable.bg_cerah);
            }
            textSiangMalam.setText("Siang");
            imgSiangMalam.setImageResource(R.drawable.ic_siang);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mendapatkan id dari item menu yang di klik oleh user
        int id = item.getItemId();
        //percabangan switch untuk variabel id
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.about:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Tentang");
                alertDialog.setMessage("Developed By : Dedi Styawan\nNIM : 14.11.7933\nUniversitas Amikom YK");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
                //return true supaya fungsi hanya di eksekusi di scope ini / sesuai dokumentasi Android Menu
                return true;
            case R.id.rate:
                try {
                    Intent intentrate = new Intent(Intent.ACTION_VIEW);
                    intentrate.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                    startActivity(intentrate);
                }catch (Exception e){
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName()));
                    startActivity(i);
                }
                //return true supaya fungsi hanya di eksekusi di scope ini / sesuai dokumentasi Android Menu
                return true;
        }
        //tidak ada dari id menu diatas yang di eksekusi, return superMethod dari class ini
        return super.onOptionsItemSelected(item);
    }
}
