package com.example.englishlearningapp.Model;

public class ChuDePhuModel {
    private int maChuDePhu;
    private String tenChuDePhu;
    private int maIcon;

    public ChuDePhuModel(int maChuDePhu, String tenChuDePhu, int maIcon) {
        this.maChuDePhu = maChuDePhu;
        this.tenChuDePhu = tenChuDePhu;
        this.maIcon = maIcon;
    }

    public int getMaChuDePhu() {
        return maChuDePhu;
    }

    public String getTenChuDePhu() {
        return tenChuDePhu;
    }

    public int getMaIcon() {
        return maIcon;
    }
}
