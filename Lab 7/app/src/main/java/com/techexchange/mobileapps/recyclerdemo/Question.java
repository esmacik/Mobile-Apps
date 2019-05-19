package com.techexchange.mobileapps.recyclerdemo;

class Question {

    private final String question, correctAnswer, wrongAnswer;

    private String selectedAnswer;

    public Question(String question, String correctAnswer, String wrongAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
        this.selectedAnswer = null;
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

    public String getSelectedAnswer(){
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer){
        this.selectedAnswer = selectedAnswer;
    }
}
