package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KetQuaActivity extends AppCompatActivity {

    // Khai báo View
    private TextView tvdiem;
    private TextView tvTiledung;
    private TextView tvThoigian;
    private Button btnLamlai;
    private Button btnThoat;

    // Các hằng số key để nhận/gửi dữ liệu
    public static final String EXTRA_CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String EXTRA_TOTAL_QUESTIONS = "TOTAL_QUESTIONS";
    public static final String EXTRA_TIME_SPENT = "TIME_SPENT";

    public static final String EXTRA_TOPIC = "TOPIC"; // Loại bài (Nghe/Đọc...)
    public static final String EXTRA_LEVEL = "LEVEL"; // Mức độ (Dễ/Trung bình...)
    public static final String EXTRA_EXERCISE_ID = "ID_BAI_TAP"; // ID bài tập (Cho API)

    // Biến lưu trữ dữ liệu để dùng cho nút "Làm lại"
    private String receivedTopic = "";
    private String receivedLevel = "";
    private int receivedExerciseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua);

        initViews();
        loadResults();
        setupListeners();
    }

    private void initViews() {
        tvdiem = findViewById(R.id.tvdiem);
        tvTiledung = findViewById(R.id.tvTiledung);
        tvThoigian = findViewById(R.id.tvThoigian);
        btnLamlai = findViewById(R.id.btnLamlai);
        btnThoat = findViewById(R.id.btnThoat);
    }

    private void loadResults() {
        Intent intent = getIntent();

        // 1. Nhận thông số kết quả (Điểm, Thời gian)
        int correctAnswers = intent.getIntExtra(EXTRA_CORRECT_ANSWERS, 0);
        int totalQuestions = intent.getIntExtra(EXTRA_TOTAL_QUESTIONS, 1); // Mặc định 1 để tránh chia cho 0
        int timeSpent = intent.getIntExtra(EXTRA_TIME_SPENT, 0);

        // 2. Nhận thông tin bài tập để phục vụ nút "Làm lại"
        receivedTopic = intent.getStringExtra(EXTRA_TOPIC);
        receivedLevel = intent.getStringExtra(EXTRA_LEVEL);
        receivedExerciseId = intent.getIntExtra(EXTRA_EXERCISE_ID, -1);

        // 3. Hiển thị lên giao diện
        tvdiem.setText(correctAnswers + "/" + totalQuestions);

        // Tính phần trăm
        int accuracy = 0;
        if (totalQuestions > 0) {
            accuracy = (int) (((float) correctAnswers / totalQuestions) * 100);
        }
        tvTiledung.setText(accuracy + "%");

        // Hiển thị thời gian
        tvThoigian.setText(formatTime(timeSpent));
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void setupListeners() {
        // --- XỬ LÝ NÚT LÀM LẠI ---
        btnLamlai.setOnClickListener(v -> {
            Intent intent = null;

            // Kiểm tra Topic để điều hướng về đúng màn hình
            if (receivedTopic != null) {
                switch (receivedTopic) {
                    // --- NHÓM ĐÃ CÓ API (Cần ID_BAI_TAP) ---
                    case "Nghe":
                        if (receivedExerciseId != -1) {
                            intent = new Intent(KetQuaActivity.this, BaiTapNgheActivity.class);
                            intent.putExtra("ID_BAI_TAP", receivedExerciseId);
                            intent.putExtra("MUC_DO", receivedLevel);
                        } else {
                            Toast.makeText(this, "Lỗi: Không tìm thấy bài tập Nghe!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Đọc":
                        if (receivedExerciseId != -1) {
                            intent = new Intent(KetQuaActivity.this, BaiTapDocActivity.class);
                            intent.putExtra("ID_BAI_TAP", receivedExerciseId);
                            intent.putExtra("MUC_DO", receivedLevel);
                        } else {
                            Toast.makeText(this, "Lỗi: Không tìm thấy bài tập Đọc!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    // --- NHÓM CHƯA CÓ API HOÀN CHỈNH (Nói & Viết) ---
                    // Giữ nguyên logic cũ hoặc báo đang phát triển
                    case "Nói":
                        // Nếu bạn muốn mở lại activity cũ (giả sử BaiTapNoiActivity dùng logic cũ)
                         intent = new Intent(KetQuaActivity.this, BaiTapNoiActivity.class);
                         intent.putExtra("SELECTED_LEVEL", receivedLevel);

                        // Hoặc thông báo chưa làm xong:
//                        Toast.makeText(this, "Chức năng Nói đang được cập nhật API", Toast.LENGTH_SHORT).show();
                        return;

                    case "Viết":
                        // Tương tự cho Viết
                         intent = new Intent(KetQuaActivity.this, BaiTapVietActivity.class);
                         intent.putExtra("SELECTED_LEVEL", receivedLevel);

//                        Toast.makeText(this, "Chức năng Viết đang được cập nhật API", Toast.LENGTH_SHORT).show();
                        return;

                    default:
                        Toast.makeText(this, "Không xác định được loại bài tập!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            // Nếu Intent đã được tạo thành công (cho Nghe/Đọc), thì start activity
            if (intent != null) {
                // Có thể gửi thêm Topic nếu activity đích cần hiển thị tiêu đề
                intent.putExtra("TOPIC", receivedTopic);

                startActivity(intent);
                finish(); // Đóng màn hình kết quả để quay lại bài làm mới
            }
        });

        // --- XỬ LÝ NÚT THOÁT ---
        btnThoat.setOnClickListener(v -> {
            // Đóng Activity này -> Quay về màn hình cha (Fragment Kiểm tra hoặc Danh sách bài tập)
            finish();
        });
    }
}