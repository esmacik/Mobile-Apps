package com.techexchange.mobileapps.recyclerdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    static final String KEY_RESTART_QUIZ = "RetakeQuiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("High Score", 0);
        SharedPreferences.Editor editor = pref.edit();

        TextView scoreText = findViewById(R.id.score_text);
        int score = getIntent().getIntExtra("score", 0);
        scoreText.setText("Quiz Score: " + score);

        Button againButton = findViewById(R.id.again_button);
        againButton.setOnClickListener(v -> onAgainButtonPressed());

        Button emailButtom = findViewById(R.id.email_button);
        emailButtom.setOnClickListener(v -> onEmailButtonPressed());

        if (score > pref.getInt("high_score", -1)){
            editor.putInt("high_score", score);
            editor.commit();
            Toast.makeText(this, "New High Score!"+score, Toast.LENGTH_SHORT).show();
        }
    }

    private void onAgainButtonPressed(){
        MainActivity.resetView();
        Intent intent = new Intent();
        intent.putExtra(KEY_RESTART_QUIZ, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onEmailButtonPressed(){
        int highScore = getIntent().getIntExtra("score", -1);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Score");
        intent.putExtra(Intent.EXTRA_TEXT, "My high score is " + highScore);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
