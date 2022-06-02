package com.dekastudio.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private List<String> daftarDaerah;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.daftarDaerah = objects;
    }

    public void setDaftarDaerah(List<String> daftarDaerah){
        this.daftarDaerah = daftarDaerah;
        notifyDataSetChanged();
    }


    @Nullable
    @Override
    public String getItem(int position) {
        return daftarDaerah.get(position);
    }

    @Override
    public int getCount() {
        return daftarDaerah.size();
    }
}
