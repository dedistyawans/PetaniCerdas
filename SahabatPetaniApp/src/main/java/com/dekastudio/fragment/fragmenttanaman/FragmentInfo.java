package com.dekastudio.fragment.fragmenttanaman;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dekastudio.activity.DetailPenyakit;
import com.dekastudio.activity.DetailTanah;
import com.dekastudio.activity.DetailTanaman;
import com.dekastudio.adapter.SimpelAdapter;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelSimpel;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentInfo extends Fragment implements SimpelAdapter.AksiSimpel{
    private TextView nama, umur, musim, ketinggianTanah, curahHujan, suhu;
    private RecyclerView penyakit, tanah;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.info_tanaman_fragment, container, false);

        activity = getActivity();
        init(root);
        bacaDatabase();

        return root;
    }

    private void init(View v){
        nama = v.findViewById(R.id.fpTxtNama);
        umur = v.findViewById(R.id.fdtTxtUmur);
        musim = v.findViewById(R.id.fdtTxtMusim);
        ketinggianTanah = v.findViewById(R.id.fdtTxtKetinggian);
        curahHujan = v.findViewById(R.id.fdtTxtCurahHujan);
        suhu = v.findViewById(R.id.fdtTxtSuhu);
        penyakit = v.findViewById(R.id.fdtRecyclePenyakit);
        tanah = v.findViewById(R.id.fdtRecycleTanah);
    }

    @SuppressLint("SetTextI18n")
    private void bacaDatabase(){
        DetailTanaman dt = (DetailTanaman) activity;
        ModelTanaman t = dt.getDataTanaman();

        nama.setText("Nama : " + t.getNama());
        umur.setText("Umur : " + String.valueOf(t.getUmur()) + " Hari");
        musim.setText("Musim : " + t.getMusim());
        ketinggianTanah.setText("Ketinggian Tanah : " + String.valueOf(t.getKetinggianMin()) + " M - " + String.valueOf(t.getKetinggianMax()) + " M");
        curahHujan.setText("Curah Hujan : " + String.valueOf(t.getCurahHujanMin()) + " mm - " + String.valueOf(t.getCurahHujanMax()) + " mm");
        suhu.setText("Suhu : " + String.valueOf(t.getSuhuMin()) + " °C - " + String.valueOf(t.getSuhuMax()) + " °C");

        List<ModelSimpel> listPenyakit = new ArrayList<>();
        for(ModelPenyakit p : dt.getDataPenyakit()){
            listPenyakit.add(new ModelSimpel(p.getId(), p.getNama()));
        }
        SimpelAdapter simpelAdapterPenyakit = new SimpelAdapter(this, listPenyakit, SimpelAdapter.Tipe.PENYAKIT);
        penyakit.setLayoutManager(new LinearLayoutManager(activity));
        penyakit.setItemAnimator(new DefaultItemAnimator());
        penyakit.addItemDecoration(new JarakRecycleView(20, activity));
        penyakit.setAdapter(simpelAdapterPenyakit);

        List<ModelSimpel> listTanah = new ArrayList<>();
        for(ModelTanah p : dt.getDataTanah()){
            listTanah.add(new ModelSimpel(p.getId(), p.getNama()));
        }
        SimpelAdapter simpelAdapterTanah = new SimpelAdapter(this, listTanah, SimpelAdapter.Tipe.TANAH);
        tanah.setLayoutManager(new LinearLayoutManager(activity));
        tanah.setItemAnimator(new DefaultItemAnimator());
        tanah.addItemDecoration(new JarakRecycleView(20, activity));
        tanah.setAdapter(simpelAdapterTanah);

    }

    @Override
    public void bukaDetail(int id, SimpelAdapter.Tipe tipe) {
        Intent intent;
        if(tipe == SimpelAdapter.Tipe.PENYAKIT){
            intent = new Intent(getActivity(), DetailPenyakit.class);
        }else{
            intent = new Intent(getActivity(), DetailTanah.class);
        }
        intent.putExtra(Constant.INDEX_CODE, id);
        activity.startActivity(intent);
    }
}
