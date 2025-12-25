package com.example.englishlearningapp.DTO.Response;

public class BaiHocResponse {
    private int id;
    private String tenBaiHoc;
    private String iconUrl;

    public BaiHocResponse(int id, String tenBaiHoc, String iconUrl) {
        this.id = id;
        this.tenBaiHoc = tenBaiHoc;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public String getTenBaiHoc() {
        return tenBaiHoc;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
