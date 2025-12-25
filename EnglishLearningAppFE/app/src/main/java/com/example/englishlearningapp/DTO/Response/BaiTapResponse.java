package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BaiTapResponse implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("tenBaiTap")
    private String tenBaiTap;

    @SerializedName("loaiBaiTap")
    private String loaiBaiTap; // Nghe, Nói, Đọc, Viết

    @SerializedName("capdo")
    private String capDo; // Cơ bản, Trung bình...

    @SerializedName("trangThai")
    private String trangThai;

    public String getTrangThai() {
        return trangThai;
    }

    public int getId() {
        return id;
    }

    // Constructor, Getter, Setter
    public String getTenBaiTap() { return tenBaiTap; }
    public String getLoaiBaiTap() { return loaiBaiTap; }
    public String getCapDo() { return capDo; }
}