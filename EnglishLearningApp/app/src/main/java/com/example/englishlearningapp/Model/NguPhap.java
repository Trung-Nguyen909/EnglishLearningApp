package com.example.englishlearningapp.Model;

import java.io.Serializable;

public class NguPhap implements Serializable {
    private Integer id;
    private Integer idBaiHoc;
    private String tenNguPhap;
    private String giaiThich;
    private String viDu;
    // Constructor, Getter, Setter
    public NguPhap() { }

    public Integer getId() {
        return id;
    }

    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }

    public void setTenNguPhap(String tenNguPhap) {
        this.tenNguPhap = tenNguPhap;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdBaiHoc(Integer idBaiHoc) {
        this.idBaiHoc = idBaiHoc;
    }

    public void setViDu(String viDu) {
        this.viDu = viDu;
    }
    public Integer getIdBaiHoc() {
        return idBaiHoc;
    }

    public String getTenNguPhap() { return tenNguPhap; }
    public String getGiaiThich() { return giaiThich; }
    public String getViDu() { return viDu; }



}