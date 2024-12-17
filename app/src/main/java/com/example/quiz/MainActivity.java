package com.example.quiz;

import static com.example.quiz.QuestionUtils.FILE_NAME;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Button startQuizButton, manageQuestionsButton;
    private TextView welcomeText;
    private JSONArray questionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welcomeText);
        startQuizButton = findViewById(R.id.startQuizButton);
        manageQuestionsButton = findViewById(R.id.manageQuestionsButton);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcomeText.startAnimation(fadeIn);

        // Salin file JSON dari assets ke direktori aplikasi jika belum ada
        copyJsonFileIfNeeded();

        // Memuat pertanyaan dari file JSON
        questionArray = QuestionUtils.readQuestionsFromFile(this);

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionArray.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("questions", questionArray.toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Tidak ada pertanyaan! Tambahkan dulu.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tombol Manage Questions
        manageQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageQuestionsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionArray = QuestionUtils.readQuestionsFromFile(this);
    }

    private void copyJsonFileIfNeeded() {
        try {
            // Cek apakah file sudah ada di direktori aplikasi
            InputStream inputStream = openFileInput(FILE_NAME);
            inputStream.close();
        } catch (Exception e) {
            // Jika belum ada, salin dari assets
            try {
                InputStream is = getAssets().open("questions.json");
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();

                FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(buffer);
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
