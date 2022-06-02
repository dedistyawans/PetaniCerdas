package com.dekastudio.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.dekastudio.activity.KonsultasiActivity;
import com.dekastudio.customview.SquareRelative;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.ImageHelper;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.HolderView>{

    private List<ModelTanah> listTanah;
    private Context context;
    private RequestQueue requestQueue;
    private DatabaseHelper databaseHelper;
    private int posisiAktif = -1;
    private int posisiSebelumnya = -1;

    public GridAdapter(Context context, DatabaseHelper databaseHelper, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
        this.databaseHelper = databaseHelper;
        listTanah = databaseHelper.bacaSemuaData(new ModelTanah());
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new GridAdapter.HolderView(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        holder.penGrid.setOnClickListener(v -> {
            KonsultasiActivity ka = (KonsultasiActivity) context;
            ka.gridKlik(listTanah.get(position).getId());
        });
        holder.txtGrid.setText(listTanah.get(position).getNama());
        List<ModelFotoTanah> gambarTanah = ModelFotoTanah.bacaFotoTanah(databaseHelper.getOpenDatabase(), listTanah.get(position).getId());
        Glide.with(context)
                .load(ImageHelper.getUrl(requestQueue, Constant.TANAH_FOLDER, gambarTanah.get(0).getNamaFile()))
                .into(holder.imgGrid);

        if (posisiAktif!=-1 && position==posisiAktif) {
            holder.checkGrid.setVisibility(View.VISIBLE);
        } else {
            holder.checkGrid.setVisibility(View.GONE);
        }

    }

    public void centangItem(int idTanah){
        posisiAktif = getIndexByIdTanah(idTanah);
        if(posisiSebelumnya!=-1){
            notifyItemChanged(posisiSebelumnya);
        }
        notifyItemChanged(posisiAktif);
        posisiSebelumnya = posisiAktif;
    }

    private int getIndexByIdTanah(int idTanah){
        for(int i = 0; i<listTanah.size(); i++){
            if(idTanah == listTanah.get(i).getId()) {
                return i;
            }
        }
        //return -1 jika index tidak ditemukan
        return -1;
    }

    @Override
    public int getItemCount() {
        return listTanah.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private SquareRelative penGrid;
        private TextView txtGrid;
        private ImageView imgGrid;
        private LinearLayout checkGrid;

        public HolderView(View itemView) {
            super(itemView);
            penGrid = itemView.findViewById(R.id.penGrid);
            txtGrid = itemView.findViewById(R.id.txtGrid);
            imgGrid = itemView.findViewById(R.id.imgGrid);
            checkGrid = itemView.findViewById(R.id.checkGrid);
        }
    }

}
