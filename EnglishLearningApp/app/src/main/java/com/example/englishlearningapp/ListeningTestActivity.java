package com.example.englishlearningapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ListeningTestActivity extends AppCompatActivity {

    // Khai báo biến UI
    private ImageButton btnBack;
    private TextView tvTimer, tvProgressText;
    private ProgressBar progressBar;
    private Button btnPlayAudio, btnNext;
    private RadioGroup radioGroupAnswers;

    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private boolean isPlaying = false;
    private long timeLeftInMillis = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listening_test);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        // 2. Cài đặt Media Player (Âm thanh)
//        setupMediaPlayer();
        // 3. Cài đặt Timer (Đếm ngược)
        startTimer();

        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTimer = findViewById(R.id.tv_timer);
        // tvProgressText = findViewById(R.id.tv_progress_text);
        progressBar = findViewById(R.id.progress_bar);
        btnPlayAudio = findViewById(R.id.btn_play_audio);
        radioGroupAnswers = findViewById(R.id.radio_group_answers);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setEnabled(false);
    }

//    private void setupMediaPlayer() {
//        // Thay R.raw.audio_sample bằng tên file mp3 thực tế của bạn
//        // Nếu chưa có file, hãy comment dòng này lại để tránh lỗi
//        try {
//            mediaPlayer = MediaPlayer.create(this, R.raw.audio_sample);
//
//            // Khi phát xong thì đổi nút về trạng thái "Play"
//            mediaPlayer.setOnCompletionListener(mp -> {
//                isPlaying = false;
//                btnPlayAudio.setText("Play Audio");
//                // Đổi icon về Play (nếu có set icon trong code)
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Không tìm thấy file âm thanh!", Toast.LENGTH_SHORT).show();
//        }
//    }

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
                Toast.makeText(ListeningTestActivity.this, "Hết giờ!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void setupEvents() {
        // Nút Quay lại
        btnBack.setOnClickListener(v -> {
            finish(); // Đóng màn hình này
        });

        // Nút Play/Pause Audio
        btnPlayAudio.setOnClickListener(v -> {
            if (mediaPlayer == null) return;

            if (isPlaying) {
                mediaPlayer.pause();
                btnPlayAudio.setText("Play Audio");
                // Nếu muốn đổi icon: btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
            } else {
                mediaPlayer.start();
                btnPlayAudio.setText("Pause Audio");
                // Nếu muốn đổi icon: btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);
            }
            isPlaying = !isPlaying;
        });

        // Sự kiện chọn đáp án (RadioGroup)
        radioGroupAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
            btnNext.setTextColor(Color.WHITE);

            // 2. Kích hoạt nút
            btnNext.setEnabled(true);
        });

        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Đã lưu câu trả lời!", Toast.LENGTH_SHORT).show();

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
                btnPlayAudio.setText("Play Audio");
            }
        });
    }

    // QUAN TRỌNG: Giải phóng tài nguyên khi thoát màn hình
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Dừng Timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}