package com.example.englishlearningapp.Model;

import com.google.gson.annotations.SerializedName; // Nên thêm cái này để map chính xác với JSON
import java.io.Serializable;

public class NguPhap implements Serializable {
    private Integer id;

    // Nếu JSON trả về là "idBaiHoc", dùng tên biến y hệt hoặc dùng @SerializedName
    private Integer idBaiHoc;

    private String tenNguPhap;
    private String giaiThich;
    private String viDu;

    // --- THÊM MỚI ---
    // Biến này sẽ chứa tên bài học (VD: "Unit 1: Family")
    // Lưu ý: Backend phải trả về trường này trong JSON, hoặc bạn phải join bảng ở SQL
    @SerializedName("tenBaiHoc")
    private String tenBaiHoc;

    public NguPhap() { }

    // --- Getter & Setter cho tenBaiHoc ---
    public String getTenBaiHoc() {
        return tenBaiHoc;
    }

    public void setTenBaiHoc(String tenBaiHoc) {
        this.tenBaiHoc = tenBaiHoc;
    }

    // ... Các Getter/Setter cũ giữ nguyên ...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdBaiHoc() { return idBaiHoc; }
    public void setIdBaiHoc(Integer idBaiHoc) { this.idBaiHoc = idBaiHoc; }
    public String getTenNguPhap() { return tenNguPhap; }
    public void setTenNguPhap(String tenNguPhap) { this.tenNguPhap = tenNguPhap; }
    public String getGiaiThich() { return giaiThich; }
    public void setGiaiThich(String giaiThich) { this.giaiThich = giaiThich; }
    public String getViDu() { return viDu; }
    public void setViDu(String viDu) { this.viDu = viDu; }
}