package com.dekastudio.fragment.fragmentpenyakit;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dekastudio.activity.DetailPenyakit;
import com.dekastudio.sahabatpetanicerdas.R;

public class FragmentMenangani extends Fragment {
    private Activity activity;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.webview, container, false);

        activity = getActivity();
        init(root);
        bacaDatabase();

        return root;
    }

    private void init(View v){
        webView = v.findViewById(R.id.webview);
    }

    private void bacaDatabase(){
        DetailPenyakit dp = (DetailPenyakit) activity;
        String caraMenangani = dp.getModelPenyakit().getCaraMenangani();
        webView.loadData(caraMenangani, "text/html; charset=utf-8", "UTF-8");
    }
}
