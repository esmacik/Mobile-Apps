package com.techexchange.mobileapps.recyclerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SingleQuestionFragment extends Fragment {

    private static final String ARG_QUESTION_TEXT = "ARG_QUESTION_TEXT";
    private static final String ARG_CORRECT_ANSWER = "ARG_CORRECT_ANSWER";
    private static final String ARG_WRONG_ANSWER = "ARG_WRONG_ANSWER";
    private static final String ARG_QUESTION_INDEX = "ARG_QUESTION_INDEX";
    private static final String ARG_SELECTED_ANSWER = "ARG_SELECTED_ANSWER";
    static final String KEY_SCORE = "Score";

    private int questionIndex;

    static SingleQuestionFragment createFragmentFromQuestion(
            Question question, int questionIndex) {
        Bundle fragArgs = new Bundle();
        fragArgs.putString(ARG_QUESTION_TEXT, question.getQuestion());
        fragArgs.putString(ARG_CORRECT_ANSWER, question.getCorrectAnswer());
        fragArgs.putString(ARG_WRONG_ANSWER, question.getWrongAnswer());
        fragArgs.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragArgs.putString(ARG_SELECTED_ANSWER, question.getSelectedAnswer());

        SingleQuestionFragment frag = new SingleQuestionFragment();
        frag.setArguments(fragArgs);
        return frag;
    }

    private TextView questionText, correctText;
    private Button leftButton, rightButton;
    private List<Question> questionList;

    public SingleQuestionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_question, container, false);

        questionList = initQuestionList();

        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        correctText = rootView.findViewById(R.id.correct_incorrect_text);

        Bundle args = getArguments();
        if (args != null) {
            questionIndex = args.getInt(ARG_QUESTION_INDEX);
            String currentQuestion = args.getString(ARG_QUESTION_TEXT);
            String correctAnswer = args.getString(ARG_CORRECT_ANSWER);
            String wrongAnswer = args.getString(ARG_WRONG_ANSWER);

            questionText.setText(currentQuestion);

            if (Math.random() < 0.5) {
                leftButton.setText(correctAnswer);
                rightButton.setText(wrongAnswer);
            } else {
                leftButton.setText(wrongAnswer);
                rightButton.setText(correctAnswer);
            }
        }
        return rootView;
    }

    private void onAnswerButtonPressed(View v) {
        Button selectedButton = (Button) v;
        Question ques = questionList.get(questionIndex);
        if (ques.getCorrectAnswer().contentEquals(selectedButton.getText())) {
            correctText.setText("Correct!");
            answerListener.onQuestionAnswered(ques.getCorrectAnswer(), questionIndex);
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);
        } else {
            correctText.setText("Wrong!");
            answerListener.onQuestionAnswered(ques.getWrongAnswer(), questionIndex);
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);
        }
    }

    private List<Question> initQuestionList() {
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] correctAnswers = res.getStringArray(R.array.correct_answers);
        String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

        // Make sure that all arrays have the same length.
        Preconditions.checkState(questions.length == correctAnswers.length);
        Preconditions.checkState(questions.length == wrongAnswers.length);

        List<Question> qList = new ArrayList<>();

        for (int i = 0; i < questions.length; ++i) {
            qList.add(new Question(questions[i], correctAnswers[i], wrongAnswers[i]));
        }
        return qList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", questionIndex);
    }

    interface OnQuestionAnsweredListener {
        void onQuestionAnswered(String selectedAnswer, int questionId);
    }

    private OnQuestionAnsweredListener answerListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            answerListener = (OnQuestionAnsweredListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(
                    "The Context did not implement OnQuestionAnsweredListener!");
        }
    }
}
