package com.techexchange.mobileapps.assignment2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionListFragment extends Fragment {

    private static final String TAG = "QuestionListFragment";
    private RecyclerView recyclerView;
    private Button submitButton;
    private Button sendEmailButton;

    private static String SUBMIT_PRESSED = "SUBMIT_PRESSED";
    private static boolean submitWasPressed = false;

    public ArrayList<Question> questionList = new ArrayList<>();

    public QuestionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        questionList = QuestionListFactory.get(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(questionList));

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> onSubmitButtonPressed());

        sendEmailButton = view.findViewById(R.id.send_email_button);
        sendEmailButton.setEnabled(false);
        sendEmailButton.setOnClickListener(v -> onEmailButtonPressed());

        if (savedInstanceState != null){
            submitWasPressed = savedInstanceState.getBoolean(SUBMIT_PRESSED);
        }
        if (submitWasPressed){
            submitButton.setEnabled(false);
            sendEmailButton.setEnabled(true);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBMIT_PRESSED, submitWasPressed);
    }

    private void onSubmitButtonPressed() {
        submitWasPressed = true;
        submitButton.setEnabled(false);
        sendEmailButton.setEnabled(true);
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(questionList));
    }

    private void onEmailButtonPressed() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Score Report");

        String emailBody = createEmailBody();

        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        startActivity(intent);
    }

    private int calculateQuizScore() {
        int quizScore = 0;
        for (int i = 0; i < questionList.size(); i++) {
            Question currentQuestion = questionList.get(i);
            if (currentQuestion.selectedAnswer != null) {
                if (currentQuestion.selectedAnswer.equals(currentQuestion.correctCountry)) {
                    quizScore++;
                }
            }
        }
        return quizScore;
    }

    private String createEmailBody() {
        int quizScore = calculateQuizScore();
        String emailBody = "Summary: " + quizScore + " out of " + questionList.size() + "\n\n";
        emailBody += "Here is the complete score report:\n\n";
        for (int i = 0; i < questionList.size(); i++) {
            Question currentQuestion = questionList.get(i);

            emailBody += "Question: " + currentQuestion.questionText + "\n";
            if (currentQuestion.selectedAnswer == null) {
                emailBody += "Your answer: none\n";
                emailBody += "Correct answer: " + currentQuestion.correctCountry + "\n";
                emailBody += "Points: 0\n\n";
            } else {
                emailBody += "Your answer: " + currentQuestion.selectedAnswer + "\n";
                emailBody += "Correct answer: " + currentQuestion.correctCountry + "\n";
                if (currentQuestion.selectedAnswer.equals(currentQuestion.correctCountry)) {
                    emailBody += "Points: 1\n\n";
                } else {
                    emailBody += "Points: 0\n\n";
                }
            }

        }
        return emailBody;
    }


    interface OnQuestionPressedListener {
        void onQuestionPressed(int i);
    }

    private OnQuestionPressedListener questionPressed;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        questionPressed = (OnQuestionPressedListener) context;
    }


    // RecyclerViewAdapter class
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ListFragmentAdapter> {

        private static final String TAG = "RecyclerViewAdapter";
        private ArrayList<Question> questionList;

        public RecyclerViewAdapter(ArrayList<Question> questionList) {
            this.questionList = questionList;
        }

        @NonNull
        @Override
        public ListFragmentAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Log.d(TAG, "onCreateViewHolder: called");
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
            ListFragmentAdapter listFragmentAdapter = new ListFragmentAdapter(view);
            return listFragmentAdapter;
        }

        @Override
        public void onBindViewHolder(@NonNull ListFragmentAdapter listFragmentAdapter, int i) {
            Log.d(TAG, "onBindViewHolder: called");
            Question currentQuestion = questionList.get(i);
            listFragmentAdapter.i = i;
            listFragmentAdapter.questionNumber.setText("Question #" + (i + 1));
            listFragmentAdapter.questionText.setText(currentQuestion.questionText);
            if (currentQuestion.answered){
                listFragmentAdapter.itemView.setBackground(getContext().getDrawable(R.drawable.blue_fill));
            } else {
                listFragmentAdapter.itemView.setBackground(getContext().getDrawable(R.drawable.border));
            }
            if (submitWasPressed){
                listFragmentAdapter.itemView.setClickable(false);
                if (!currentQuestion.answered){
                    listFragmentAdapter.itemView.setBackground(getContext().getDrawable(R.drawable.red_fill));
                } else if (currentQuestion.selectedAnswer.equals(currentQuestion.correctCountry)){
                    listFragmentAdapter.itemView.setBackground(getContext().getDrawable(R.drawable.green_fill));
                } else {
                    listFragmentAdapter.itemView.setBackground(getContext().getDrawable(R.drawable.red_fill));
                }
            }
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }


        // ListFragmentAdapter class
        public class ListFragmentAdapter extends RecyclerView.ViewHolder {

            private static final String TAG = "ListFragmentAdapter";
            TextView questionNumber;
            TextView questionText;
            int i;

            public ListFragmentAdapter(@NonNull View itemView) {
                super(itemView);
                questionNumber = itemView.findViewById(R.id.question_number);
                questionText = itemView.findViewById(R.id.question_text);
                itemView.setOnClickListener(v -> itemViewPressed());
            }

            private void itemViewPressed() {
                questionPressed.onQuestionPressed(i);
            }
        }
    }
}