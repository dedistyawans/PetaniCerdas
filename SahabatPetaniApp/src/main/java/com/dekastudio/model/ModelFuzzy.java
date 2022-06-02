package com.dekastudio.model;


public class ModelFuzzy {
    private ModelTanaman tanaman;
    private double nilaiKetinggianTanah, nilaiSuhu, nilaiCurahHujan, nilaiUmurTerhadapMusim;
    private double fireStrength;

    public ModelTanaman getTanaman() {
        return tanaman;
    }

    public double getFireStrength() {
        return fireStrength;
    }

    public void setFireStrength(double fireStrength) {
        this.fireStrength = fireStrength;
    }

    public void setTanaman(ModelTanaman tanaman) {
        this.tanaman = tanaman;
    }

    public double getNilaiKetinggianTanah() {
        return nilaiKetinggianTanah;
    }

    public void setNilaiKetinggianTanah(double nilaiKetinggianTanah) {
        this.nilaiKetinggianTanah = nilaiKetinggianTanah;
    }

    public double getNilaiSuhu() {
        return nilaiSuhu;
    }

    public void setNilaiSuhu(double nilaiSuhu) {
        this.nilaiSuhu = nilaiSuhu;
    }

    public double getNilaiCurahHujan() {
        return nilaiCurahHujan;
    }

    public void setNilaiCurahHujan(double nilaiCurahHujan) {
        this.nilaiCurahHujan = nilaiCurahHujan;
    }

    public double getNilaiUmurTerhadapMusim() {
        return nilaiUmurTerhadapMusim;
    }

    public void setNilaiUmurTerhadapMusim(double nilaiUmurTerhadapMusim) {
        this.nilaiUmurTerhadapMusim = nilaiUmurTerhadapMusim;
    }
}
