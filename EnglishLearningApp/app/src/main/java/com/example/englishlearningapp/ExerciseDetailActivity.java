package com.example.englishlearningapp;

import android.content.Intent; // Nhớ import Intent
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ExerciseDetailActivity extends AppCompatActivity {

    // Khai báo các biến giao diện (View)
    private TextView tvLoaiBai, tvTieuDe, tvCapDo, tvThoiGian;
    private TextView tvGioiThieu, tvDiemChinh1, tvDiemChinh2, tvViDuTu, tvViDuCau;
    private MaterialButton btnLamBaiTap;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        // 1. Ánh xạ các View
        anhXaView();

        // 2. Xử lý nút Back
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            LinearLayout topBar = findViewById(R.id.top_bar);
            if(topBar != null && topBar.getChildCount() > 0) {
                topBar.getChildAt(0).setOnClickListener(v -> finish());
            }
        }

        // >>> 3. XỬ LÝ SỰ KIỆN NÚT "LÀM BÀI TẬP" (SỬA Ở ĐÂY) <<<
        if (btnLamBaiTap != null) {
            btnLamBaiTap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tạo Intent chuyển sang trang BaiTapActivity (Trắc nghiệm)
                    Intent intent = new Intent(ExerciseDetailActivity.this, BaiTapDocActivity.class);

                    // Gửi kèm cấp độ (Ví dụ: Beginner) để trang bài tập biết load câu dễ hay khó
                    if (tvCapDo != null) {
                        intent.putExtra("SELECTED_LEVEL", tvCapDo.getText().toString());
                    }

                    // Chuyển trang
                    startActivity(intent);
                }
            });
        }

        // 4. NHẬN DỮ LIỆU TỪ MÀN HÌNH TRƯỚC
        Intent intent = getIntent();
        if (intent != null) {
            String tieuDe = intent.getStringExtra("TITLE");
            String loaiBai = intent.getStringExtra("TYPE");
            String capDo = intent.getStringExtra("LEVEL");
            String thoiGian = intent.getStringExtra("TIME");

            // Hiển thị thông tin
            if (tvTieuDe != null) tvTieuDe.setText(tieuDe);
            if (tvLoaiBai != null) tvLoaiBai.setText(loaiBai);
            if (tvCapDo != null) tvCapDo.setText(capDo);
            if (tvThoiGian != null) tvThoiGian.setText(thoiGian);

            // Tải nội dung chi tiết
            if (tieuDe != null) {
                hienThiNoiDungChiTiet(tieuDe);
            }
        }
    }

    private void anhXaView() {
        // Header (IDs dựa theo code bạn gửi)
        tvLoaiBai = findViewById(R.id.tag_vocabulary);
        tvTieuDe = findViewById(R.id.tv_tieude);
        tvCapDo = findViewById(R.id.text_beginner);
        tvThoiGian = findViewById(R.id.text_time);

        btnBack = findViewById(R.id.btn_back_detail);

        // Body
        tvGioiThieu = findViewById(R.id.tv_introduction_content);
        tvDiemChinh1 = findViewById(R.id.tv_point_1);
        tvDiemChinh2 = findViewById(R.id.tv_point_2);
        tvViDuTu = findViewById(R.id.tv_example_word);
        tvViDuCau = findViewById(R.id.tv_example_sentence);

        // Button
        btnLamBaiTap = findViewById(R.id.button_do_exercise);
    }

    private void hienThiNoiDungChiTiet(String tenBaiHoc) {
        if (tenBaiHoc.contains("thành viên") || tenBaiHoc.contains("Family")) {
            setTextSafe(tvGioiThieu, "Basic English vocabulary for describing family relationships.");
            setTextSafe(tvDiemChinh1, "Immediate family (father, mother, sister, brother)");
            setTextSafe(tvDiemChinh2, "Extended family (uncle, aunt, cousin, grandparents)");
            setTextSafe(tvViDuTu, "Father / Mother");
            setTextSafe(tvViDuCau, "\"My father works in a hospital.\"");

        } else if (tenBaiHoc.contains("Sở hữu cách") || tenBaiHoc.contains("Possessive")) {
            setTextSafe(tvGioiThieu, "Learn how to use apostrophe s ('s) to show possession.");
            setTextSafe(tvDiemChinh1, "Singular nouns: Add 's (e.g., John's car)");
            setTextSafe(tvDiemChinh2, "Plural nouns ending in s: Add ' (e.g., Students' books)");
            setTextSafe(tvViDuTu, "Rule Example");
            setTextSafe(tvViDuCau, "\"This is my brother's house.\"");

        } else if (tenBaiHoc.contains("Giới thiệu")) {
            setTextSafe(tvGioiThieu, "Phrases and sentences to introduce your family members to others.");
            setTextSafe(tvDiemChinh1, "Using 'This is...' to introduce someone.");
            setTextSafe(tvDiemChinh2, "Describing age, jobs, and hobbies.");
            setTextSafe(tvViDuTu, "Introduction");
            setTextSafe(tvViDuCau, "\"There are 4 people in my family: my parents, my sister and me.\"");

        } else if (tenBaiHoc.contains("Hoạt động buổi sáng")) {
            setTextSafe(tvGioiThieu, "Vocabulary related to daily morning routines.");
            setTextSafe(tvDiemChinh1, "Verbs: wake up, brush teeth, have breakfast.");
            setTextSafe(tvDiemChinh2, "Time expressions: at 7 AM, in the morning.");
            setTextSafe(tvViDuTu, "Wake up");
            setTextSafe(tvViDuCau, "\"I usually wake up at 6 o'clock.\"");

        } else {
            setTextSafe(tvGioiThieu, "Nội dung bài học đang được cập nhật...");
            setTextSafe(tvDiemChinh1, "Key point 1");
            setTextSafe(tvDiemChinh2, "Key point 2");
            setTextSafe(tvViDuTu, "Example");
            setTextSafe(tvViDuCau, "Example sentence.");
        }
    }

    private void setTextSafe(TextView tv, String text) {
        if (tv != null) {
            tv.setText(text);
        }
    }
}