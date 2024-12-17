package com.example.quiz;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManageQuestionsActivity extends AppCompatActivity {
    private EditText questionInput, option1Input, option2Input, option3Input, option4Input, answerInput;
    private Button addQuestionButton, deleteQuestionButton, backToMainButton;
    private JSONArray questionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);

        questionInput = findViewById(R.id.questionInput);
        option1Input = findViewById(R.id.optionAInput);
        option2Input = findViewById(R.id.optionBInput);
        option3Input = findViewById(R.id.optionCInput);
        option4Input = findViewById(R.id.optionDInput);
        answerInput = findViewById(R.id.correctAnswerInput);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        deleteQuestionButton = findViewById(R.id.removeQuestionButton);
        backToMainButton = findViewById(R.id.backToMainButton);

        questionArray = QuestionUtils.readQuestionsFromFile(this);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject newQuestion = new JSONObject();
                    newQuestion.put("question", questionInput.getText().toString());
                    JSONArray options = new JSONArray();
                    options.put(option1Input.getText().toString());
                    options.put(option2Input.getText().toString());
                    options.put(option3Input.getText().toString());
                    options.put(option4Input.getText().toString());
                    newQuestion.put("options", options);
                    newQuestion.put("answer", answerInput.getText().toString());
                    questionArray.put(newQuestion);
                    QuestionUtils.saveQuestionsToFile(ManageQuestionsActivity.this, questionArray);
                    Toast.makeText(ManageQuestionsActivity.this, "Pertanyaan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ManageQuestionsActivity.this, "Gagal menambahkan pertanyaan.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionArray.length() > 0) {
                    questionArray.remove(questionArray.length() - 1);
                    QuestionUtils.saveQuestionsToFile(ManageQuestionsActivity.this, questionArray);
                    Toast.makeText(ManageQuestionsActivity.this, "Pertanyaan terakhir dihapus!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageQuestionsActivity.this, "Tidak ada pertanyaan untuk dihapus.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Tombol Kembali
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
