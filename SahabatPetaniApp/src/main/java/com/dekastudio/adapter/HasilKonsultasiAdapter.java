package com.dekastudio.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.dekastudio.activity.DetailTanaman;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelFuzzy;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class HasilKonsultasiAdapter extends RecyclerView.Adapter<HasilKonsultasiAdapter.HolderView>{
    private List<ModelFuzzy> listTemp;
    private List<ModelFuzzy> listData;
    private Context context;
    private RequestQueue requestQueue;
    private DatabaseHelper databaseHelper;

    public HasilKonsultasiAdapter(List<ModelFuzzy> listData, Context context, RequestQueue requestQueue) {
        this.listTemp = listData;
        this.listData = listData;
        this.context = context;
        this.requestQueue = requestQueue;
        this.databaseHelper = new DatabaseHelper(context);
        tampilkanPesanKosong("Rekomandasi Tidak Tersedia!");
    }

    private TextView pesanKosong;
    private void tampilkanPesanKosong(String pesan){
        if(pesanKosong == null){
            pesanKosong = ((Activity) context).findViewById(R.id.txtKosongHasilKonsultasi);
        }
        if(listData.size()==0) {
            pesanKosong.setVisibility(View.VISIBLE);
            pesanKosong.setText(pesan);
        }else{
            pesanKosong.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hasil_konsultasi, parent, false);
        return new HasilKonsultasiAdapter.HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        holder.penampungList.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTanaman.class);
            intent.putExtra(Constant.INDEX_CODE, listData.get(position).getTanaman().getId());
            context.startActivity(intent);
        });

        int persen = (int) (listData.get(position).getFireStrength()*100d);
        holder.persen.setText(String.valueOf(persen) + "%");

        holder.judulTanaman.setText(listData.get(position).getTanaman().getNama());

        holder.musimTanam.setText(listData.get(position).getTanaman().getMusim());

        List<ModelFotoTanaman> listFotoTanaman = ModelFotoTanaman.bacaFotoTanaman(databaseHelper.getOpenDatabase(), listData.get(position).getTanaman().getId());
        Glide.with(context)
                .load(ImageHelper.getUrl(requestQueue, Constant.TANAMAN_FOLDER, listFotoTanaman.get(0).getNamaFile()))
                .into(holder.imageTanaman);
    }

    public void filter(String query){
        listData = new ArrayList<>();
        if(query.equals("")){
            listData = listTemp;
        }else {
            for (ModelFuzzy fuzzy : listTemp) {
                if (fuzzy.getTanaman().getNama().toLowerCase().contains(query.toLowerCase()) || fuzzy.getTanaman().getMusim().toLowerCase().contains(query.toLowerCase())) {
                    listData.add(fuzzy);
                }
            }
        }
        notifyDataSetChanged();
        tampilkanPesanKosong("Pencarian Tidak Ditemukan!");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penampungList;
        private TextView judulTanaman, musimTanam, persen;
        private ImageView imageTanaman;

        public HolderView(View itemView) {
            super(itemView);
            penampungList = itemView.findViewById(R.id.penTanaman);
            judulTanaman = itemView.findViewById(R.id.txtNamaTanaman);
            musimTanam = itemView.findViewById(R.id.txtMusim);
            persen = itemView.findViewById(R.id.txtPersen);
            imageTanaman = itemView.findViewById(R.id.imgTanaman);
        }
    }

}
