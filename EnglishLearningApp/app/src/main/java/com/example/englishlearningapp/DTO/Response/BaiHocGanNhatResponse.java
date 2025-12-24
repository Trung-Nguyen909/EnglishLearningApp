package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;

public class BaiHocGanNhatResponse {
    @SerializedName("tenKhoaHoc")
    private String tenKhoaHoc;

    @SerializedName("tenBaiHoc")
    private String tenBaiHoc;

    @SerializedName("idBaiHoc")
    private int idBaiHoc;

    @SerializedName("phanTram")
    private double phanTram;

    // Getter
    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public String getTenBaiHoc() { return tenBaiHoc; }
    public int getIdBaiHoc() { return idBaiHoc; }
    public double getPhanTram() { return phanTram; }
}