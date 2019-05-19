package com.techexchange.mobileapps.recyclerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Preconditions;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


// UPDATED AFTER SUBMISSION



public class MainActivity extends AppCompatActivity implements SingleQuestionFragment.OnQuestionAnsweredListener {

    private static Context context;
    private static final String TAG = MainActivity.class.getSimpleName();
    private int answered = 0;
    private int score = 0;

    private static ViewPager viewPager;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        FragmentManager fm = getSupportFragmentManager();

        viewPager = findViewById(R.id.contact_pager);
        viewPager.setAdapter(new QuestionFragmentAdapter(fm));
    }

    public void onQuestionAnswered(String selectedAnswer, int questionId) {
        Log.d(TAG, "The " + selectedAnswer + " button was pressed!");
        Resources res = getResources();
        String[] correctAnswer = res.getStringArray(R.array.correct_answers);

        answered++;
        if (selectedAnswer.contentEquals(correctAnswer[questionId])){
            score++;
        }
        if (answered == correctAnswer.length){
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra("score", score);
            startActivityForResult(intent, 0);
            score = 0;
            answered = 0;
        }
    }

    public static void resetView(){
        viewPager.setCurrentItem(0);
    }

    private final class QuestionFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Question> qList;
        private final String TAG = QuestionFragmentAdapter.class.getSimpleName();

        public QuestionFragmentAdapter(FragmentManager fm) {
            super(fm);
            qList = new ArrayList<>();
            Resources res = getContext().getResources();
            String[] questions = res.getStringArray(R.array.questions);
            String[] correctAnswers = res.getStringArray(R.array.correct_answers);
            String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

            // Make sure that all arrays have the same length.
            Preconditions.checkState(questions.length == correctAnswers.length);
            Preconditions.checkState(questions.length == wrongAnswers.length);

            for (int i = 0; i < questions.length; ++i) {
                qList.add(new Question(questions[i], correctAnswers[i], wrongAnswers[i]));
            }
        }

        @Override
        public Fragment getItem(int position) {
            Question question = qList.get(position);
            return SingleQuestionFragment.createFragmentFromQuestion(question, position);
        }

        @Override
        public int getCount() {
            return qList.size();
        }
    }
}