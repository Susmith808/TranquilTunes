package com.example.tranquiltunes;

public class Question {
    private String questionText;
    private String[] options;
    private int[] scores;

    public Question(String questionText, String[] options, int[] scores) {
        this.questionText = questionText;
        this.options = options;
        this.scores = scores;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int[] getScores() {
        return scores;
    }
}
