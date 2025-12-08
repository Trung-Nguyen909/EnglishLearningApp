package com.example.englishlearningapp.Model;

public class BaiTapModel {
    private int iconResId;
    private String title;
    private String date;
    private String statusText;
    private int statusColorRes;
    private boolean isError;

    public BaiTapModel(int iconResId, String title, String date,
                       String statusText, int statusColorRes, boolean isError) {
        this.iconResId = iconResId;
        this.title = title;
        this.date = date;
        this.statusText = statusText;
        this.statusColorRes = statusColorRes;
        this.isError = isError;
    }

    public int getIconResId() { return iconResId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStatusText() { return statusText; }
    public int getStatusColorRes() { return statusColorRes; }
    public boolean isError() { return isError; }
}
