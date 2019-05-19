package com.techexchange.mobileapps.assignment2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = "QuestionFragment";

    private TextView questionText;
    private RadioGroup answerChoicesGroup;
    private RadioButton button0, button1, button2, button3;
    private Button submitButton;
    private ArrayList<Question> questionList = QuestionListFactory.get(getContext());
    private String tappedAnswer;

    private ArrayList<String> answerChoicesText = new ArrayList<>();

    private static final String QUESTION_NUMBER = "QUESTION_NUMBER";
    private static final String ANSWER_CHOICES = "ANSWER_CHOICES";

    private Question currentQuestion;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        currentQuestion = questionList.get(getArguments().getInt(QUESTION_NUMBER));

        //Assign question text
        questionText = view.findViewById(R.id.question_text);
        questionText.setText(currentQuestion.questionText);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(v -> onSubmitButtonPressed());

        //Assign radio group
        answerChoicesGroup = view.findViewById(R.id.radio_button_group);
        answerChoicesGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
                switch (checkedId) {
                    case R.id.button0:
                        tappedAnswer = button0.getText().toString();
                        break;
                    case R.id.button1:
                        tappedAnswer = button1.getText().toString();
                        break;
                    case R.id.button2:
                        tappedAnswer = button2.getText().toString();
                        break;
                    case R.id.button3:
                        tappedAnswer = button3.getText().toString();
                        break;
                }
                Log.d(TAG, "onCheckedChanged: selected answer = " + currentQuestion.selectedAnswer);
            }
        });

        //assign buttons in order then shuffle
        assignButtonsAndShuffle(view, savedInstanceState);

        Log.d(TAG, "onCreateView: buttons shuffled");


        return view;
    }

    private void assignButtonsAndShuffle(View view, Bundle savedInstanceState) {
        Log.d(TAG, "assignButtonsAndShuffle: ");
        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);

        if (savedInstanceState == null) {
            answerChoicesText.add(currentQuestion.correctCountry);
            answerChoicesText.add(currentQuestion.wrongCountry1);
            answerChoicesText.add(currentQuestion.wrongCountry2);
            answerChoicesText.add(currentQuestion.wrongCountry3);

            Collections.shuffle(answerChoicesText);
            button0.setText(answerChoicesText.get(0));
            button1.setText(answerChoicesText.get(1));
            button2.setText(answerChoicesText.get(2));
            button3.setText(answerChoicesText.get(3));
        } else {
            answerChoicesText = savedInstanceState.getStringArrayList(ANSWER_CHOICES);
            button0.setText(answerChoicesText.get(0));
            button1.setText(answerChoicesText.get(1));
            button2.setText(answerChoicesText.get(2));
            button3.setText(answerChoicesText.get(3));
        }

        if (currentQuestion.answered) {
            if (button0.getText().toString().equals(currentQuestion.selectedAnswer)) {
                button0.setChecked(true);
                return;
            }
            if (button1.getText().toString().equals(currentQuestion.selectedAnswer)) {
                button1.setChecked(true);
                return;
            }
            if (button2.getText().toString().equals(currentQuestion.selectedAnswer)) {
                button2.setChecked(true);
                return;
            }
            if (button3.getText().toString().equals(currentQuestion.selectedAnswer)) {
                button3.setChecked(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(ANSWER_CHOICES, answerChoicesText);
    }


    private onQuestionSubmitPressedListener questionSubmitPressed;

    interface onQuestionSubmitPressedListener {
        void onQuestionSubmitPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        questionSubmitPressed = (onQuestionSubmitPressedListener) context;
    }

    public void onSubmitButtonPressed() {
        currentQuestion.selectedAnswer = tappedAnswer;
        currentQuestion.answered = true;
        questionSubmitPressed.onQuestionSubmitPressed();
    }
}
