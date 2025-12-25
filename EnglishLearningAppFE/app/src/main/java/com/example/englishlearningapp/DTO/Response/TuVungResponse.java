package com.example.englishlearningapp.DTO.Response;

public class TuVungResponse {

    private Integer id;

    private Integer idBaiHoc;

    private String tuTiengAnh;

    private String nghiaTiengViet;

    private String phienAm;

    private String viDu;

    private String amThanhPhienAm;

    public TuVungResponse() {
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

    public String getTuTiengAnh() {
        return tuTiengAnh;
    }

    public void setTuTiengAnh(String tuTiengAnh) {
        this.tuTiengAnh = tuTiengAnh;
    }

    public String getNghiaTiengViet() {
        return nghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        this.nghiaTiengViet = nghiaTiengViet;
    }

    public String getPhienAm() {
        return phienAm;
    }

    public void setPhienAm(String phienAm) {
        this.phienAm = phienAm;
    }

    public String getViDu() {
        return viDu;
    }

    public void setViDu(String viDu) {
        this.viDu = viDu;
    }

    public String getAmThanhPhienAm() {
        return amThanhPhienAm;
    }

    public void setAmThanhPhienAm(String amThanhPhienAm) {
        this.amThanhPhienAm = amThanhPhienAm;
    }
}
