package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

public class CauHoiVietResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("noiDungCauHoi")
    private String noiDungCauHoi; // Ví dụ: "Write about your family"

    @SerializedName("duLieuDapAn")
    private String duLieuDapAn;

    // Biến hỗ trợ hiển thị
    private String deBai; // Lấy từ noiDungCauHoi hoặc JSON
    private String dapAnNguoiDung = "";

    private String dapAnMau = "";

    // Hàm xử lý dữ liệu từ API
    public void xuLyDuLieu() {
        // Với bài viết, đề bài chính là nội dung câu hỏi
        this.deBai = noiDungCauHoi;

        // Nếu muốn lấy gợi ý từ JSON (nếu có)
        try {
            if (duLieuDapAn != null && !duLieuDapAn.isEmpty()) {
                JSONObject json = new JSONObject(duLieuDapAn);
                if (json.has("Hint")) {
                    this.deBai += "\n(Gợi ý: " + json.getString("Hint") + ")";
                }

                // 2. Lấy đáp án đúng (Correct) để chấm điểm
                if (json.has("Correct")) {
                    this.dapAnMau = json.getString("Correct").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getDeBai() { return deBai; }
    public String getDapAnNguoiDung() { return dapAnNguoiDung; }
    public void setDapAnNguoiDung(String dapAnNguoiDung) { this.dapAnNguoiDung = dapAnNguoiDung; }

    public String getDapAnMau() { return dapAnMau; }
}