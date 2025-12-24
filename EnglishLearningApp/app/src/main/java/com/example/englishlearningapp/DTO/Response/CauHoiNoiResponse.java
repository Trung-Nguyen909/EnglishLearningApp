package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

public class CauHoiNoiResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("noiDungCauHoi")
    private String noiDungCauHoi; // Ví dụ: "Read aloud: Good morning"

    @SerializedName("duLieuDapAn")
    private String duLieuDapAn; // JSON: {"Correct": "Good morning"}

    // Biến nội bộ để xử lý giao diện
    private String cauMau; // Câu cần đọc (Lấy từ JSON)
    private String dapAnNguoiDung = "";
    private boolean isChinhXac = false;

    // Constructor mặc định cho Retrofit
    public CauHoiNoiResponse() {}

    // Hàm xử lý dữ liệu JSON (Gọi sau khi nhận từ API)
    public void xuLyDuLieu() {
        try {
            if (duLieuDapAn != null) {
                JSONObject json = new JSONObject(duLieuDapAn);
                // Lấy câu mẫu từ key "Correct"
                if (json.has("Correct")) {
                    this.cauMau = json.getString("Correct");
                } else {
                    this.cauMau = "No sample sentence";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.cauMau = "Error parsing data";
        }
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getCauMau() { return cauMau; } // Hiển thị lên màn hình
    public String getDapAnNguoiDung() { return dapAnNguoiDung; }
    public void setDapAnNguoiDung(String dapAnNguoiDung) { this.dapAnNguoiDung = dapAnNguoiDung; }
    public boolean isChinhXac() { return isChinhXac; }
    public void setChinhXac(boolean chinhXac) { isChinhXac = chinhXac; }
}