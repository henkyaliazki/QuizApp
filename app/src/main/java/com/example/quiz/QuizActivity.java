package com.example.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QuizActivity extends AppCompatActivity {
    private JSONArray questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView;
    private Button option1Button, option2Button,
            option3Button, option4Button, endQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        endQuizButton = findViewById(R.id.endQuizButton);

        // Load questions dari file JSON
        questions = readQuestionsFromFile();

        // Load pertanyaan pertama
        loadNextQuestion();


        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button selectedButton = (Button) v;
                checkAnswer(selectedButton.getText().toString());
            }
        };

        option1Button.setOnClickListener(optionClickListener);
        option2Button.setOnClickListener(optionClickListener);
        option3Button.setOnClickListener(optionClickListener);
        option4Button.setOnClickListener(optionClickListener);

        endQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent
                        (QuizActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private JSONArray readQuestionsFromFile() {
        try {
            // Membaca file JSON dari folder assets
            InputStream is = getAssets().open("questions.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
    private void loadNextQuestion() {
        if (currentQuestionIndex < questions.length()) {
            try {
                JSONObject questionObject = questions.getJSONObject
                        (currentQuestionIndex);
                String question = questionObject.getString("question");
                JSONArray options = questionObject.getJSONArray("options");

                questionTextView.setText(question);
                option1Button.setText(options.getString(0));
                option2Button.setText(options.getString(1));
                option3Button.setText(options.getString(2));
                option4Button.setText(options.getString(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showFinalScore();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        try {
            JSONObject questionObject = questions.getJSONObject
                    (currentQuestionIndex);
            String correctAnswer = questionObject.getString("answer");

            if (selectedAnswer.equals(correctAnswer)) {
                score++;
            }

            currentQuestionIndex++;
            loadNextQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFinalScore() {
        questionTextView.setText("Quiz selesai! Skor Anda: " +
                score + " dari " + questions.length());
        option1Button.setVisibility(View.GONE);
        option2Button.setVisibility(View.GONE);
        option3Button.setVisibility(View.GONE);
        option4Button.setVisibility(View.GONE);
        endQuizButton.setVisibility(View.VISIBLE);
    }
}
