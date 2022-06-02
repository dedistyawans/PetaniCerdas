package com.dekastudio.adapter;


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
import com.dekastudio.activity.DetailTanah;
import com.dekastudio.activity.WikiActivity;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class TanahAdapter extends RecyclerView.Adapter<TanahAdapter.HolderView>{
    private Context context;
    private List<ModelTanah> listData;
    private DatabaseHelper databaseHelper;
    private RequestQueue requestQueue;
    private boolean apakahBookmark;

    public TanahAdapter(Context context, boolean apakahBookmark, RequestQueue requestQueue, View pesanKosong) {
        this.context = context;
        this.pesanKosong = pesanKosong;
        databaseHelper = new DatabaseHelper(context);
        this.apakahBookmark = apakahBookmark;
        if(this.apakahBookmark){
            this.listData = ModelTanah.bacaDataBookmark(databaseHelper.getOpenDatabase());
            tampilkanPesanKosong();
        }else{
            this.listData = databaseHelper.bacaSemuaData(new ModelTanah());
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

    public void notifyDataBerubah(int idTanah){
        int index = getIndexByIdTanah(idTanah);
        if(index!=-1) {
            notifyItemRemoved(index);
            listData.remove(index);
            notifyItemRangeChanged(index, listData.size() - index);
        }
        tampilkanPesanKosong();
    }
    private int getIndexByIdTanah(int idTanah){
        for(int i = 0; i<listData.size(); i++){
            if(listData.get(i).getId() == idTanah)
                return i;
        }
        return -1;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tanah, parent, false);
        return new HolderView(item);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.penampungList.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTanah.class);
            intent.putExtra(Constant.INDEX_CODE, listData.get(position).getId());
            ((WikiActivity)context).startResult(intent, WikiActivity.Pengirim.TANAH);
        });
        holder.judulTanah.setText(listData.get(position).getNama());

        List<ModelFotoTanah> gambarTanah = ModelFotoTanah.bacaFotoTanah(databaseHelper.getOpenDatabase(), listData.get(position).getId());
        Glide.with(context)
                .load(ImageHelper.getUrl(requestQueue, Constant.TANAH_FOLDER, gambarTanah.get(0).getNamaFile()))
                .into(holder.imageTanah);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void filter(String query){
        listData = new ArrayList<>();
        if(query.equals("")){
            if(this.apakahBookmark){
                this.listData = ModelTanah.bacaDataBookmark(databaseHelper.getOpenDatabase());
            }else{
                this.listData = databaseHelper.bacaSemuaData(new ModelTanah());
            }
        }else {
            if(this.apakahBookmark){
                for (ModelTanah modelTanah : ModelTanah.bacaDataBookmark(databaseHelper.getOpenDatabase())){
                    if (modelTanah.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelTanah);
                    }
                }
            }else{
                for (ModelTanah modelTanah : databaseHelper.bacaSemuaData(new ModelTanah())) {
                    if (modelTanah.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelTanah);
                    }
                }
            }

        }
        notifyDataSetChanged();
        tampilkanPesanKosong();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penampungList;
        private TextView judulTanah;
        private ImageView imageTanah;

        public HolderView(View itemView) {
            super(itemView);
            penampungList = itemView.findViewById(R.id.penTanah);
            judulTanah = itemView.findViewById(R.id.txtTanggal);
            imageTanah = itemView.findViewById(R.id.imgCuaca);
        }
    }

}
