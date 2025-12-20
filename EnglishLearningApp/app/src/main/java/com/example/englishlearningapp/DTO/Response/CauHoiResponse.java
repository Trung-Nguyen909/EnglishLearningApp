package com.example.englishlearningapp.DTO.Response;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CauHoiResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("noiDungCauHoi")
    private String noiDung;

    @SerializedName("loaiCauHoi")
    private String loaiCauHoi; // ABCD, Speaking, FillBlank...

    @SerializedName("duLieuDapAn")
    private String jsonDapAn; // Dữ liệu thô từ API

    @SerializedName("giaiThich")
    private String giaiThich;

    @SerializedName("audioUrl")
    private String audioUrl;

    // Các biến hỗ trợ hiển thị (Không map với JSON)
    private String dapAnA, dapAnB, dapAnC, dapAnD;
    private String dapAnDung;

    // Getter & Setter...

    // HÀM QUAN TRỌNG: Parse JSON để lấy đáp án
    public void xuLyDuLieu() {
        try {
            if (jsonDapAn == null || jsonDapAn.isEmpty()) return;

            JSONObject json = new JSONObject(jsonDapAn);

            // Lấy đáp án trắc nghiệm (nếu có)
            this.dapAnA = json.optString("A", "");
            this.dapAnB = json.optString("B", "");
            this.dapAnC = json.optString("C", "");
            this.dapAnD = json.optString("D", "");

            // Lấy đáp án đúng
            // Nếu JSON có field "Correct", giá trị có thể là "A" hoặc câu trả lời full
            String correctKey = json.optString("Correct", "");

            if (json.has(correctKey)) {
                // Ví dụ Correct="A" -> Lấy nội dung của A
                this.dapAnDung = json.optString(correctKey);
            } else {
                // Ví dụ Correct="Hello" -> Đáp án là Hello
                this.dapAnDung = correctKey;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String dapAnDaChon = ""; // Biến lưu đáp án người dùng chọn

    public String getDapAnDaChon() { return dapAnDaChon; }
    public void setDapAnDaChon(String dapAnDaChon) { this.dapAnDaChon = dapAnDaChon; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getLoaiCauHoi() {
        return loaiCauHoi;
    }

    public void setLoaiCauHoi(String loaiCauHoi) {
        this.loaiCauHoi = loaiCauHoi;
    }

    public String getJsonDapAn() {
        return jsonDapAn;
    }

    public void setJsonDapAn(String jsonDapAn) {
        this.jsonDapAn = jsonDapAn;
    }

    public String getGiaiThich() {
        return giaiThich;
    }

    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getDapAnA() {
        return dapAnA;
    }

    public void setDapAnA(String dapAnA) {
        this.dapAnA = dapAnA;
    }

    public String getDapAnB() {
        return dapAnB;
    }

    public void setDapAnB(String dapAnB) {
        this.dapAnB = dapAnB;
    }

    public String getDapAnC() {
        return dapAnC;
    }

    public void setDapAnC(String dapAnC) {
        this.dapAnC = dapAnC;
    }

    public String getDapAnD() {
        return dapAnD;
    }

    public void setDapAnD(String dapAnD) {
        this.dapAnD = dapAnD;
    }

    public String getDapAnDung() {
        return dapAnDung;
    }

    public void setDapAnDung(String dapAnDung) {
        this.dapAnDung = dapAnDung;
    }
}
