package com.example.englishlearningapp.Model;

public class KyNangModel {
    private String tenKyNang;
    private int maHinhAnh;

    public KyNangModel(String tenKyNang, int maHinhAnh) {
        this.tenKyNang = tenKyNang;
        this.maHinhAnh = maHinhAnh;
    }

    public String getTenKyNang() {
        return tenKyNang;
    }

    public int getMaHinhAnh() {
        return maHinhAnh;
    }

    public void setMaHinhAnh(int maHinhAnh) {
        this.maHinhAnh = maHinhAnh;
    }

    public void setTenKyNang(String tenKyNang) {
        this.tenKyNang = tenKyNang;
    }
}