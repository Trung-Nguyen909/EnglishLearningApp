package com.example.englishlearningapp.Model;

import java.util.List;

public class ChuDeModel {
    private String tenChuDe;
    private int idIcon;
    private List<ChuDePhuModel> danhSachMucCon;
    private boolean moRong = false;

    public ChuDeModel(String tenChuDe, int idIcon, List<ChuDePhuModel> danhSachMucCon) {
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

    public List<ChuDePhuModel> getDanhSachMucCon() {
        return danhSachMucCon;
    }

    public boolean isMoRong() {
        return moRong;
    }

    public void setMoRong(boolean moRong) {
        this.moRong = moRong;
    }
}
