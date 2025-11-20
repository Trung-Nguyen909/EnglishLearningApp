package com.example.englishlearningapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaiTapNgheActivity extends AppCompatActivity implements CauHoiAdapter.OnAnswerSelectedListener {

    private List<CauHoi> questions;
    private Button completeButton;
    private ProgressBar mainProgressBar;
    private TextView questionCountText;
    private TextView progressPercentText;
    private Set<Integer> answeredQuestionIds = new HashSet<>();

    private ImageButton btnPlayAudio;
    private SeekBar seekbarAudio;
    private TextView tvAudioDuration;
    private MediaPlayer mediaPlayer;
    private Handler audioHandler = new Handler();
    private boolean isPlaying = false;

    // Biến lưu Level hiện tại để gửi sang trang kết quả (phục vụ nút Làm lại)
    private String currentLevel = "Basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_nghe);

        // 1. NHẬN DỮ LIỆU LEVEL TỪ MÀN HÌNH TRƯỚC
        if (getIntent() != null) {
            String level = getIntent().getStringExtra("SELECTED_LEVEL");
            if (level != null) {
                currentLevel = level;
            }
        }

        initViews();

        questions = createListeningQuestions();

        setupQuestionsRecyclerView();
        setupAudioPlayer();

        mainProgressBar.setMax(questions.size());
        updateProgressState();

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        // --- XỬ LÝ NÚT HOÀN THÀNH ---
        completeButton.setOnClickListener(v -> {
            // Chuyển sang trang Kết quả
            chuyenSangTrangKetQua();
        });
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapNgheActivity.this, TestResultActivity.class);

        // Truyền số câu đã làm (coi như là điểm số hoàn thành)
        intent.putExtra(TestResultActivity.EXTRA_CORRECT_ANSWERS, answeredQuestionIds.size());
        intent.putExtra(TestResultActivity.EXTRA_TOTAL_QUESTIONS, questions.size());
        intent.putExtra(TestResultActivity.EXTRA_TIME_SPENT, 0); // Thời gian (tùy chọn)

        // QUAN TRỌNG: Gửi Topic và Level để nút "Làm lại" ở trang kia biết quay về đây
        intent.putExtra(TestResultActivity.EXTRA_TOPIC, "Listening");
        intent.putExtra(TestResultActivity.EXTRA_LEVEL, currentLevel);

        startActivity(intent);
        finish(); // Đóng màn hình hiện tại để khi bấm Back không quay lại đây
    }

    private void initViews() {
        completeButton = findViewById(R.id.complete_button);
        mainProgressBar = findViewById(R.id.main_progress_bar);
        questionCountText = findViewById(R.id.question_count_text);
        progressPercentText = findViewById(R.id.progress_percent_text);

        btnPlayAudio = findViewById(R.id.btn_play_audio);
        seekbarAudio = findViewById(R.id.seekbar_audio);
        tvAudioDuration = findViewById(R.id.tv_audio_duration);
    }

    private void setupQuestionsRecyclerView() {
        RecyclerView questionsRecyclerView = findViewById(R.id.questions_recycler_view_nghe);
        CauHoiAdapter adapter = new CauHoiAdapter(questions, this);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(adapter);
    }

    // --- PHẦN XỬ LÝ AUDIO (GIỮ NGUYÊN) ---
    private void setupAudioPlayer() {
        try {
            // Đảm bảo file audio_sample.mp3 có trong res/raw
            mediaPlayer = MediaPlayer.create(this, R.raw.audio_sample);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (mediaPlayer != null) {
            seekbarAudio.setMax(mediaPlayer.getDuration());
            tvAudioDuration.setText(formatTime(mediaPlayer.getDuration()));

            btnPlayAudio.setOnClickListener(v -> {
                if (isPlaying) pauseAudio();
                else playAudio();
            });

            seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                        tvAudioDuration.setText(formatTime(progress));
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                pauseAudio();
                mediaPlayer.seekTo(0);
                seekbarAudio.setProgress(0);
                tvAudioDuration.setText(formatTime(mediaPlayer.getDuration()));
            });
        }
    }

    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            btnPlayAudio.setImageResource(android.R.drawable.ic_media_pause);
            updateSeekBar();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            btnPlayAudio.setImageResource(android.R.drawable.ic_media_play);
            audioHandler.removeCallbacks(updateSeekBarRunnable);
        }
    }

    private Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPlaying) {
                int currentPos = mediaPlayer.getCurrentPosition();
                seekbarAudio.setProgress(currentPos);
                tvAudioDuration.setText(formatTime(currentPos));
                audioHandler.postDelayed(this, 100);
            }
        }
    };

    private void updateSeekBar() {
        audioHandler.postDelayed(updateSeekBarRunnable, 0);
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // --- LOGIC CẬP NHẬT TIẾN ĐỘ ---
    @Override
    public void onAnswerSelected(int questionId, String selectedAnswer) {
        // Chỉ cần biết là đã chọn, không cần check đúng sai
        if (selectedAnswer != null) {
            answeredQuestionIds.add(questionId);
        } else {
            answeredQuestionIds.remove(questionId);
        }
        updateProgressState();
    }

    private void updateProgressState() {
        int answeredCount = answeredQuestionIds.size();
        int total = questions.size();
        int progressPercent = (total > 0) ? (int) (((float) answeredCount / total) * 100) : 0;

        questionCountText.setText("Câu " + answeredCount + "/" + total);
        progressPercentText.setText(progressPercent + "%");
        mainProgressBar.setProgress(answeredCount);

        // Hiện nút hoàn thành khi làm hết (hoặc luôn hiện tùy bạn)
        if (answeredCount == total) {
            completeButton.setEnabled(true);
            completeButton.setVisibility(View.VISIBLE);
            completeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.royalBlue));
        } else {
            completeButton.setEnabled(false);
            completeButton.setVisibility(View.VISIBLE);
            completeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
        }
    }

    private List<CauHoi> createListeningQuestions() {
        return Arrays.asList(
                new CauHoi(1, "Instruction: Listen and choose.", "Where are they going?", Arrays.asList("Cinema", "Museum", "Park", "School"), "Museum"),
                new CauHoi(2, "Instruction: Listen to detail.", "What time is it?", Arrays.asList("7:00", "7:30", "8:00", "9:00"), "7:30"),
                new CauHoi(3, "Instruction: Inferring.", "How does the man feel?", Arrays.asList("Happy", "Sad", "Angry", "Tired"), "Happy")
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            audioHandler.removeCallbacks(updateSeekBarRunnable);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}