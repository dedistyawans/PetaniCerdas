package com.dekastudio.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.dekastudio.core.AppCore;
import com.dekastudio.customview.AppCompactImageSlider;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.fragment.fragmenttanaman.FragmentDetail;
import com.dekastudio.fragment.fragmenttanaman.FragmentInfo;
import com.dekastudio.fragment.fragmenttanaman.FragmentMenanam;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.sahabatpetanicerdas.R;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;

import java.util.List;

public class DetailTanaman extends AppCompatActivity {

    private SliderLayout sliderLayout;
    private ModelTanaman modelTanaman;
    private List<ModelPenyakit> listPenyakit;
    private List<ModelTanah> listTanah;
    private int idTanaman;
    private boolean unBookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tanaman);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //inisialisasi databasehelper utk membaca data dari database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        //mendapatkan nilai int yg dikirim oleh activity sebelumnya
        idTanaman = getIntent().getIntExtra(Constant.INDEX_CODE, -1);
        modelTanaman = databaseHelper.bacaDataDenganId(new ModelTanaman(idTanaman));

        //mendapatkan list penyakit utk tanaman ini
        listPenyakit = ModelPenyakit.bacaPenyakitByIdTanaman(databaseHelper.getOpenDatabase(), idTanaman);

        //mendapatkan list tanah utk tanaman ini
        listTanah = ModelTanah.bacaTanahByIdTanaman(databaseHelper.getOpenDatabase(), idTanaman);

        //area tab layout
        ViewPager viewPager = findViewById(R.id.viewpager);
        DetailTanamanTabAdapter detailTanamanTabAdapter = new DetailTanamanTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(detailTanamanTabAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        //inisialisasi slider image
        sliderLayout = findViewById(R.id.slider);
        for (ModelFotoTanaman fotoTanaman: ModelFotoTanaman.bacaFotoTanaman(databaseHelper.getOpenDatabase(), idTanaman)) {
            AppCompactImageSlider defaultSliderView = new AppCompactImageSlider(this);
            defaultSliderView.image(ImageHelper.getUrl(((AppCore)getApplication()).getRequestQueue(), Constant.TANAMAN_FOLDER, fotoTanaman.getNamaFile()));
            sliderLayout.addSlider(defaultSliderView);
        }
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Top);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        ImageView bookmark = findViewById(R.id.btnBookmarkTanaman);
        if(modelTanaman.getBookmark() == 1){
            bookmark.setImageResource(R.drawable.ic_bookmark_on);
        }

        bookmark.setOnClickListener(v -> {
            if(modelTanaman.getBookmark() == 1){
                //hapus bookmark
                modelTanaman.setBookmark(0);
                databaseHelper.updateData(modelTanaman, modelTanaman.getId());
                bookmark.setImageResource(R.drawable.ic_bookmark_off);
                Toast.makeText(this, "Dihapus dari daftar bookmark", Toast.LENGTH_SHORT).show();
                unBookmark = true;
            }else{
                //tambahkan ke bookmark
                modelTanaman.setBookmark(1);
                databaseHelper.updateData(modelTanaman, modelTanaman.getId());
                bookmark.setImageResource(R.drawable.ic_bookmark_on);
                Toast.makeText(this, "Ditambahkan ke bookmark", Toast.LENGTH_SHORT).show();
                unBookmark = false;
            }
        });
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(Constant.INDEX_CODE, idTanaman);
        if(unBookmark) {
            setResult(RESULT_OK, data);
        }else{
            setResult(RESULT_CANCELED, data);
        }
        finish();
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

    public ModelTanaman getDataTanaman(){
        return modelTanaman;
    }

    public List<ModelPenyakit> getDataPenyakit(){
        return listPenyakit;
    }

    public List<ModelTanah> getDataTanah(){
        return listTanah;
    }

    class DetailTanamanTabAdapter extends FragmentPagerAdapter {

        private final Fragment[] daftarFragment = {new FragmentInfo(), new FragmentDetail(), new FragmentMenanam()};
        private final String[] daftarJudulTab = {"Info", "Detail", "Menanam"};

        DetailTanamanTabAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return daftarFragment[position];
        }

        @Override
        public int getCount() {
            return daftarFragment.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return daftarJudulTab[position];
        }
    }
}
