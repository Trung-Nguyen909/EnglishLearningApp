package com.example.englishlearningapp.Model;

public class NgayModel {
    private String ngay;
    private String trangThai;

    public NgayModel(String ngay, String trangThai) {
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