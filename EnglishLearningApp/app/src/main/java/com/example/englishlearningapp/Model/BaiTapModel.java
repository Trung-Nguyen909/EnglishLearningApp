package com.example.englishlearningapp.Model;

public class BaiTapModel {
    private int id;
    private int iconResId;
    private String title;
    private String date;
    private String statusText;
    private int statusColorRes;
    private boolean isError;
    private int tgianLam;

    public BaiTapModel(int id, int iconResId, String title, String date, String statusText, int statusColorRes, boolean isError, int tgianLam) {
        this.id = id;
        this.iconResId = iconResId;
        this.title = title;
        this.date = date;
        this.statusText = statusText;
        this.statusColorRes = statusColorRes;
        this.isError = isError;
        this.tgianLam = tgianLam;
    }

    public int getTgianLam() {
        return tgianLam;
    }

    public void setTgianLam(int tgianLam) {
        this.tgianLam = tgianLam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconResId() { return iconResId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStatusText() { return statusText; }
    public int getStatusColorRes() { return statusColorRes; }
    public boolean isError() { return isError; }
}
