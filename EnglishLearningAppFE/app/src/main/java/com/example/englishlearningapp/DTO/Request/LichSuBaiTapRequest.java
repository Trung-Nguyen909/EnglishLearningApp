package com.example.englishlearningapp.DTO.Request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LichSuBaiTapRequest {
    @SerializedName("idTest")
    private Integer idTest;

    @SerializedName("idBaiTap")
    private Integer idBaiTap;

    @SerializedName("tenBai")
    private String tenBai;

    @SerializedName("loaiBai")
    private String loaiBai;

    @SerializedName("tgianLam")
    private int tgianLam; // Thời gian làm bài (giây)

    @SerializedName("cauTraLoi")
    private List<CauTraLoiRequest> cauTraLoi;

    public LichSuBaiTapRequest() {
    }

    public LichSuBaiTapRequest(Integer idTest, Integer idBaiTap, String tenBai,
                               String loaiBai, int tgianLam, List<CauTraLoiRequest> cauTraLoi) {
        this.idTest = idTest;
        this.idBaiTap = idBaiTap;
        this.tenBai = tenBai;
        this.loaiBai = loaiBai;
        this.tgianLam = tgianLam;
        this.cauTraLoi = cauTraLoi;
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

    public int getTgianLam() {
        return tgianLam;
    }

    public void setTgianLam(int tgianLam) {
        this.tgianLam = tgianLam;
    }

    public List<CauTraLoiRequest> getCauTraLoi() {
        return cauTraLoi;
    }

    public void setCauTraLoi(List<CauTraLoiRequest> cauTraLoi) {
        this.cauTraLoi = cauTraLoi;
    }
}

