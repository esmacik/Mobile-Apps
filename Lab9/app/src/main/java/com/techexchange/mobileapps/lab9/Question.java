package com.techexchange.mobileapps.lab9;

class Question {

    private final String question, correctAnswer, wrongAnswer;

    public Question(String question, String correctAnswer, String wrongAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }
}
