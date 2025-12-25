package com.example.englishlearningapp.Model;

import com.example.englishlearningapp.DTO.Response.BaiHocResponse;

import java.util.List;

public class ChuDeModel {
    private String tenChuDe;
    private int idIcon;
    private List<BaiHocResponse> danhSachMucCon;
    private boolean moRong = false;

    public ChuDeModel(String tenChuDe, int idIcon, List<BaiHocResponse> danhSachMucCon) {
        this.tenChuDe = tenChuDe;
        this.idIcon = idIcon;
        this.danhSachMucCon = danhSachMucCon;
    }

    public String getTenChuDe() {
        return tenChuDe;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public List<BaiHocResponse> getDanhSachMucCon() {
        return danhSachMucCon;
    }

    public boolean isMoRong() {
        return moRong;
    }

    public void setMoRong(boolean moRong) {
        this.moRong = moRong;
    }

    public void setDanhSachMucCon(List<BaiHocResponse> danhSachMucCon) {
        this.danhSachMucCon = danhSachMucCon;
    }
}
