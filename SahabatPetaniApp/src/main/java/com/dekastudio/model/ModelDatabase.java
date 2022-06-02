package com.dekastudio.model;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dekastudio.helper.Constant;

import java.util.List;

public abstract class ModelDatabase <T> {

    public abstract ContentValues getContentValues();
    public abstract String getNamaTabel();
    public abstract String getKolomPrimary();
    public abstract int getId();
    public abstract List<T> bacaSemuaData(SQLiteDatabase dbWritable);
    public abstract T bacaDataDenganId(SQLiteDatabase dbWritable);

    public int insertData(SQLiteDatabase dbWritable){
        int id = (int) dbWritable.insert(getNamaTabel(), null, getContentValues());
        if(id!=-1) {
            Log.d(Constant.TAG, "sukses insert data->" + getNamaTabel());
        }else {
            Log.d(Constant.TAG, "gagal insert data->" + getNamaTabel());
        }
        dbWritable.close();
        return id;
    }

    public void updateData(SQLiteDatabase dbWritable, int id){
        if(dbWritable.update(getNamaTabel(), getContentValues(), getKolomPrimary()+"="+String.valueOf(id), null)>0){
            Log.d(Constant.TAG, "sukses update data->"+getNamaTabel());
        }else{
            Log.d(Constant.TAG, "gagal update data->"+getNamaTabel());
        }
        dbWritable.close();
    }

    public void deleteData(SQLiteDatabase dbWritable){
        if(dbWritable.delete(getNamaTabel(), getKolomPrimary()+"="+String.valueOf(getId()), null)>0){
            Log.d(Constant.TAG, "sukses delete data->"+getNamaTabel());
        }else{
            Log.d(Constant.TAG, "gagal delete data->"+getNamaTabel());
        }
        dbWritable.close();
    }

    public void deleteSemuaData(SQLiteDatabase dbWritable){
        if(dbWritable.delete(getNamaTabel(), null, null)>0){
            Log.d(Constant.TAG, "sukses delete semua data->"+getNamaTabel());
        }else{
            Log.d(Constant.TAG, "gagal delete semua data->"+getNamaTabel());
        }
        dbWritable.close();
    }
}
