package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ExerciseDetailActivity extends AppCompatActivity {

    // Khai báo các biến giao diện (View)
    private TextView tvLoaiBai, tvTieuDe, tvCapDo, tvThoiGian;
    private TextView tvGioiThieu, tvDiemChinh1, tvDiemChinh2, tvViDuTu, tvViDuCau;
    private MaterialButton btnLamBaiTap;
    private ImageView btnBack; // Nút quay lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        // 1. Ánh xạ các View (Kết nối code với giao diện XML)
        anhXaView();

        // 2. Xử lý nút Back (Quay lại)
        // Cách của bạn dùng topBar.getChildAt(0) cũng được, nhưng mình dùng ID trực tiếp cho an toàn
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            // Fallback nếu bạn muốn dùng cách cũ
            LinearLayout topBar = findViewById(R.id.top_bar); // Đảm bảo ID này đúng trong XML
            if(topBar != null && topBar.getChildCount() > 0) {
                topBar.getChildAt(0).setOnClickListener(v -> finish());
            }
        }

        // 3. Xử lý nút "Làm bài tập"
        if (btnLamBaiTap != null) {
            btnLamBaiTap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ExerciseDetailActivity.this, "Bắt đầu làm bài...", Toast.LENGTH_SHORT).show();
                    // Sau này bạn có thể Intent sang trang làm bài trắc nghiệm ở đây
                }
            });
        }

        // 4. NHẬN DỮ LIỆU TỪ INTENT (Từ trang danh sách gửi sang)
        Intent intent = getIntent();
        if (intent != null) {
            String tieuDe = intent.getStringExtra("TITLE");
            String loaiBai = intent.getStringExtra("TYPE");
            String capDo = intent.getStringExtra("LEVEL");
            String thoiGian = intent.getStringExtra("TIME");

            // 5. Hiển thị thông tin cơ bản lên Header
            if (tvTieuDe != null) tvTieuDe.setText(tieuDe);
            if (tvLoaiBai != null) tvLoaiBai.setText(loaiBai);
            if (tvCapDo != null) tvCapDo.setText(capDo);
            if (tvThoiGian != null) tvThoiGian.setText(thoiGian);

            // 6. Gọi hàm tải nội dung chi tiết dựa trên tên bài học
            if (tieuDe != null) {
                hienThiNoiDungChiTiet(tieuDe);
            }
        }
    }

    // Hàm ánh xạ ID (Bạn hãy kiểm tra kỹ ID trong file XML của bạn nhé)
    private void anhXaView() {
        // Header
        tvLoaiBai = findViewById(R.id.tag_vocabulary);       // ID của nhãn nhỏ (Vocabulary/Grammar...)
        tvTieuDe = findViewById(R.id.tv_tieude);      // ID của tiêu đề to
        tvCapDo = findViewById(R.id.text_beginner);              // ID của text Level
        tvThoiGian = findViewById(R.id.text_time);            // ID của text Time

        // Nút Back (Giả sử bạn đặt ID cho cái icon back là btn_back_detail)
        // Nếu trong XML bạn chưa đặt ID cho ImageView back, hãy vào đặt là android:id="@+id/btn_back_detail"
        btnBack = findViewById(R.id.btn_back_detail);

        // Nội dung chi tiết (Body)
        tvGioiThieu = findViewById(R.id.tv_introduction_content); // Phần Introduction
        tvDiemChinh1 = findViewById(R.id.tv_point_1);             // Key point dòng 1
        tvDiemChinh2 = findViewById(R.id.tv_point_2);             // Key point dòng 2
        tvViDuTu = findViewById(R.id.tv_example_word);            // Phần Example (Chữ xanh)
        tvViDuCau = findViewById(R.id.tv_example_sentence);       // Phần câu ví dụ

        // Nút Button dưới cùng
        btnLamBaiTap = findViewById(R.id.button_do_exercise);     // ID của nút Do Exercise
    }

    // Hàm logic để hiển thị nội dung giả lập
    private void hienThiNoiDungChiTiet(String tenBaiHoc) {

        // Dùng contains để kiểm tra từ khóa trong tên bài học
        if (tenBaiHoc.contains("thành viên") || tenBaiHoc.contains("Family")) {
            // Bài 1: Family Vocabulary
            setTextSafe(tvGioiThieu, "Basic English vocabulary for describing family relationships.");
            setTextSafe(tvDiemChinh1, "Immediate family (father, mother, sister, brother)");
            setTextSafe(tvDiemChinh2, "Extended family (uncle, aunt, cousin, grandparents)");
            setTextSafe(tvViDuTu, "Father / Mother");
            setTextSafe(tvViDuCau, "\"My father works in a hospital.\"");

        } else if (tenBaiHoc.contains("Sở hữu cách") || tenBaiHoc.contains("Possessive")) {
            // Bài 2: Grammar
            setTextSafe(tvGioiThieu, "Learn how to use apostrophe s ('s) to show possession.");
            setTextSafe(tvDiemChinh1, "Singular nouns: Add 's (e.g., John's car)");
            setTextSafe(tvDiemChinh2, "Plural nouns ending in s: Add ' (e.g., Students' books)");
            setTextSafe(tvViDuTu, "Rule Example");
            setTextSafe(tvViDuCau, "\"This is my brother's house.\"");

        } else if (tenBaiHoc.contains("Giới thiệu")) {
            // Bài 3: Speaking
            setTextSafe(tvGioiThieu, "Phrases and sentences to introduce your family members to others.");
            setTextSafe(tvDiemChinh1, "Using 'This is...' to introduce someone.");
            setTextSafe(tvDiemChinh2, "Describing age, jobs, and hobbies.");
            setTextSafe(tvViDuTu, "Introduction");
            setTextSafe(tvViDuCau, "\"There are 4 people in my family: my parents, my sister and me.\"");

        } else if (tenBaiHoc.contains("Hoạt động buổi sáng")) {
            // Bài Daily Routine
            setTextSafe(tvGioiThieu, "Vocabulary related to daily morning routines.");
            setTextSafe(tvDiemChinh1, "Verbs: wake up, brush teeth, have breakfast.");
            setTextSafe(tvDiemChinh2, "Time expressions: at 7 AM, in the morning.");
            setTextSafe(tvViDuTu, "Wake up");
            setTextSafe(tvViDuCau, "\"I usually wake up at 6 o'clock.\"");

        } else {
            // Nội dung mặc định nếu chưa cập nhật
            setTextSafe(tvGioiThieu, "Nội dung bài học đang được cập nhật...");
            setTextSafe(tvDiemChinh1, "Key point 1");
            setTextSafe(tvDiemChinh2, "Key point 2");
            setTextSafe(tvViDuTu, "Example");
            setTextSafe(tvViDuCau, "Example sentence.");
        }
    }

    // Hàm phụ trợ để set text an toàn (tránh lỗi nếu View bị null do tìm không thấy ID)
    private void setTextSafe(TextView tv, String text) {
        if (tv != null) {
            tv.setText(text);
        }
    }
}