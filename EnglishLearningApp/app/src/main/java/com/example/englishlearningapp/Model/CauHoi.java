package com.example.englishlearningapp.Model;

import java.util.List;

public class CauHoi {
    private final int id;
    private final String instruction;
    private final String sentence;
    private final List<String> options;
    private final String correctAnswer;
    private String selectedAnswer;

    public CauHoi(int id, String instruction, String sentence, List<String> options, String correctAnswer) {
        this.id = id;
        this.instruction = instruction;
        this.sentence = sentence;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = null;
    }

    // Getters
    public int getId() { return id; }
    public String getInstruction() { return instruction; }
    public String getSentence() { return sentence; }
    public List<String> getOptions() { return options; }
    public String getSelectedAnswer() { return selectedAnswer; }

    // Setter
    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}