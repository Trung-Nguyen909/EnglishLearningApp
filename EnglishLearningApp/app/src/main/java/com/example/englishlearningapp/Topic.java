package com.example.englishlearningapp;

import java.util.List;

public class Topic {
    private String name;
    private int iconResId;
    private List<SubItem> subItems;
    private boolean isExpanded = false;

    public Topic(String name, int iconResId, List<SubItem> subItems) {
        this.name = name;
        this.iconResId = iconResId;
        this.subItems = subItems;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
    public List<SubItem> getSubItems() { return subItems; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
