package com.dekastudio.adapter;


import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dekastudio.model.ModelSimpel;
import com.dekastudio.sahabatpetanicerdas.R;

import java.util.List;

public class SimpelAdapter extends RecyclerView.Adapter<SimpelAdapter.HolderView>{

    private AksiSimpel aksiSimpel;
    private List<ModelSimpel> listSimpel;
    private Tipe tipe;

    public SimpelAdapter(AksiSimpel aksiSimpel, List<ModelSimpel> listSimpel, Tipe tipe) {
        this.aksiSimpel = aksiSimpel;
        this.listSimpel = listSimpel;
        this.tipe = tipe;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simpel, parent, false);
        return new SimpelAdapter.HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.penSimpel.setOnClickListener(v -> aksiSimpel.bukaDetail(listSimpel.get(position).getId(), tipe));
        holder.txtNama.setText(String.valueOf(position+1) + ". " + listSimpel.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return listSimpel.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private ConstraintLayout penSimpel;
        private TextView txtNama;

        public HolderView(View itemView) {
            super(itemView);
            penSimpel = itemView.findViewById(R.id.penSimpel);
            txtNama = itemView.findViewById(R.id.isTxtNama);
        }
    }


    public interface AksiSimpel{
        void bukaDetail(int id, Tipe tipe);
    }

    public enum Tipe{
        TANAMAN, TANAH, PENYAKIT
    }
}
