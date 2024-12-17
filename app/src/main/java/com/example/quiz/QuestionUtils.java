package com.example.quiz;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class QuestionUtils {
    static final String FILE_NAME = "questions.json";

    // Membaca pertanyaan dari file internal storage
    public static JSONArray readQuestionsFromFile(Context context) {
        try {
            FileInputStream inputStream = context.openFileInput(FILE_NAME);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            // Jika file tidak ditemukan, kembalikan array kosong
            return new JSONArray();
        }
    }

    // Menyimpan pertanyaan ke file internal storage
    public static void saveQuestionsToFile(Context context, JSONArray questionArray) {
        try {
            FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(questionArray.toString().getBytes());
            outputStream.close();
            Toast.makeText(context, "Pertanyaan berhasil disimpan!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Gagal menyimpan pertanyaan ke file JSON.", Toast.LENGTH_SHORT).show();
        }
    }

    // Memastikan file JSON awal tersedia di internal storage
    public static void initializeQuestionsFile(Context context) {
        try {
            FileInputStream inputStream = context.openFileInput(FILE_NAME);
            inputStream.close(); // Jika file ada, langsung keluar
        } catch (Exception e) {
            try {
                // Jika file tidak ada, salin dari assets
                FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                byte[] buffer = readFromAssets(context);
                if (buffer != null) {
                    outputStream.write(buffer);
                }
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Membaca file JSON dari folder assets jika diperlukan
    private static byte[] readFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open(FILE_NAME);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
