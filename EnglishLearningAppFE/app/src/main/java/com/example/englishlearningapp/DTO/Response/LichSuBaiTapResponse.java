package com.example.englishlearningapp.DTO.Response;
import java.math.BigDecimal;
public class LichSuBaiTapResponse {
    private Integer id;

    private Integer idNguoiDung;

    private Integer idTest;

    private Integer idBaiTap;

    private String tenBai;

    private String loaiBai; // 'TEST' hoặc 'BAITAP'

    private double diemSo;

    private String trangThai;

    private String tgianNopBai;

    private int tgianLam;

    public int getTgianLam() {
        return tgianLam;
    }

    public void setTgianLam(int tgianLam) {
        this.tgianLam = tgianLam;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(Integer idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public Integer getIdTest() {
        return idTest;
    }

    public void setIdTest(Integer idTest) {
        this.idTest = idTest;
    }

    public Integer getIdBaiTap() {
        return idBaiTap;
    }

    public void setIdBaiTap(Integer idBaiTap) {
        this.idBaiTap = idBaiTap;
    }

    public String getTenBai() {
        return tenBai;
    }

    public void setTenBai(String tenBai) {
        this.tenBai = tenBai;
    }

    public String getLoaiBai() {
        return loaiBai;
    }

    public void setLoaiBai(String loaiBai) {
        this.loaiBai = loaiBai;
    }

    public double getDiemSo() {
        return diemSo;
    }

    public void setDiemSo(double diemSo) {
        this.diemSo = diemSo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTgianNopBai() {
        return tgianNopBai;
    }

    public void setTgianNopBai(String tgianNopBai) {
        this.tgianNopBai = tgianNopBai;
    }
}
