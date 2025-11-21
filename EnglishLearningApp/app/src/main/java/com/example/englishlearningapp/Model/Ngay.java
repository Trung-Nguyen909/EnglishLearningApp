package com.example.englishlearningapp.Model;

public class Ngay {
    private String ngay;
    private String trangThai;

    public Ngay(String ngay, String trangThai) {
        this.ngay = ngay;
        this.trangThai = trangThai;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}