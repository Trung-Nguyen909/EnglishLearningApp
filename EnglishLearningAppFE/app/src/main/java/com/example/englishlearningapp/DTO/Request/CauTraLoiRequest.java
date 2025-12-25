package com.example.englishlearningapp.DTO.Request;

import com.google.gson.annotations.SerializedName;

public class CauTraLoiRequest {
    @SerializedName("idCauHoi")
    private int idCauHoi;

    @SerializedName("userAns")
    private String userAns;

    @SerializedName("correctAns")
    private String correctAns;

    public CauTraLoiRequest(int idCauHoi, String userAns, String correctAns) {
        this.idCauHoi = idCauHoi;
        this.userAns = userAns;
        this.correctAns = correctAns;
    }

    public int getIdCauHoi() {
        return idCauHoi;
    }

    public void setIdCauHoi(int idCauHoi) {
        this.idCauHoi = idCauHoi;
    }

    public String getUserAns() {
        return userAns;
    }

    public void setUserAns(String userAns) {
        this.userAns = userAns;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }
}

