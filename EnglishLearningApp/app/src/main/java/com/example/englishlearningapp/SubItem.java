package com.example.englishlearningapp;

public class SubItem {
    private String name;
    private int iconResId;

    public SubItem(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
