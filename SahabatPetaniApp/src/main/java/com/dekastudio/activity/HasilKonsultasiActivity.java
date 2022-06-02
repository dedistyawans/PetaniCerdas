package com.dekastudio.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.dekastudio.adapter.HasilKonsultasiAdapter;
import com.dekastudio.core.AppCore;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.HitungKonsultasi;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.model.ModelFuzzy;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.Collections;
import java.util.List;

public class HasilKonsultasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_konsultasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int idTanah = getIntent().getIntExtra(Constant.ID_TANAH_CODE, -1);
        double tinggi = getIntent().getDoubleExtra(Constant.TINGGI_TANAH_CODE, -1d);
        double suhu = getIntent().getDoubleExtra(Constant.SUHU_CODE, -1d);
        double curah = getIntent().getDoubleExtra(Constant.CURAH_HUJAN_CODE, -1d);

        HitungKonsultasi hk = new HitungKonsultasi(new DatabaseHelper(this));
        //urutkan berdasarkan firestrength tertinggi
        List<ModelFuzzy> listData = hk.dapatkanHasilKonsultasi(suhu, tinggi, idTanah, curah);
        Collections.sort(listData, (o1, o2) -> {
            int o1Fire = (int) (o1.getFireStrength()*1000d);
            int o2Fire = (int) (o2.getFireStrength()*1000d);
            return o2Fire-o1Fire;
        });


        HasilKonsultasiAdapter hka = new HasilKonsultasiAdapter(listData, this, ((AppCore) getApplication()).getRequestQueue());
        RecyclerView recyclerView = findViewById(R.id.recycleHasilKonsultasi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new JarakRecycleView(10, this));
        recyclerView.setAdapter(hka);

        SearchView searchView = findViewById(R.id.search);
        searchView.setQueryHint("Cari tanaman...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hka.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hka.filter(newText);
                return false;
            }
        });
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
