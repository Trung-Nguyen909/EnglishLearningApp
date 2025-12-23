package com.example.englishlearningapp;

import android.content.Intent; // Nhớ import Intent
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ChiTietBaiTapActivity extends AppCompatActivity {

    // Khai báo các biến giao diện (View)
    private TextView tvLoaiBai, tvTieuDe, tvCapDo, tvThoiGian;
    private TextView tvGioiThieu, tvDiemChinh1, tvDiemChinh2, tvViDuTu, tvViDuCau;
    private MaterialButton btnLamBaiTap;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_baitap);
        Intent intent = getIntent();
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
        int baiHocId = intent.getIntExtra("BAIHOC_ID", -1);

        // >>> 3. XỬ LÝ SỰ KIỆN NÚT "LÀM BÀI TẬP" (SỬA Ở ĐÂY) <<<
        if (btnLamBaiTap != null) {
            btnLamBaiTap.setOnClickListener(v -> {
                if (baiHocId <= 0) return;
                Intent i = new Intent(this, LessonVocabularyActivity.class);
                i.putExtra(LessonVocabularyActivity.EXTRA_BAIHOC_ID, baiHocId);
                startActivity(i);
            });

        }

        // 4. NHẬN DỮ LIỆU TỪ MÀN HÌNH TRƯỚC
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
            setTextSafe(tvGioiThieu, "Từ vựng tiếng Anh cơ bản về các mối quan hệ trong gia đình.");
            setTextSafe(tvDiemChinh1, "Gia đình ruột thịt (bố, mẹ, anh, chị, em).");
            setTextSafe(tvDiemChinh2, "Họ hàng (cậu, dì, chú, bác, anh em họ, ông bà).");
            setTextSafe(tvViDuTu, "Father / Mother");
            setTextSafe(tvViDuCau, "\"My father works in a hospital.\" – Bố tôi làm việc trong một bệnh viện.");

        } else if (tenBaiHoc.contains("Sở hữu cách") || tenBaiHoc.contains("Possessive")) {
            setTextSafe(tvGioiThieu, "Học cách dùng 's để thể hiện sự sở hữu trong tiếng Anh.");
            setTextSafe(tvDiemChinh1, "Danh từ số ít: thêm 's (VD: John's car – xe của John).");
            setTextSafe(tvDiemChinh2, "Danh từ số nhiều tận cùng bằng s: thêm ' (VD: Students' books – sách của học sinh).");
            setTextSafe(tvViDuTu, "Ví dụ về quy tắc.");
            setTextSafe(tvViDuCau, "\"This is my brother's house.\" – Đây là nhà của anh trai tôi.");

        } else if (tenBaiHoc.contains("Giới thiệu")) {
            setTextSafe(tvGioiThieu, "Cụm từ và câu dùng để giới thiệu các thành viên gia đình.");
            setTextSafe(tvDiemChinh1, "Dùng 'This is...' để giới thiệu ai đó.");
            setTextSafe(tvDiemChinh2, "Mô tả tuổi, nghề nghiệp và sở thích.");
            setTextSafe(tvViDuTu, "Giới thiệu.");
            setTextSafe(tvViDuCau, "\"There are 4 people in my family...\" – Gia đình tôi có 4 người...");

        } else if (tenBaiHoc.contains("Hoạt động buổi sáng")) {
            setTextSafe(tvGioiThieu, "Từ vựng liên quan đến các hoạt động buổi sáng hằng ngày.");
            setTextSafe(tvDiemChinh1, "Các động từ: wake up (thức dậy), brush teeth (đánh răng), have breakfast (ăn sáng).");
            setTextSafe(tvDiemChinh2, "Các cụm chỉ thời gian: at 7 AM (lúc 7 giờ sáng), in the morning (vào buổi sáng).");
            setTextSafe(tvViDuTu, "Wake up – thức dậy.");
            setTextSafe(tvViDuCau, "\"I usually wake up at 6 o'clock.\" – Tôi thường thức dậy lúc 6 giờ.");

        } else {
            setTextSafe(tvGioiThieu, "Nội dung bài học đang được cập nhật...");
            setTextSafe(tvDiemChinh1, "Ý chính 1.");
            setTextSafe(tvDiemChinh2, "Ý chính 2.");
            setTextSafe(tvViDuTu, "Ví dụ từ.");
            setTextSafe(tvViDuCau, "Ví dụ câu.");
        }
    }


    private void setTextSafe(TextView tv, String text) {
        if (tv != null) {
            tv.setText(text);
        }
    }
}