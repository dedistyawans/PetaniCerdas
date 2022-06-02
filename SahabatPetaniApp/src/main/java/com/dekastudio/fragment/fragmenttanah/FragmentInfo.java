package com.dekastudio.fragment.fragmenttanah;


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

import com.dekastudio.activity.DetailTanah;
import com.dekastudio.activity.DetailTanaman;
import com.dekastudio.adapter.SimpelAdapter;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.model.ModelSimpel;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentInfo extends Fragment implements SimpelAdapter.AksiSimpel{
    private Activity activity;
    private TextView nama;
    private RecyclerView daftarTanaman;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.info_tanah_fragment, container, false);

        activity = getActivity();
        init(root);
        bacaDatabase();

        return root;
    }

    private void init(View v){
        nama = v.findViewById(R.id.fpTxtNama);
        daftarTanaman = v.findViewById(R.id.ftRecycleTanaman);
    }

    @SuppressLint("SetTextI18n")
    private void bacaDatabase(){
        DetailTanah dt = (DetailTanah) activity;
        nama.setText("Nama : " + dt.getModelTanah().getNama());

        List<ModelSimpel> listTanaman = new ArrayList<>();
        for(ModelTanaman p : dt.getListTanaman()){
            listTanaman.add(new ModelSimpel(p.getId(), p.getNama()));
        }
        SimpelAdapter simpelAdapterPenyakit = new SimpelAdapter(this, listTanaman, SimpelAdapter.Tipe.TANAMAN);
        daftarTanaman.setLayoutManager(new LinearLayoutManager(activity));
        daftarTanaman.setItemAnimator(new DefaultItemAnimator());
        daftarTanaman.addItemDecoration(new JarakRecycleView(20, activity));
        daftarTanaman.setAdapter(simpelAdapterPenyakit);

    }

    @Override
    public void bukaDetail(int id, SimpelAdapter.Tipe tipe) {
        Intent intent = new Intent(getActivity(), DetailTanaman.class);
        intent.putExtra(Constant.INDEX_CODE, id);
        activity.startActivity(intent);
    }
}
