package com.example.englishlearningapp.DTO.Response;

import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

public class CauHoiResponse implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("noiDungCauHoi")
    private String noiDung;

    @SerializedName("loaiCauHoi")
    private String loaiCauHoi;

    @SerializedName("duLieuDapAn")
    private String jsonDapAn;

    @SerializedName("giaiThich")
    private String giaiThich;

    @SerializedName("audioUrl")
    private String audioUrl;

    // Các biến hỗ trợ hiển thị
    private String dapAnA, dapAnB, dapAnC, dapAnD;

    private String dapAnDung;

    private String dapAnDungRaw;

    // Biến lưu đáp án người dùng chọn (A, B...)
    private String dapAnDaChon = "";

    // --- HÀM XỬ LÝ DỮ LIỆU (ĐÃ SỬA) ---
    public void xuLyDuLieu() {
        try {
            if (jsonDapAn == null || jsonDapAn.isEmpty()) return;

            JSONObject json = new JSONObject(jsonDapAn);

            // 1. Lấy nội dung các đáp án ABCD
            this.dapAnA = json.optString("A", "");
            this.dapAnB = json.optString("B", "");
            this.dapAnC = json.optString("C", "");
            this.dapAnD = json.optString("D", "");

            // 2. Lấy đáp án đúng THÔ (A, B, C, D hoặc từ điền khuyết)
            // Đây là cái dùng để so sánh tính điểm
            String correctKey = json.optString("Correct", "").trim();
            this.dapAnDungRaw = correctKey;

            // 3. Lấy nội dung hiển thị của đáp án đúng (để hiện lời giải)
            if (json.has(correctKey)) {
                // Nếu Correct="A", json có key "A" -> Lấy nội dung của A (VD: "Father")
                this.dapAnDung = json.optString(correctKey);
            } else {
                // Nếu không phải ABCD (dạng điền từ), nội dung chính là key
                this.dapAnDung = correctKey;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // --- Getter & Setter ---

    public String getDapAnDungRaw() {
        return dapAnDungRaw; // Dùng hàm này để chấm điểm
    }

    public String getDapAnDaChon() { return dapAnDaChon; }
    public void setDapAnDaChon(String dapAnDaChon) { this.dapAnDaChon = dapAnDaChon; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getLoaiCauHoi() { return loaiCauHoi; }
    public void setLoaiCauHoi(String loaiCauHoi) { this.loaiCauHoi = loaiCauHoi; }

    public String getJsonDapAn() { return jsonDapAn; }
    public void setJsonDapAn(String jsonDapAn) { this.jsonDapAn = jsonDapAn; }

    public String getGiaiThich() { return giaiThich; }
    public void setGiaiThich(String giaiThich) { this.giaiThich = giaiThich; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public String getDapAnA() { return dapAnA; }
    public String getDapAnB() { return dapAnB; }
    public String getDapAnC() { return dapAnC; }
    public String getDapAnDung() { return dapAnDung; }
    public String getDapAnD() { return dapAnD; }

    // Getter cho đáp án thô (A, B, C, D)
    public String getDapAnDungRawAnswer() {
        return dapAnDungRaw; // Trả về "A", "B", "C", "D" hoặc nội dung câu
    }
}