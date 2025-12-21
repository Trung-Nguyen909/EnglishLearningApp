package com.example.englishlearningapp.DTO.Response;
public class BaiHocResponse {
    private int id;
    private String tenBaiHoc;
    private int iconRes;

    public BaiHocResponse(int id, String tenBaiHoc, int iconRes) {
        this.id = id;
        this.tenBaiHoc = tenBaiHoc;
        this.iconRes = iconRes;
    }

    public int getId() {
        return id;
    }

    public String getTenBaiHoc() {
        return tenBaiHoc;
    }

    public int getIconRes() {
        return iconRes;
    }
}

