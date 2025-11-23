package com.example.englishlearningapp.Model;

import java.util.List;

public class CauHoiModel {
    private final int maCauHoi;
    private final String huongDan;
    private final String noiDung;
    private final List<String> cacLuaChon;
    private final String dapAnDung;
    private String dapAnDaChon;

    // Constructor
    public CauHoiModel(int maCauHoi, String huongDan, String noiDung, List<String> cacLuaChon, String dapAnDung) {
        this.maCauHoi = maCauHoi;
        this.huongDan = huongDan;
        this.noiDung = noiDung;
        this.cacLuaChon = cacLuaChon;
        this.dapAnDung = dapAnDung;
        this.dapAnDaChon = null;
    }
    public CauHoiModel(int maCauHoi, String huongDan, String noiDung, String dapAnGoiY) {
        this.maCauHoi = maCauHoi;
        this.huongDan = huongDan;
        this.noiDung = noiDung;
        this.cacLuaChon = null;
        this.dapAnDung = dapAnGoiY;
        this.dapAnDaChon = null;
    }
    public int getMaCauHoi() {
        return maCauHoi;
    }

    public String getHuongDan() {
        return huongDan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public List<String> getCacLuaChon() {
        return cacLuaChon;
    }

    public String getDapAnDung() {
        return dapAnDung;
    }

    public String getDapAnDaChon() {
        return dapAnDaChon;
    }

    public void setDapAnDaChon(String dapAnDaChon) {
        this.dapAnDaChon = dapAnDaChon;
    }
}