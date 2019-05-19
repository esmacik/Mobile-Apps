package com.techexchange.mobileapps.lab9;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex;

    private int currentScore = 0;

    static final String KEY_SCORE = "Score";

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseApp app = FirebaseApp.initializeApp(getActivity());
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);
        database.goOnline();

        DatabaseReference ref = database.getReference();
        ref = ref.child("lab9").child("questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                onQuestionListChanged(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                Log.d(TAG, "The database transaction was cancelled", databaseError.toException());
            }
        });

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(this::onNextButtonPressed);

        correctText = rootView.findViewById(R.id.correct_incorrect_text);
        currentQuestionIndex = 0;
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("CurrentIndex");
        }
        //updateView();
        return rootView;
    }

    private void onQuestionListChanged(DataSnapshot dataSnapshot) {
        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
            DataSnapshot temp = dataSnapshot.child(Integer.toString(i));
            String question = temp.child("question").getValue().toString();
            String correctAnswer = temp.child("correctAnswer").getValue().toString();
            String wrongAnswer = temp.child("wrongAnswer").getValue().toString();

            Log.d(TAG, question+", "+correctAnswer+", "+wrongAnswer);

            questionList.add(new Question(question, correctAnswer, wrongAnswer));
        }
        updateView();
    }

    private void onNextButtonPressed(View v){
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            updateView();
        } else {
            Intent scoreIntent = new Intent(getActivity(), ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }
    }

    private void onAnswerButtonPressed(View v) {
        Button selectedButton = (Button) v;
        Question ques = questionList.get(currentQuestionIndex);
        if (ques.getCorrectAnswer().contentEquals(selectedButton.getText())) {
            currentScore++;
            correctText.setText("Correct!");
        } else {
            correctText.setText("Wrong!");
        }
        nextButton.setEnabled(true);
    }

    private void updateView() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());
        if (Math.random() < 0.5) {
            leftButton.setText(currentQuestion.getCorrectAnswer());
            rightButton.setText(currentQuestion.getWrongAnswer());
        } else {
            rightButton.setText(currentQuestion.getCorrectAnswer());
            leftButton.setText(currentQuestion.getWrongAnswer());
        }
        nextButton.setEnabled(false);
        correctText.setText(R.string.initial_correct_incorrect);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", currentQuestionIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
            getActivity().finish();
        }
        boolean restartPressed = data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ, false);
        if(restartPressed) {
            currentScore = 0;
            currentQuestionIndex = 0;
            updateView();
        }
    }


}
