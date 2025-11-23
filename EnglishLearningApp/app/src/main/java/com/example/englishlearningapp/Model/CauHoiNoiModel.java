package com.example.englishlearningapp.Model;

public class CauHoiNoiModel {
    private String cauMau;
    private String dapAnNguoiDung;
    private boolean chinhXac;

    public CauHoiNoiModel(String cauMau) {
        this.cauMau = cauMau;
        this.dapAnNguoiDung = "";
        this.chinhXac = false;
    }

    // Getter & Setter (Giữ tiền tố get/set, Việt hóa phần sau)

    public String getCauMau() {
        return cauMau;
    }

    public void setCauMau(String cauMau) {
        this.cauMau = cauMau;
    }

    public String getDapAnNguoiDung() {
        return dapAnNguoiDung;
    }

    public void setDapAnNguoiDung(String dapAnNguoiDung) {
        this.dapAnNguoiDung = dapAnNguoiDung;
    }

    public boolean isChinhXac() {
        return chinhXac;
    }

    public void setChinhXac(boolean chinhXac) {
        this.chinhXac = chinhXac;
    }
}