package com.example.englishlearningapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.SpeakingQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaiTapNoiActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTimer, tvQuestionCounter, tvProgressPercentage;
    private ProgressBar progressBar;
    private RecyclerView rvSpeakingQuestions;
    private Button btnHoanThanh;

    // Logic Variables
    private SpeakingAdapter adapter;
    private List<SpeakingQuestion> questionList;
    private TextToSpeech textToSpeech;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_noi);

        initViews();
        createDummyData();
        setupAdapter();
        setupTextToSpeech();

        updateProgress();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTimer = findViewById(R.id.tv_timer);
        tvQuestionCounter = findViewById(R.id.tv_question_counter);
        tvProgressPercentage = findViewById(R.id.tv_progress_percentage);
        progressBar = findViewById(R.id.progress_bar);
        rvSpeakingQuestions = findViewById(R.id.rv_speaking_questions);
        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);

        rvSpeakingQuestions.setLayoutManager(new LinearLayoutManager(this));

        tvTimer.setText("00:20");

        btnBack.setOnClickListener(v -> finish());

        btnHoanThanh.setOnClickListener(v -> {
            chuyenSangTrangKetQua();
        });
    }

    private void createDummyData() {
        questionList = new ArrayList<>();
        questionList.add(new SpeakingQuestion("Good morning"));
        questionList.add(new SpeakingQuestion("How are you?"));
        questionList.add(new SpeakingQuestion("I like learning English"));
        questionList.add(new SpeakingQuestion("What is your name?"));
        questionList.add(new SpeakingQuestion("Nice to meet you"));
    }

    private void setupAdapter() {
        adapter = new SpeakingAdapter(this, questionList, new SpeakingAdapter.OnItemClickListener() {
            @Override
            public void onRecordClick(int position) {
                simulateRecording(position);
            }

            @Override
            public void onListenClick(String text) {
                speakText(text);
            }
        });
        rvSpeakingQuestions.setAdapter(adapter);
    }

    // --- HÀM GIẢ LẬP GHI ÂM 3 GIÂY ---
    private void simulateRecording(int position) {
        Toast.makeText(this, "Đang ghi âm...", Toast.LENGTH_SHORT).show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                completeQuestion(position);
            }
        }, 3000);
    }

    private void completeQuestion(int position) {
        if (position >= 0 && position < questionList.size()) {
            SpeakingQuestion question = questionList.get(position);

            question.setUserAnswer(question.getSentence());
            question.setCorrect(true);

            adapter.notifyItemChanged(position);

            updateProgress();

            Toast.makeText(this, "Đã xác nhận đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProgress() {
        int total = questionList.size();
        int completed = 0;
        for (SpeakingQuestion q : questionList) {
            if (q.isCorrect()) {
                completed++;
            }
        }

        // Cập nhật ProgressBar
        int percent = (total > 0) ? (int) (((float) completed / total) * 100) : 0;
        progressBar.setProgress(percent);
        tvProgressPercentage.setText(percent + "%");
        tvQuestionCounter.setText("Câu " + completed + "/" + total + " hoàn thành");

        // Logic nút Hoàn thành
        if (completed == total) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        }
    }

    // --- MÁY ĐỌC (TTS) ---
    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    private void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        // Xóa các luồng đếm giờ nếu thoát app đột ngột
        handler.removeCallbacksAndMessages(null);
    }
    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapNoiActivity.this, TestResultActivity.class);

        // Tính số câu đúng (Dựa trên thuộc tính isCorrect của SpeakingQuestion)
        int correctCount = 0;
        for (SpeakingQuestion q : questionList) {
            if (q.isCorrect()) {
                correctCount++;
            }
        }

        // Truyền dữ liệu sang màn hình kết quả
        // Lưu ý: Đảm bảo các biến static EXTRA_... bên TestResultActivity là public
        intent.putExtra(TestResultActivity.EXTRA_CORRECT_ANSWERS, correctCount);
        intent.putExtra(TestResultActivity.EXTRA_TOTAL_QUESTIONS, questionList.size());
        intent.putExtra(TestResultActivity.EXTRA_TIME_SPENT, 0); // Thời gian tạm để 0
        intent.putExtra(TestResultActivity.EXTRA_TOPIC, "Speaking"); // Đổi chủ đề thành Speaking

        // Lấy level từ Intent cũ chuyển sang (nếu có)
        String level = getIntent().getStringExtra("SELECTED_LEVEL");
        intent.putExtra(TestResultActivity.EXTRA_LEVEL, level != null ? level : "Basic");

        startActivity(intent);
        finish();
    }
}