package com.example.englishlearningapp.Model;

public class BaiHocModel {
    private String loaiBaiHoc;
    private String tieuDe;
    private String capDo;
    private String thoiGian;
    private int mauSacLoai;

    public BaiHocModel(String loaiBaiHoc, String tieuDe, String capDo, String thoiGian, int mauSacLoai) {
        this.loaiBaiHoc = loaiBaiHoc;
        this.tieuDe = tieuDe;
        this.capDo = capDo;
        this.thoiGian = thoiGian;
        this.mauSacLoai = mauSacLoai;
    }

    // Getters
    public String getLoaiBaiHoc() {
        return loaiBaiHoc;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getCapDo() {
        return capDo;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public int getMauSacLoai() {
        return mauSacLoai;
    }
}