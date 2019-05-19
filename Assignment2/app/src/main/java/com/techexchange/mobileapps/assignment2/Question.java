package com.techexchange.mobileapps.assignment2;

public class Question {
    public static final String TAG = Question.class.getSimpleName();

    public String capitalName, correctCountry;
    public String wrongCountry1, wrongCountry2, wrongCountry3;
    public String questionText;

    public String selectedAnswer;
    public boolean answered = false;

    public Question(String capitalName, String correctCountry, String wrongCountry1, String wrongCountry2, String wrongCountry3) {
        this.capitalName = capitalName;
        this.correctCountry = correctCountry;
        this.wrongCountry1 = wrongCountry1;
        this.wrongCountry2 = wrongCountry2;
        this.wrongCountry3 = wrongCountry3;

        this.questionText = capitalName+" is the capital of which country?";
    }
}
