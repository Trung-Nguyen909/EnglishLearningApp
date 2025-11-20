package com.example.englishlearningapp.Model;

import java.util.List;

public class ChuDe {
    private String tenChuDe;
    private int idIcon;
    private List<ChuDePhu> danhSachMucCon;
    private boolean moRong = false;

    public ChuDe(String tenChuDe, int idIcon, List<ChuDePhu> danhSachMucCon) {
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

    public List<ChuDePhu> getDanhSachMucCon() {
        return danhSachMucCon;
    }

    public boolean isMoRong() {
        return moRong;
    }

    public void setMoRong(boolean moRong) {
        this.moRong = moRong;
    }
}
