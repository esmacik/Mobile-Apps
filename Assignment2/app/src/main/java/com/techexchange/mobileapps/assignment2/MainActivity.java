package com.techexchange.mobileapps.assignment2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements QuestionListFragment.OnQuestionPressedListener, QuestionFragment.onQuestionSubmitPressedListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize questionList
        questionList = QuestionListFactory.get(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment recyclerWithButtonsFrag = fm.findFragmentById(R.id.fragment_container);
        if (recyclerWithButtonsFrag == null) {
            recyclerWithButtonsFrag = new QuestionListFragment();
            fm.beginTransaction().add(R.id.fragment_container, recyclerWithButtonsFrag).commit();
        }
    }

    private static final String QUESTION_NUMBER = "QUESTION_NUMBER";

    public void onQuestionPressed(int i) {
        Log.d(TAG, "onQuestionPressed: Question " + i + " pressed");
        Bundle bundle = new Bundle();
        bundle.putInt(QUESTION_NUMBER, i);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, questionFragment)
                .commit();
    }

    public void onQuestionSubmitPressed() {
        getSupportFragmentManager().popBackStack();
    }
}