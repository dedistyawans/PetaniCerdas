package com.dekastudio.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.dekastudio.activity.DetailPenyakit;
import com.dekastudio.activity.WikiActivity;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoPenyakit;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.HolderView>{
    private Context context;
    private List<ModelPenyakit> listData;
    private DatabaseHelper databaseHelper;
    private RequestQueue requestQueue;
    private boolean apakahBookmark;

    public PenyakitAdapter(Context context, boolean apakahBookmark, RequestQueue requestQueue, View pesanKosong) {
        this.context = context;
        this.pesanKosong = pesanKosong;
        databaseHelper = new DatabaseHelper(context);
        this.apakahBookmark = apakahBookmark;
        if(this.apakahBookmark){
            this.listData = ModelPenyakit.bacaDataBookmark(databaseHelper.getOpenDatabase());
            tampilkanPesanKosong();
        }else{
            this.listData = databaseHelper.bacaSemuaData(new ModelPenyakit());
        }
        this.requestQueue = requestQueue;
    }

    private View pesanKosong;
    private void tampilkanPesanKosong(){
        if(listData.size()==0) {
            pesanKosong.setVisibility(View.VISIBLE);
        }else{
            pesanKosong.setVisibility(View.GONE);
        }
    }

    public void notifyDataBerubah(int idPenyakit){
        int index = getIndexByIdPenyakit(idPenyakit);
        if(index!=-1) {
            notifyItemRemoved(index);
            listData.remove(index);
            notifyItemRangeChanged(index, listData.size() - index);
        }
        tampilkanPesanKosong();
    }

    private int getIndexByIdPenyakit(int idPenyakit){
        for(int i = 0; i<listData.size(); i++){
            if(listData.get(i).getId() == idPenyakit)
                return i;
        }
        return -1;
    }


    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penyakit, parent, false);
        return new HolderView(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.penampungList.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailPenyakit.class);
            intent.putExtra(Constant.INDEX_CODE, listData.get(position).getId());
            ((WikiActivity)context).startResult(intent, WikiActivity.Pengirim.PENYAKIT);
        });
        holder.judulPenyakit.setText(listData.get(position).getNama());

        ModelTanaman modelTanaman = databaseHelper.bacaDataDenganId(new ModelTanaman(listData.get(position).getIdTanaman()));
        holder.namaTanaman.setText(modelTanaman.getNama());

        if(modelTanaman.getMusim().equals("Musim Hujan")){
            holder.imageCuaca.setImageResource(R.drawable.ic_hujan);
        }else{
            holder.imageCuaca.setImageResource(R.drawable.ic_kemarau);
        }

        List<ModelFotoPenyakit> gambarPenyakit = ModelFotoPenyakit.bacaFotoPenyakit(databaseHelper.getOpenDatabase(), listData.get(position).getId());
        Glide.with(context)
                .load(ImageHelper.getUrl(requestQueue, Constant.PENYAKIT_FOLDER, gambarPenyakit.get(0).getNamaFile()))
                .into(holder.imagePenyakit);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void filter(String query){
        listData = new ArrayList<>();
        if(query.equals("")){
            if(this.apakahBookmark){
                this.listData = ModelPenyakit.bacaDataBookmark(databaseHelper.getOpenDatabase());
            }else{
                this.listData = databaseHelper.bacaSemuaData(new ModelPenyakit());
            }
        }else {
            if(this.apakahBookmark){
                for (ModelPenyakit modelPenyakit : ModelPenyakit.bacaDataBookmark(databaseHelper.getOpenDatabase())) {
                    if (modelPenyakit.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelPenyakit);
                    }
                }
            }else{
                for (ModelPenyakit modelPenyakit : databaseHelper.bacaSemuaData(new ModelPenyakit())) {
                    if (modelPenyakit.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelPenyakit);
                    }
                }
            }
        }
        notifyDataSetChanged();
        tampilkanPesanKosong();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penampungList;
        private TextView judulPenyakit, namaTanaman;
        private ImageView imagePenyakit, imageCuaca;

        public HolderView(View itemView) {
            super(itemView);
            penampungList = itemView.findViewById(R.id.penPenyakit);
            judulPenyakit = itemView.findViewById(R.id.txtTanggal);
            namaTanaman = itemView.findViewById(R.id.txtTanaman);
            imagePenyakit = itemView.findViewById(R.id.imagePenyakit);
            imageCuaca = itemView.findViewById(R.id.imageCuaca);
        }
    }
}
