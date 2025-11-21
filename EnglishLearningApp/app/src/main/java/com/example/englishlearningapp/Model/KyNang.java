package com.example.englishlearningapp.Model;

public class KyNang {
    private String tenKyNang; // title
    private int maHinhAnh;    // iconResId

    public KyNang(String tenKyNang, int maHinhAnh) {
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