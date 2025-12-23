package com.example.englishlearningapp.Model;

public class BaiHocModel {
    private int id;
    private String loaiBaiHoc;
    private String tieuDe;
    private String capDo;
    private String thoiGian;
    private int mauSacLoai;

    public BaiHocModel(int id, String loaiBaiHoc, String tieuDe, String capDo, String thoiGian, int mauSacLoai) {
        this.id = id;
        this.loaiBaiHoc = loaiBaiHoc;
        this.tieuDe = tieuDe;
        this.capDo = capDo;
        this.thoiGian = thoiGian;
        this.mauSacLoai = mauSacLoai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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