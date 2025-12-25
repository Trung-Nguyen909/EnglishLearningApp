package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class KyNangResponse implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("tenKyNang")
    private String tenKyNang;

    public KyNangResponse() {} // Constructor rỗng

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenKyNang() { return tenKyNang; }
    public void setTenKyNang(String tenKyNang) { this.tenKyNang = tenKyNang; }
}