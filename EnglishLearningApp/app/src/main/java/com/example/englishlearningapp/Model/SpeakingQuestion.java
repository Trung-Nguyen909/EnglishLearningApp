package com.example.englishlearningapp.Model;

public class SpeakingQuestion {
    private String sentence;
    private String userAnswer;
    private boolean isCorrect;

    public SpeakingQuestion(String sentence) {
        this.sentence = sentence;
        this.userAnswer = "";
        this.isCorrect = false;
    }

    // Getter & Setter
    public String getSentence() { return sentence; }
    public void setSentence(String sentence) { this.sentence = sentence; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
