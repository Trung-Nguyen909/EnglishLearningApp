package com.example.englishlearningapp.Model;

public class SubItem {
    private int id;
    private String name;
    private int iconResId;

    public SubItem(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() { return id; } // <-- Getter
    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
