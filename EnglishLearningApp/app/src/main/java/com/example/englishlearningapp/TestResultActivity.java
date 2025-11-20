package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TestResultActivity extends AppCompatActivity {

    // Khai báo các View
    private TextView tvScore;
    private TextView tvAccuracy;
    private TextView tvTime;
    private Button btnRetry;

    public static final String EXTRA_CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String EXTRA_TOTAL_QUESTIONS = "TOTAL_QUESTIONS";
    public static final String EXTRA_TIME_SPENT = "TIME_SPENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        initViews();
        loadResults();
        setupListeners();
    }

    private void initViews() {
        tvScore = findViewById(R.id.tvScore);
        tvAccuracy = findViewById(R.id.tvAccuracy);
        tvTime = findViewById(R.id.tvTime);
        btnRetry = findViewById(R.id.btnRetry);
    }

    private void loadResults() {
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra(EXTRA_CORRECT_ANSWERS, 0);
        int totalQuestions = intent.getIntExtra(EXTRA_TOTAL_QUESTIONS, 1);
        int timeSpent = intent.getIntExtra(EXTRA_TIME_SPENT, 1); // thời gian tính bằng giây

        // Hiển thị điểm số
        tvScore.setText(correctAnswers + "/" + totalQuestions);

        // Tính và hiển thị tỷ lệ đúng
        int accuracy = 0;
        if (totalQuestions > 0) {
            accuracy = (correctAnswers * 100) / totalQuestions;
        }
        tvAccuracy.setText(accuracy + "%");

        // Hiển thị thời gian
        tvTime.setText(formatTime(timeSpent));
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void setupListeners() {
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình test hoặc trang chủ
                finish();
            }
        });
    }
}
