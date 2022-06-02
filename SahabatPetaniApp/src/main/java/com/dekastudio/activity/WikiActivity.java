package com.dekastudio.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dekastudio.fragment.fragmentwiki.FragmentWikiPenyakit;
import com.dekastudio.fragment.fragmentwiki.FragmentWikiTanah;
import com.dekastudio.fragment.fragmentwiki.FragmentWikiTanaman;
import com.dekastudio.helper.Constant;
import com.dekastudio.sahabatpetanicerdas.R;

public class WikiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    private boolean apakahBookmark = false;
    private FragmentWikiTanaman wikiTanaman;
    private FragmentWikiTanah wikiTanah;
    private FragmentWikiPenyakit wikiPenyakit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apakahBookmark = getIntent().getBooleanExtra(Constant.INDEX_CODE, false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //inisialisasi fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.INDEX_CODE, apakahBookmark);
        wikiTanaman = new FragmentWikiTanaman();
        wikiTanaman.setArguments(bundle);
        wikiTanah = new FragmentWikiTanah();
        wikiTanah.setArguments(bundle);
        wikiPenyakit = new FragmentWikiPenyakit();
        wikiPenyakit.setArguments(bundle);

        //secara deffault fragment index 0 diaktifkan
        ubahFragment(0);
    }

    private int pengirimTanah = 1, pengirimTanaman = 2, pengirimPenyakit = 3;
    public void startResult(Intent intent, Pengirim pengirim){
        if(!apakahBookmark){
            startActivity(intent);
        }else{
            int kodeRequest = pengirimTanah;
            switch (pengirim){
                case TANAMAN:
                    kodeRequest = pengirimTanaman;
                    break;
                case PENYAKIT:
                    kodeRequest = pengirimPenyakit;
                    break;
            }
            startActivityForResult(intent, kodeRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == pengirimTanah) {
                //bagian tanah
                wikiTanah.notifyDataBerubah(data.getIntExtra(Constant.INDEX_CODE, -1));
            } else if (requestCode == pengirimTanaman) {
                // bagian tanaman
                wikiTanaman.notifyDataBerubah(data.getIntExtra(Constant.INDEX_CODE, -1));
            } else if (requestCode == pengirimPenyakit) {
                //bagian penyakit
                wikiPenyakit.notifyDataBerubah(data.getIntExtra(Constant.INDEX_CODE, -1));
            }
        }
    }

    public enum Pengirim{
        TANAH, TANAMAN, PENYAKIT
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tanaman) {
           ubahFragment(0);
        } else if (id == R.id.nav_tanah) {
            ubahFragment(1);
        } else if (id == R.id.nav_penyakit) {
            ubahFragment(2);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void ubahFragment(int index){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(index==0){
            fragmentTransaction.replace(R.id.frameFragment, wikiTanaman, null);
        }else if(index == 1){
            fragmentTransaction.replace(R.id.frameFragment, wikiTanah, null);
        }else{
            fragmentTransaction.replace(R.id.frameFragment, wikiPenyakit, null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        //set index menu yang aktif
        navigationView.getMenu().getItem(index).setChecked(true);
        //ubah judul actionbar sesuai dengan posisi fragment yang aktif
        String[] judulActionBar = {"Wiki Tanaman", "Wiki Tanah", "Wiki Hama"};
        String[] judulActionBarBookmark = {"Bookmark Tanaman", "Bookmark Tanah", "Bookmark Hama"};
        if(apakahBookmark){
            getSupportActionBar().setTitle(judulActionBarBookmark[index]);
        }else{
            getSupportActionBar().setTitle(judulActionBar[index]);
        }
    }
}
