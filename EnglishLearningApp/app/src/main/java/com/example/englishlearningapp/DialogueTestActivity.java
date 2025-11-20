package com.example.englishlearningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class DialogueTestActivity extends AppCompatActivity {

    // 1. Khai báo các biến giao diện
    private ImageButton btnBack;
    private TextView tvTimer;
    private RadioGroup radioGroupAnswers;
    private AppCompatButton btnFinish;
    private ProgressBar progressBar;

    // 2. Khai báo biến logic
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dialogue_test);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        startTimer();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTimer = findViewById(R.id.tv_timer);
        radioGroupAnswers = findViewById(R.id.radio_group_answers);
        btnFinish = findViewById(R.id.btn_finish);
        progressBar = findViewById(R.id.progress_bar);

        btnFinish.setEnabled(false);

        if (progressBar != null) {
            progressBar.setProgress(100);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                // Xử lý khi hết giờ (ví dụ: tự động nộp bài)
                finishTest();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    @SuppressLint("RestrictedApi")
    private void setupEvents() {
        // Nút Quay lại
        btnBack.setOnClickListener(v -> {
            finish();
        });

        radioGroupAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            // Khi người dùng chọn một RadioButton bất kỳ
            // Đổi màu nút Hoàn thành sang Xanh (Active)
            btnFinish.setBackgroundResource(R.drawable.bg_button_play); // Hoặc drawable màu xanh của bạn
            // Hoặc dùng setBackgroundTintList nếu dùng AppCompatButton và backgroundTint
            btnFinish.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));

            btnFinish.setTextColor(Color.WHITE);
            btnFinish.setEnabled(true);
        });

        // Nút Hoàn thành
        btnFinish.setOnClickListener(v -> finishTest());
    }

    private void finishTest() {
        // Dừng timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Logic nộp bài / Tính điểm / Chuyển sang màn hình kết quả
        Toast.makeText(this, "Đã nộp bài thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}