package com.dekastudio.helper;


import android.annotation.SuppressLint;
import android.util.Log;

import com.dekastudio.database.DatabaseHelper;
import com.dekastudio.model.ModelCuaca;
import com.dekastudio.model.ModelFuzzy;
import com.dekastudio.model.ModelLokasi;
import com.dekastudio.model.ModelTanaman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HitungKonsultasi {

    private DatabaseHelper databaseHelper;
    private List<ModelCuaca> listCuaca;

    public HitungKonsultasi(DatabaseHelper databaseHelper, ModelLokasi lokasi) {
        this.databaseHelper = databaseHelper;
        setLokasi(lokasi);
    }

    public HitungKonsultasi(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void setLokasi(ModelLokasi lokasi) {
        listCuaca = ModelCuaca.bacaDataByLokasi(databaseHelper.getOpenDatabase(), lokasi);
    }


    public double getCurahHujan(){
        double jumlahCurah = 0d;
        for(ModelCuaca cuaca : listCuaca){
            jumlahCurah += cuaca.getCurahHujanSiang();
            jumlahCurah += cuaca.getCurahHujanMalam();
        }
        return jumlahCurah;
    }

    public double getSuhuRata2(){
        double suhuRata2 = 0d;
        for(ModelCuaca cuaca : listCuaca){
            suhuRata2 += cuaca.getSuhuSiang();
            suhuRata2 += cuaca.getSuhuMalam();
        }
        //menghitung rata2 jumlah nilai data dibagi jumlah hitung data
        return suhuRata2/(listCuaca.size()*2d);
    }

    public List<ModelFuzzy> dapatkanHasilKonsultasi(double suhuUser, double ketinggianTanahUser, int idTanahuser, double curahHujanUser){
        //menghitung menggunakan rumus fuzzy mamdani
        //rumus fuzzy dari http://ejurnal.provisi.ac.id/index.php/JTIKP/article/download/119/113/ diakses pada 6 Maret 2018
        /*
            0; x<=a || x>=c  -> jika(x<=a || x>=c) maka hasil = 0
            (x-a)/(b-a); x>=a && x<=b  -> jika(x>=a && x<=b) maka hasil = (x-a)/(b-a)
            (c-x)/(c-b); x>=b && x<=c  -> jika(x>=b && x<=c) maka hasil = (c-x)/(c-b)

            fireStrength = min(var1, var2, ...) -> menentukan nilai fire strength dg mengambil nilai terkecil dr perhitungan semua variabel

            x -> data dari user
            a -> batas bawah (nilai minimal) dari data
            b -> batas tengah (nilai tengah) dari data -> a+c/2
            c -> batas atas (nilai maksimum) dari data

            variabel fuzzy :
            1. Ketinggian tanah
            2. Curah Hujan
            3. Suhu rata2

            variabel non fuzzy :
            1. Musim
            2. Umur Tanaman
            3. Jenis Tanah

            menentukan nilai umur tanaman terhadap musim, penulis menggunakan motode perbandingan dg nilai 1/4 (4 dr jumlah variabel yg ada),
            maka didapatkan persamaan :
            hasil; (umur/lama musim) * (1/4)  -> (umur/lama musim) * (1/4) sama dengan hasil

            variabel2 yg akan dihitung :
            1. Ketinggian Tanah (fuzzy)
            2. Curah Hujan  (fuzzy)
            3. Suhu rata2   (fuzzy)
            4. Musim terhadap umur tanaman (non fuzzy, perbandingan)

            variabel2 berfungsi sbg filter:
            1. Jenis tanah (non fuzzy)
         */

        //filter data menggunakan jenis tanah
        List<ModelFuzzy> listFilterTanah = new ArrayList<>();
        for(ModelTanaman t : ModelTanaman.bacaDataByIdTanah(databaseHelper.getOpenDatabase(), idTanahuser)){
            ModelFuzzy mf = new ModelFuzzy();
            mf.setTanaman(t);
            listFilterTanah.add(mf);
        }

        for(int i =0; i<listFilterTanah.size(); i++){
            ModelTanaman mt = listFilterTanah.get(i).getTanaman();

            //hitung nilai ketinggian tanah dg fuzzy
            double bTinggiTanah = (mt.getKetinggianMin()+mt.getKetinggianMax()) / 2d;
            if(ketinggianTanahUser <= mt.getKetinggianMin() || ketinggianTanahUser >= mt.getKetinggianMax()){
                listFilterTanah.get(i).setNilaiKetinggianTanah(0d);
            }else if(ketinggianTanahUser >= mt.getKetinggianMin() && ketinggianTanahUser <= bTinggiTanah){
                double nilai = (ketinggianTanahUser-mt.getKetinggianMin()) / (bTinggiTanah - mt.getKetinggianMin());
                listFilterTanah.get(i).setNilaiKetinggianTanah(nilai);
            }else if(ketinggianTanahUser >= bTinggiTanah && ketinggianTanahUser <= mt.getKetinggianMax()){
                double nilai = (mt.getKetinggianMax()-ketinggianTanahUser) / (mt.getKetinggianMax()-bTinggiTanah);
                listFilterTanah.get(i).setNilaiKetinggianTanah(nilai);
            }
            Log.d("ihir", String.valueOf(listFilterTanah.get(i).getNilaiKetinggianTanah()));

            //hitung nilai curah hujan dg fuzzy
            double bCurahHujan = (mt.getCurahHujanMin()+mt.getCurahHujanMax()) / 2d;
            if(curahHujanUser <= mt.getCurahHujanMin() || curahHujanUser >= mt.getCurahHujanMax()){
                listFilterTanah.get(i).setNilaiCurahHujan(0d);
            }else if(curahHujanUser >= mt.getCurahHujanMin() && curahHujanUser <= bCurahHujan){
                double nilai = (curahHujanUser-mt.getCurahHujanMin()) / (bCurahHujan-mt.getCurahHujanMin());
                listFilterTanah.get(i).setNilaiCurahHujan(nilai);
            }else if(curahHujanUser >= bCurahHujan && curahHujanUser <= mt.getCurahHujanMax()){
                double nilai = (mt.getCurahHujanMax()-curahHujanUser) / (mt.getCurahHujanMax()-bCurahHujan);
                listFilterTanah.get(i).setNilaiCurahHujan(nilai);
            }
            Log.d("ihir", String.valueOf(listFilterTanah.get(i).getNilaiCurahHujan()));


            //hitung nilai suhu dg fuzzy
            double bSuhu = (mt.getSuhuMin()+mt.getSuhuMax()) / 2d;
            if(suhuUser <= mt.getSuhuMin() || suhuUser >= mt.getSuhuMax()){
                listFilterTanah.get(i).setNilaiSuhu(0d);
            }else if(suhuUser >= mt.getSuhuMin() && suhuUser <= bSuhu){
                double nilai = (suhuUser-mt.getSuhuMin()) / (bSuhu-mt.getSuhuMin());
                listFilterTanah.get(i).setNilaiSuhu(nilai);
            }else if(suhuUser >= bSuhu && suhuUser <= mt.getSuhuMax()){
                double nilai = (mt.getSuhuMax()-suhuUser) / (mt.getSuhuMax()-bSuhu);
                listFilterTanah.get(i).setNilaiSuhu(nilai);
            }
            Log.d("ihir", String.valueOf(listFilterTanah.get(i).getNilaiSuhu()));


            //hitung fire strength-> nilai terendah dr variabel fuzzy
            //karena nilai fuzzy tdk akan lebih dr 1
            double fireStrength = 1d;
            if(listFilterTanah.get(i).getNilaiKetinggianTanah() <= fireStrength) {
                fireStrength = listFilterTanah.get(i).getNilaiKetinggianTanah();
            }
            if(listFilterTanah.get(i).getNilaiCurahHujan() <= fireStrength) {
                fireStrength = listFilterTanah.get(i).getNilaiCurahHujan();
            }
            if(listFilterTanah.get(i).getNilaiSuhu() <= fireStrength) {
                fireStrength = listFilterTanah.get(i).getNilaiSuhu();
            }
            listFilterTanah.get(i).setFireStrength(fireStrength);

            //hitung umur thd musim dg perbandingan
            double lamaMusim = getSisaHariMusim(mt.getMusim(), mt.getUmur());
            double nilai = (lamaMusim / mt.getUmur()) * (1d / 4d);
            listFilterTanah.get(i).setNilaiUmurTerhadapMusim(nilai);
            Log.d("ihir", String.valueOf(listFilterTanah.get(i).getNilaiUmurTerhadapMusim()));

            //jika min firestrength lebih besar dari 0 maka tambahkan perbandingan umur thd musim
            if(listFilterTanah.get(i).getFireStrength()>0d) {
                //tambahkan hasil umur thd perbandingan ke fire strength
                double finalFireStrength = listFilterTanah.get(i).getFireStrength() + listFilterTanah.get(i).getNilaiUmurTerhadapMusim();
                if (finalFireStrength >= 1d) {
                    finalFireStrength = 1d;
                }
                listFilterTanah.get(i).setFireStrength(finalFireStrength);
            }
        }

        //filter dan buang data yg nilai firestrengnya 0, 0.01 adalah 1%
        List<ModelFuzzy> listFilterFireStrength = new ArrayList<>();
        for(ModelFuzzy f : listFilterTanah){
            if(f.getFireStrength()>=0.01d){
                listFilterFireStrength.add(f);
            }
        }
        return listFilterFireStrength;
    }

    @SuppressLint("SimpleDateFormat")
    private int getSisaHariMusim(String musim, int umur){
        //Musim kemarau april - september bersamaan dg angin musim timur https://id.wikipedia.org/wiki/Musim diakses 5 maret 2018
        //musim hujan oktober - maret bersamaan angin musim barat https://id.wikipedia.org/wiki/Musim diakses 5 maret 2018
        int[] bulanKemarau = {3, 4, 5, 6, 7, 8};
        int[] bulanHujan = {9, 10, 11, 0, 1, 2};
        int sisaHari = 0;
        Date sekarang = new Date();
        Calendar c = Calendar.getInstance();
        if(musim.equals("Musim Kemarau")){
            //hitung sisa musim kemarau
            for(int i =0; i<umur; i++){
                c.setTime(sekarang);
                if(i!=0) {
                    c.add(Calendar.DATE, 1);
                }
                sekarang = c.getTime();
                int bulan = c.get(Calendar.MONTH);
                for(int b : bulanKemarau){
                    if(bulan == b){
                        sisaHari++;
                        //hentikan perulangan ini
                        break;
                    }
                }
            }
        }else{
            //hitung sisa musim hujan
            for(int i =0; i<umur; i++){
                c.setTime(sekarang);
                if(i!=0) {
                    c.add(Calendar.DATE, 1);
                }
                sekarang = c.getTime();
                int bulan = c.get(Calendar.MONTH);
                for(int b : bulanHujan){
                    if(bulan == b){
                        sisaHari++;
                        //hentikan perulangan ini
                        break;
                    }
                }
            }
        }
        return sisaHari;
    }

}
