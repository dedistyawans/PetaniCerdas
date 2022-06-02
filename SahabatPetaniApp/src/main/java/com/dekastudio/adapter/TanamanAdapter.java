package com.dekastudio.adapter;


import android.annotation.SuppressLint;
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
import com.dekastudio.activity.WikiActivity;
import com.dekastudio.customview.CircleImageView;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.ArrayList;
import java.util.List;

public class TanamanAdapter extends RecyclerView.Adapter<TanamanAdapter.HolderView>{

    private Context context;
    private List<ModelTanaman> listData;
    private DatabaseHelper databaseHelper;
    private RequestQueue requestQueue;
    private boolean apakahBookmark;

    public TanamanAdapter(Context context, boolean apakahBookmark, RequestQueue requestQueue, View pesanKosong) {
        this.context = context;
        this.pesanKosong = pesanKosong;
        databaseHelper = new DatabaseHelper(context);
        this.apakahBookmark = apakahBookmark;
        if(this.apakahBookmark){
            this.listData = ModelTanaman.bacaDataBookmark(databaseHelper.getOpenDatabase());
            tampilkanPesanKosong();
        }else{
            this.listData = databaseHelper.bacaSemuaData(new ModelTanaman());
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

    public void notifyDataBerubah(int idTanaman){
        int index = getIndexByIdTanaman(idTanaman);
        if(index!=-1) {
            notifyItemRemoved(index);
            listData.remove(index);
            notifyItemRangeChanged(index, listData.size() - index);
        }
        tampilkanPesanKosong();
    }

    private int getIndexByIdTanaman(int idTanaman){
        for(int i = 0; i<listData.size(); i++){
            if(listData.get(i).getId() == idTanaman)
                return i;
        }
        return -1;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tanaman, parent, false);
        return new HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        //handle klik listener ketika penampung list di klik
        holder.penampungList.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTanaman.class);
            intent.putExtra(Constant.INDEX_CODE, listData.get(position).getId());
            ((WikiActivity)context).startResult(intent, WikiActivity.Pengirim.TANAMAN);
        });

        List<ModelFotoTanaman> listFotoTanaman = ModelFotoTanaman.bacaFotoTanaman(databaseHelper.getOpenDatabase(), listData.get(position).getId());

        //atur gambar tanaman dengan glide library
        Glide.with(context)
                .load(ImageHelper.getUrl(requestQueue, Constant.TANAMAN_FOLDER, listFotoTanaman.get(0).getNamaFile()))
                .into(holder.imageTanaman);

        holder.judulTanaman.setText(listData.get(position).getNama());
        holder.umurTanaman.setText(String.valueOf(listData.get(position).getUmur()) + " Hari");
        if(listData.get(position).getMusim().equals("Musim Hujan")){
            holder.imageCuaca.setImageResource(R.drawable.ic_hujan);
        }else{
            holder.imageCuaca.setImageResource(R.drawable.ic_kemarau);
        }
    }


    public void filter(String query){
        listData = new ArrayList<>();
        if(query.equals("")){
            if(this.apakahBookmark){
                this.listData = ModelTanaman.bacaDataBookmark(databaseHelper.getOpenDatabase());
            }else{
                this.listData = databaseHelper.bacaSemuaData(new ModelTanaman());
            }
        }else {
            if(this.apakahBookmark){
                for (ModelTanaman modelTanaman : ModelTanaman.bacaDataBookmark(databaseHelper.getOpenDatabase())) {
                    if (modelTanaman.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelTanaman);
                    }
                }
            }else{
                for (ModelTanaman modelTanaman : databaseHelper.bacaSemuaData(new ModelTanaman())) {
                    if (modelTanaman.getNama().toLowerCase().contains(query.toLowerCase())) {
                        listData.add(modelTanaman);
                    }
                }
            }

        }
        notifyDataSetChanged();
        tampilkanPesanKosong();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penampungList;
        private TextView judulTanaman, umurTanaman;
        private CircleImageView imageCuaca;
        private ImageView imageTanaman;

        public HolderView(View itemView) {
            super(itemView);
            penampungList = itemView.findViewById(R.id.penTanaman);
            judulTanaman = itemView.findViewById(R.id.txtNamaTanaman);
            umurTanaman = itemView.findViewById(R.id.txtUmur);
            imageCuaca = itemView.findViewById(R.id.imageCuacaTanaman);
            imageTanaman = itemView.findViewById(R.id.imgTanaman);
        }
    }

}
