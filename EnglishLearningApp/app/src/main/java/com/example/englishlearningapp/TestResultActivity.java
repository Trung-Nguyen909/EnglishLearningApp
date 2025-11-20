package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Hoặc android.widget.AppCompatButton
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TestResultActivity extends AppCompatActivity {

    // Khai báo các View
    private TextView tvdiem;
    private TextView tvTiledung;
    private TextView tvThoigian;
    private Button btnLamlai;
    private Button btnThoat;

    // Constants cho Intent extras (Dữ liệu nhận vào)
    public static final String EXTRA_CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String EXTRA_TOTAL_QUESTIONS = "TOTAL_QUESTIONS";
    public static final String EXTRA_TIME_SPENT = "TIME_SPENT";

    // >>> THÊM 2 HẰNG SỐ NÀY ĐỂ NHẬN BIẾT BÀI TẬP NÀO <<<
    public static final String EXTRA_TOPIC = "TOPIC"; // Ví dụ: "Listening"
    public static final String EXTRA_LEVEL = "LEVEL"; // Ví dụ: "Basic"

    // Biến lưu trữ để dùng cho nút Làm lại
    private String receivedTopic = "";
    private String receivedLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        initViews();
        loadResults();
        setupListeners();
    }

    private void initViews() {
        // Đảm bảo ID trong file XML activity_test_result khớp với các dòng này
        tvdiem = findViewById(R.id.tvdiem);
        tvTiledung = findViewById(R.id.tvTiledung);
        tvThoigian = findViewById(R.id.tvThoigian);
        btnLamlai = findViewById(R.id.btnLamlai);
        btnThoat = findViewById(R.id.btnThoat);
    }

    private void loadResults() {
        Intent intent = getIntent();

        // 1. Nhận điểm số, thời gian
        int correctAnswers = intent.getIntExtra(EXTRA_CORRECT_ANSWERS, 0);
        int totalQuestions = intent.getIntExtra(EXTRA_TOTAL_QUESTIONS, 1); // Mặc định là 1 để tránh chia cho 0
        int timeSpent = intent.getIntExtra(EXTRA_TIME_SPENT, 0);

        // 2. >>> QUAN TRỌNG: NHẬN TÊN KỸ NĂNG VÀ LEVEL ĐỂ LÀM LẠI <<<
        receivedTopic = intent.getStringExtra(EXTRA_TOPIC);
        receivedLevel = intent.getStringExtra(EXTRA_LEVEL);

        // 3. Hiển thị lên giao diện
        tvdiem.setText(correctAnswers + "/" + totalQuestions);

        int accuracy = 0;
        if (totalQuestions > 0) {
            accuracy = (correctAnswers * 100) / totalQuestions;
        }
        tvTiledung.setText(accuracy + "%");

        tvThoigian.setText(formatTime(timeSpent));
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void setupListeners() {
        // --- XỬ LÝ NÚT LÀM LẠI ---
        btnLamlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                // Kiểm tra chủ đề cũ là gì để mở lại đúng Activity đó
                // Lưu ý: Các chuỗi này phải khớp với chuỗi bạn gửi từ Kiemtra_Fragment
                if (receivedTopic != null) {
                    switch (receivedTopic) {
                        case "Listening":
                            intent = new Intent(TestResultActivity.this, BaiTapNgheActivity.class);
                            break;
                        case "Speaking":
                            intent = new Intent(TestResultActivity.this, SpeakingTestActivity.class);
                            break;
                        case "Writing":
                            intent = new Intent(TestResultActivity.this, WritingTestActivity.class);
                            break;
                        case "Reading":
                        default:
                            intent = new Intent(TestResultActivity.this, BaiTapActivity.class);
                            break;
                    }
                } else {
                    // Fallback nếu không nhận được topic (Mặc định về bài đọc)
                    intent = new Intent(TestResultActivity.this, BaiTapActivity.class);
                }

                if (intent != null) {
                    // Gửi lại Level và Topic để trang bài tập biết tải nội dung gì
                    intent.putExtra("SELECTED_LEVEL", receivedLevel);
                    intent.putExtra("SELECTED_TOPIC", receivedTopic);

                    startActivity(intent);
                    finish(); // Đóng trang kết quả hiện tại
                }
            }
        });

        // --- XỬ LÝ NÚT THOÁT ---
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi bấm Thoát, chỉ cần đóng trang Kết quả.
                // Vì trước đó (ở trang Bài tập), bạn đã gọi finish() rồi,
                // nên bên dưới trang Kết quả chính là MainActivity (Màn hình Kiểm tra).
                finish();
            }
        });
    }
}