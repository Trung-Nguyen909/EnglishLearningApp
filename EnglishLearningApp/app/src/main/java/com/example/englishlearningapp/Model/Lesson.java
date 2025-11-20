package com.example.englishlearningapp.Model;

public class Lesson {
    private String type; // Vocabulary, Grammar, Speaking, ...
    private String title;
    private String level; // Beginner, Intermediate, Advanced
    private String time;
    private int typeColor; // Màu sắc cho nhãn loại bài học

    public Lesson(String type, String title, String level, String time, int typeColor) {
        this.type = type;
        this.title = title;
        this.level = level;
        this.time = time;
        this.typeColor = typeColor;
    }

    // Getters
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getLevel() { return level; }
    public String getTime() { return time; }
    public int getTypeColor() { return typeColor; }
}