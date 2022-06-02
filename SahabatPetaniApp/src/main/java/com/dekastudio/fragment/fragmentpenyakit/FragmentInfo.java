package com.dekastudio.fragment.fragmentpenyakit;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.dekastudio.activity.DetailPenyakit;
import com.dekastudio.sahabatpetanicerdas.R;

public class FragmentInfo extends Fragment {
    private Activity activity;
    private WebView webViewInfoPenyakit;
    private TextView nama, tanaman;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.info_penyakit_fragment, container, false);

        activity = getActivity();
        init(root);
        bacaDatabase();

        return root;
    }

    private void init(View v){
        webViewInfoPenyakit = v.findViewById(R.id.webviewDeskripsiPenyakit);
        nama = v.findViewById(R.id.fpTxtNama);
        tanaman = v.findViewById(R.id.fpTxtTanaman);
    }

    @SuppressLint("SetTextI18n")
    private void bacaDatabase(){
        DetailPenyakit dp = (DetailPenyakit) activity;
        nama.setText("Nama : " + dp.getModelPenyakit().getNama());
        tanaman.setText("Tanaman : " + dp.getModelTanaman().getNama());
        String deskripsi = dp.getModelPenyakit().getDeskripsi();
        webViewInfoPenyakit.loadData(deskripsi, "text/html; charset=utf-8", "UTF-8");
    }
}
