package com.dekastudio.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dekastudio.customview.AppCompactImageTextSlider;
import com.dekastudio.helper.Constant;
import com.dekastudio.sahabatpetanicerdas.R;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;

public class MenuActivity extends AppCompatActivity {

    SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //inisialisasi slider layout
        sliderLayout = findViewById(R.id.slider);
        int[] listSlide = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3};
        String[] listDescription = {"Prakiraan cuaca hingga 90 hari", "Wiki pertanian untuk edukasi", "Konsultasikan tanaman yang akan ditanam"};
        for (int i = 0; i < 3; i++) {
            AppCompactImageTextSlider defaultSliderView = new AppCompactImageTextSlider(this);
            defaultSliderView.image(listSlide[i]);
            defaultSliderView.description(listDescription[i]);
            sliderLayout.addSlider(defaultSliderView);
        }
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        //set klik listener dengan lamda (java 8 support lamda statement/expression)
        findViewById(R.id.btnKonsultasi).setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, KonsultasiActivity.class)));

        //set klik listener dengan lamda (java 8 support lamda statement/expression)
        findViewById(R.id.btnWiki).setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, WikiActivity.class)));

        //set klik listener dengan lamda (java 8 support lamda statement/expression)
        findViewById(R.id.btnBookmark).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, WikiActivity.class);
            intent.putExtra(Constant.INDEX_CODE, true);
            startActivity(intent);
        });

        //set klik listener dengan lamda (java 8 support lamda statement/expression)
        findViewById(R.id.btnPrakiraan).setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CuacaActivity.class)));
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate xml menu untuk activity ini
        getMenuInflater().inflate(R.menu.menu, menu);
        //sesuai dokumentasi Android Menu, return true jika ingin menu ini ditampilkan
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


    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Konfirmasi");
        alertDialog.setMessage("Apakah anda ingin menutup aplikasi ini?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya",
                (dialog, which) -> finish());
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
