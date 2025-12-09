package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;

public class NhatKyHoatDong {

    @SerializedName("id")
    private int id;

    @SerializedName("idNguoiDung")
    private int idNguoiDung;

    @SerializedName("ngayHoatDong")
    private String ngayHoatDong;

    @SerializedName("soPhutHoc")
    private int soPhutHoc;

    @SerializedName("tongSoBaiDaLam")
    private int tongSoBaiDaLam;

    // --- Getter và Setter ---
    public String getNgayHoatDong() {
        return ngayHoatDong;
    }

    public void setNgayHoatDong(String ngayHoatDong) {
        this.ngayHoatDong = ngayHoatDong;
    }
}
