package com.dekastudio.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dekastudio.activity.CuacaActivity;
import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.model.ModelCuaca;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.sahabatpetanicerdas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CuacaAdapter extends RecyclerView.Adapter<CuacaAdapter.HolderView>{

    private Context context;
    private List<ModelCuacaColor> listCuaacaAktifColor = new ArrayList<>();
    private ModelLokasi lokasiAktif;
    private DatabaseHelper databaseHelper;

    class ModelCuacaColor{
        private boolean terpilih;
        private ModelCuaca modelCuaca;

        public ModelCuacaColor(boolean terpilih, ModelCuaca modelCuaca) {
            this.terpilih = terpilih;
            this.modelCuaca = modelCuaca;
        }

        public boolean isTerpilih() {
            return terpilih;
        }

        public ModelCuaca getModelCuaca() {
            return modelCuaca;
        }

        public void setTerpilih(boolean terpilih) {
            this.terpilih = terpilih;
        }
    }

    public CuacaAdapter(Context context, DatabaseHelper databaseHelper) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        if(!ModelLokasi.apakahMasihKosong(databaseHelper.getOpenDatabase())){
            lokasiAktif = databaseHelper.bacaSemuaData(new ModelLokasi()).get(0);
            setLokasiAktif(lokasiAktif);
        }

    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuaca, parent, false);
        return new HolderView(item);
    }

    private int warnaDasar = Color.parseColor("#ffffff");
    private int warnaAktif = Color.parseColor("#e0e0e0");
    private int posisiSebelumnya;
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(HolderView holder, @SuppressLint("RecyclerView") int position) {
        Date date = new Date(listCuaacaAktifColor.get(position).getModelCuaca().getUnixTime()*1000L);
        holder.tanggal.setText(konversiKeTanggalIndo(new SimpleDateFormat("dd-MM-yyyy").format(date)));
        holder.txtLamaHujan.setText(String.valueOf(listCuaacaAktifColor.get(position).getModelCuaca().getLamaHujanSiang()+listCuaacaAktifColor.get(position).getModelCuaca().getLamaHujanMalam()) + " J");
        if(listCuaacaAktifColor.get(position).getModelCuaca().getLamaHujanSiang()>0f){
            holder.imageCuaca.setImageResource(R.drawable.ic_hujan);
            holder.detail.setText(listCuaacaAktifColor.get(position).getModelCuaca().getDetailSiang());
        }else if(listCuaacaAktifColor.get(position).getModelCuaca().getLamaHujanMalam()>0f){
            holder.imageCuaca.setImageResource(R.drawable.ic_hujan);
            holder.detail.setText(listCuaacaAktifColor.get(position).getModelCuaca().getDetailMalam());
        }else{
            holder.imageCuaca.setImageResource(R.drawable.ic_kemarau);
            holder.detail.setText(listCuaacaAktifColor.get(position).getModelCuaca().getDetailSiang());
        }
        CuacaActivity cuacaActivity = (CuacaActivity) context;
        if(listCuaacaAktifColor.get(position).isTerpilih()){
            posisiSebelumnya = position;
            holder.cardPenampungCuaca.setCardBackgroundColor(warnaAktif);
            if(!ModelLokasi.apakahMasihKosong(databaseHelper.getOpenDatabase())){
                cuacaActivity.ubahDetail(listCuaacaAktifColor.get(position).getModelCuaca(), holder.tanggal.getText().toString(), lokasiAktif);
            }
        }else{
            holder.cardPenampungCuaca.setCardBackgroundColor(warnaDasar);
        }


        holder.penampungList.setOnClickListener(v -> {
            listCuaacaAktifColor.get(posisiSebelumnya).setTerpilih(false);
            listCuaacaAktifColor.get(position).setTerpilih(true);
            notifyItemChanged(posisiSebelumnya);
            notifyItemChanged(position);
            posisiSebelumnya = position;
            if(!ModelLokasi.apakahMasihKosong(databaseHelper.getOpenDatabase())) {
                cuacaActivity.ubahDetail(listCuaacaAktifColor.get(position).getModelCuaca(), holder.tanggal.getText().toString(), lokasiAktif);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void setLokasiAktif(ModelLokasi lokasi){
        this.lokasiAktif = lokasi;
        listCuaacaAktifColor = new ArrayList<>();
        List<ModelCuaca> daftarCuacaTemp = ModelCuaca.bacaDataByLokasi(databaseHelper.getOpenDatabase(), lokasiAktif);
        for(int i = 0; i<daftarCuacaTemp.size(); i++){
            if(i==0) {
                listCuaacaAktifColor.add(new ModelCuacaColor(true, daftarCuacaTemp.get(i)));
            } else {
                listCuaacaAktifColor.add(new ModelCuacaColor(false, daftarCuacaTemp.get(i)));
            }
        }
        notifyDataSetChanged();
    }

    private String konversiKeTanggalIndo(String tanggal){
        String[] arrayTanggal = tanggal.split("-");
        String namaBulan = "null";
        switch (arrayTanggal[1].toLowerCase()){
            case "01" : namaBulan = "Januari"; break;
            case "02" : namaBulan = "Februari"; break;
            case "03" : namaBulan = "Maret"; break;
            case "04" : namaBulan = "April"; break;
            case "05" : namaBulan = "Mei"; break;
            case "06" : namaBulan = "Juni"; break;
            case "07" : namaBulan = "Juli"; break;
            case "08" : namaBulan = "Agustus"; break;
            case "09" : namaBulan = "September"; break;
            case "10" : namaBulan = "Oktober"; break;
            case "11" : namaBulan = "November"; break;
            case "12" : namaBulan = "Desember"; break;
        }
        return String.valueOf(Integer.parseInt(arrayTanggal[0])) + " " + namaBulan +" "+ arrayTanggal[2];
    }

    @Override
    public int getItemCount() {
        return listCuaacaAktifColor.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penampungList;
        private TextView tanggal, detail, txtLamaHujan;
        private ImageView imageCuaca;
        private CardView cardPenampungCuaca;

        public HolderView(View itemView) {
            super(itemView);
            penampungList = itemView.findViewById(R.id.penCuaca);
            tanggal = itemView.findViewById(R.id.txtTanggal);
            detail = itemView.findViewById(R.id.txtDetail);
            imageCuaca = itemView.findViewById(R.id.imgCuaca);
            txtLamaHujan = itemView.findViewById(R.id.txtLamaHujan);
            cardPenampungCuaca = itemView.findViewById(R.id.cardPenambungCuaca);
        }
    }
}
