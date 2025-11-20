package com.example.englishlearningapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class SpeakingTestActivity extends AppCompatActivity {

    // --- KHAI BÁO VIEW ---
    private ImageButton btnBack, btnRecordMic;
    private TextView tvTimer, tvRecordingStatus, tvRecordingTimer;
    private View viewWaveformPlaceholder;
    private LinearLayout layoutPostRecordingActions;
    private AppCompatButton btnPlayback, btnReRecord, btnNext;
    private ProgressBar progressBar;

    // --- BIẾN LOGIC ---
    private CountDownTimer mainTestTimer; // Timer tổng bài thi
    private CountDownTimer recordingTimer; // Timer đếm thời gian ghi âm
    private boolean isRecording = false;
    private boolean hasRecorded = false;

    // Thời gian giới hạn ghi âm (30 giây)
    private final long MAX_RECORDING_DURATION = 30000;
    private long timeRecorded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speaking_test);

        // Xử lý EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        startMainTimer(); // Bắt đầu đếm giờ bài thi ngay
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTimer = findViewById(R.id.tv_timer);
        btnRecordMic = findViewById(R.id.btn_record_mic);
        tvRecordingStatus = findViewById(R.id.tv_recording_status);
        tvRecordingTimer = findViewById(R.id.tv_recording_timer);
        viewWaveformPlaceholder = findViewById(R.id.view_waveform_placeholder);
        layoutPostRecordingActions = findViewById(R.id.layout_post_recording_actions);
        btnPlayback = findViewById(R.id.btn_playback);
        btnReRecord = findViewById(R.id.btn_re_record);
        btnNext = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progress_bar);

        // Trạng thái ban đầu
        btnNext.setEnabled(false);
        if (progressBar != null) progressBar.setProgress(50);
    }

    private void startMainTimer() {
        // Ví dụ đếm ngược 45 giây cho câu hỏi này
        mainTestTimer = new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                if (isRecording) stopRecording(); // Hết giờ thì tự dừng ghi âm
                Toast.makeText(SpeakingTestActivity.this, "Hết thời gian làm bài!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // SỰ KIỆN NÚT MICRO CHÍNH
        btnRecordMic.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
            } else {
                stopRecording();
            }
        });

//        // SỰ KIỆN GHI LẠI
//        btnReRecord.setOnClickListener(v -> {
//            resetUIForNewRecording();
//        });

        // SỰ KIỆN NGHE LẠI
        btnPlayback.setOnClickListener(v -> {
            Toast.makeText(this, "Đang phát lại đoạn ghi âm...", Toast.LENGTH_SHORT).show();
            // TODO: Thêm code MediaPlayer phát file vừa ghi tại đây
        });

        // SỰ KIỆN CÂU TIẾP THEO
        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Đã nộp bài nói!", Toast.LENGTH_SHORT).show();
            // TODO: Chuyển sang câu tiếp theo
        });
    }

    // --- LOGIC GHI ÂM ---

    private void startRecording() {
        isRecording = true;

        // 1. Đổi giao diện sang ĐANG GHI (Màu Đỏ)
        btnRecordMic.setImageResource(android.R.drawable.ic_media_pause); // Đổi icon thành Pause hoặc Stop
        btnRecordMic.setBackgroundTintList(ColorStateList.valueOf(Color.RED)); // Nền đỏ cảnh báo

        tvRecordingStatus.setText("Đang ghi âm...");
        tvRecordingStatus.setTextColor(Color.RED);

        // 2. Hiện Timer ghi âm và Sóng âm
        tvRecordingTimer.setVisibility(View.VISIBLE);
        viewWaveformPlaceholder.setVisibility(View.VISIBLE);

        // 3. Ẩn các nút hành động cũ (nếu có)
        layoutPostRecordingActions.setVisibility(View.GONE);
        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));

        // TODO: Khởi tạo MediaRecorder và gọi recorder.start() tại đây

        // Bắt đầu đếm giờ ghi âm (00:00 -> 00:30)
//        startRecordingTimer();
    }

    private void stopRecording() {
        isRecording = false;
        hasRecorded = true;

        // Dừng timer ghi âm
        if (recordingTimer != null) recordingTimer.cancel();

        // TODO: Gọi recorder.stop() và recorder.release() tại đây

        // 1. Đổi giao diện sang HOÀN TẤT (Màu Cam gốc hoặc Xanh)
        btnRecordMic.setImageResource(android.R.drawable.ic_btn_speak_now); // Trả lại icon mic
        btnRecordMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9800"))); // Trả lại màu cam

        tvRecordingStatus.setText("Ghi âm hoàn tất!");
        tvRecordingStatus.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh lá báo thành công

        // 2. Ẩn sóng âm (hoặc giữ lại tùy ý), giữ Timer để user biết mình nói bao lâu
        viewWaveformPlaceholder.setVisibility(View.GONE);
    }
}