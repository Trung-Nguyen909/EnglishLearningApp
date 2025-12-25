package com.example.englishlearningapp.DTO.Response;

import java.time.LocalTime;

public class BaiTap {

    private Integer id;

    private Integer idBaiHoc;

    private String tenBaiTap;

    private String loaiBaiTap;

    private String trangThai;

    private String capdo;

    private String thoigian;

    public BaiTap() {
    }

    public BaiTap(Integer id, Integer idBaiHoc, String tenBaiTap, String loaiBaiTap, String trangThai, String capdo, String thoigian) {
        this.id = id;
        this.idBaiHoc = idBaiHoc;
        this.tenBaiTap = tenBaiTap;
        this.loaiBaiTap = loaiBaiTap;
        this.trangThai = trangThai;
        this.capdo = capdo;
        this.thoigian = thoigian;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdBaiHoc() {
        return idBaiHoc;
    }

    public void setIdBaiHoc(Integer idBaiHoc) {
        this.idBaiHoc = idBaiHoc;
    }

    public String getTenBaiTap() {
        return tenBaiTap;
    }

    public void setTenBaiTap(String tenBaiTap) {
        this.tenBaiTap = tenBaiTap;
    }

    public String getLoaiBaiTap() {
        return loaiBaiTap;
    }

    public void setLoaiBaiTap(String loaiBaiTap) {
        this.loaiBaiTap = loaiBaiTap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getCapdo() {
        return capdo;
    }

    public void setCapdo(String capdo) {
        this.capdo = capdo;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }
}
