package com.example.englishlearningapp.DTO.Response;

import java.time.LocalDate;

public class KhoaHocResponse {
    private Integer id;

    private String tenKhoaHoc;

    private String moTa;

    private String trinhDo;

    private String ngayTao;

    public KhoaHocResponse(Integer id, String tenKhoaHoc, String moTa, String trinhDo, String ngayTao) {
        this.id = id;
        this.tenKhoaHoc = tenKhoaHoc;
        this.moTa = moTa;
        this.trinhDo = trinhDo;
        this.ngayTao = ngayTao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String  ngayTao) {
        this.ngayTao = ngayTao;
    }
}
