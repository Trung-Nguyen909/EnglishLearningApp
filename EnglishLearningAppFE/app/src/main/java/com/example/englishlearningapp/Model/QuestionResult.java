package com.example.englishlearningapp.Model;

public class QuestionResult {
    private String questionText;
    private String userAnswer;
    private String correctAnswer;
    private String explanation;

    public QuestionResult(String questionText, String userAnswer, String correctAnswer, String explanation) {
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
