package com.dekastudio.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dekastudio.adapter.GridAdapter;
import com.dekastudio.core.AppCore;
import com.dekastudio.customview.DekaDialog;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.HitungKonsultasi;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.sahabatpetanicerdas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KonsultasiActivity extends AppCompatActivity {

    private TextView nama, koordinat, ketinggian, suhu, curahHujan;
    private DatabaseHelper databaseHelper;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        nama = findViewById(R.id.konNamaLokasi);
        koordinat = findViewById(R.id.konKoordinat);
        ketinggian = findViewById(R.id.konKetinggianTanah);
        suhu = findViewById(R.id.konSuhu);
        curahHujan = findViewById(R.id.konCurahHujan);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Spinner spinnerLokasi = findViewById(R.id.konSpinner);
        List<ModelLokasi> listLokasiModel = databaseHelper.bacaSemuaData(new ModelLokasi());


        List<String> listLokasiString = new ArrayList<>();
        for(ModelLokasi lokasi : databaseHelper.bacaSemuaData(new ModelLokasi())){
            listLokasiString.add(lokasi.getNamaDaerah());
        }

        HitungKonsultasi hk = new HitungKonsultasi(databaseHelper, listLokasiModel.get(0));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listLokasiString);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setSelection(0);
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hk.setLokasi(listLokasiModel.get(position));
                ubahDetailLokasi(hk, listLokasiModel.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gridAdapter = new GridAdapter(this, databaseHelper, ((AppCore)getApplication()).getRequestQueue());
        RecyclerView recyclerView = findViewById(R.id.konRecycle);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new JarakRecycleView(10, this));
        recyclerView.setAdapter(gridAdapter);

        findViewById(R.id.konButton).setOnClickListener(v -> {
            if(idTanahTerpilih==-1){
                Toast.makeText(this, "Harap pilih jenis tanah", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(KonsultasiActivity.this, HasilKonsultasiActivity.class);
                intent.putExtra(Constant.TINGGI_TANAH_CODE, tinggiTanah);
                intent.putExtra(Constant.CURAH_HUJAN_CODE, curahRata);
                intent.putExtra(Constant.SUHU_CODE, suhuRata);
                intent.putExtra(Constant.ID_TANAH_CODE, idTanahTerpilih);
                startActivity(intent);
            }
        });
    }

    //set default aktif index 0
    int pos = 0;
    public void gridKlik(int idTanah){
        pos = 0;
        List<ModelFotoTanah> gambarTanah = ModelFotoTanah.bacaFotoTanah(databaseHelper.getOpenDatabase(), idTanah);
        DekaDialog dekaDialog = new DekaDialog(this, R.layout.dialog_pilih_tanah);
        dekaDialog.setBatalPadaSisiLuar(false);
        dekaDialog.setDapatDiBatal(false);

        ImageView prev = dekaDialog.getView().findViewById(R.id.gridDialogBtnPrev);
        ImageView next = dekaDialog.getView().findViewById(R.id.gridDIalogBtnNext);
        ImageView img = dekaDialog.getView().findViewById(R.id.gridDialogImageTanah);
        Button btl = dekaDialog.getView().findViewById(R.id.gridDialogBtnBatal);
        Button plh = dekaDialog.getView().findViewById(R.id.gridDialogBtnPilih);

        Glide.with(this)
                .load(ImageHelper.getUrl(((AppCore)getApplication()).getRequestQueue(), Constant.TANAH_FOLDER, gambarTanah.get(pos).getNamaFile()))
                .into(img);

        prev.setOnClickListener(v -> {
            if(pos>0){
                pos--;
            }else{
                pos = gambarTanah.size()-1;
            }
            Glide.with(this)
                    .load(ImageHelper.getUrl(((AppCore)getApplication()).getRequestQueue(), Constant.TANAH_FOLDER, gambarTanah.get(pos).getNamaFile()))
                    .into(img);
        });

        next.setOnClickListener(v -> {
            if(pos<gambarTanah.size()-1){
                pos++;
            }else{
                pos = 0;
            }
            Glide.with(this)
                    .load(ImageHelper.getUrl(((AppCore)getApplication()).getRequestQueue(), Constant.TANAH_FOLDER, gambarTanah.get(pos).getNamaFile()))
                    .into(img);
        });

        btl.setOnClickListener(v -> dekaDialog.tutupDialog());
        plh.setOnClickListener(v -> {
            gridAdapter.centangItem(idTanah);
            idTanahTerpilih = idTanah;
            dekaDialog.tutupDialog();
        });

        dekaDialog.tampilkanDialog();
    }

    //variabel2 penampung
    int idTanahTerpilih = -1;
    private double tinggiTanah;
    private double suhuRata;
    private double curahRata;
    @SuppressLint("SetTextI18n")
    private void ubahDetailLokasi(HitungKonsultasi hk, ModelLokasi lok){
        //simpan nilai
        tinggiTanah = lok.getAltitude();
        suhuRata = hk.getSuhuRata2();
        curahRata = hk.getCurahHujan();

        //ubah GUI
        nama.setText("Nama : " + lok.getNamaDaerah());
        koordinat.setText("Koordinat : " + String.valueOf(lok.getLatitude()) + "," + String.valueOf(lok.getLongitude()));
        ketinggian.setText("Ketinggian Tanah : " + String.valueOf(new DecimalFormat(".##").format(tinggiTanah)) + " MDPL");
        suhu.setText("Suhu rata-rata : " + String.valueOf(new DecimalFormat(".##").format(suhuRata)) + "°C");
        curahHujan.setText("Curah Hujan (90 hari) : " + String.valueOf(new DecimalFormat(".##").format(curahRata)) + " mm");
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
