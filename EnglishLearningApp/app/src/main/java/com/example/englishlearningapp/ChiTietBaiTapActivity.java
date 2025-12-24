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

public class ChiTietBaiTapActivity extends AppCompatActivity {

    private TextView tvLoaiBai, tvTieuDe, tvCapDo, tvThoiGian;
    private TextView tvGioiThieu, tvDiemChinh1, tvDiemChinh2, tvViDuTu, tvViDuCau;
    private MaterialButton btnLamBaiTap;
    private ImageView btnBack;

    // Biến quan trọng để lưu dữ liệu bài tập
    private int baiTapId = -1;
    private String loaiBaiString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_baitap);

        anhXaView();

        // --- 1. NHẬN DỮ LIỆU TỪ MÀN HÌNH TRƯỚC (QUAN TRỌNG) ---
        Intent intent = getIntent();
        if (intent != null) {

            baiTapId = intent.getIntExtra("BAIHOC_ID", -1);

            // Nếu code cũ có gửi kèm ID_BAI_TAP thì ưu tiên lấy, nếu không thì dùng cái trên
            int idDuPhong = intent.getIntExtra("ID_BAI_TAP", -1);
            if (idDuPhong != -1) {
                baiTapId = idDuPhong;
            }

            // Lấy loại bài tập (Nghe, Đọc...) để tí nữa xét điều kiện
            loaiBaiString = intent.getStringExtra("TYPE");
            if (loaiBaiString == null) loaiBaiString = "";

            // Lấy các thông tin hiển thị
            String tieuDe = intent.getStringExtra("TITLE");
            String capDo = intent.getStringExtra("LEVEL");
            String thoiGian = intent.getStringExtra("TIME");

            // Hiển thị lên UI
            setTextSafe(tvTieuDe, tieuDe);
            setTextSafe(tvLoaiBai, loaiBaiString);
            setTextSafe(tvCapDo, capDo);
            setTextSafe(tvThoiGian, thoiGian);

            if (tieuDe != null) hienThiNoiDungChiTiet(tieuDe);
        }

        // 2. Xử lý nút Back
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            LinearLayout topBar = findViewById(R.id.top_bar);
            if(topBar != null && topBar.getChildCount() > 0) {
                topBar.getChildAt(0).setOnClickListener(v -> finish());
            }
        }

        // >>> 3. XỬ LÝ NÚT "LÀM BÀI TẬP" - DỰA VÀO ID VÀ LOẠI ĐÃ LẤY ĐƯỢC <<<
        if (btnLamBaiTap != null) {
            btnLamBaiTap.setOnClickListener(v -> {

                // Kiểm tra xem đã có ID bài tập chưa
                if (baiTapId == -1) {
                    Toast.makeText(this, "Lỗi: Không tìm thấy ID bài tập!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = null;
                String typeCheck = loaiBaiString.toLowerCase(); // Chuyển về chữ thường để so sánh

                // --- LOGIC PHÂN LUỒNG MÀN HÌNH ---
                if (typeCheck.contains("nghe") || typeCheck.contains("listening")) {
                    // Nếu loại là NGHE -> Mở Activity Nghe
                    i = new Intent(ChiTietBaiTapActivity.this, BaiTapNgheActivity.class);
                }
                else if (typeCheck.contains("đọc") || typeCheck.contains("reading")) {
                    // Nếu loại là ĐỌC/TỪ VỰNG -> Mở Activity Đọc (Trắc nghiệm)
                    i = new Intent(ChiTietBaiTapActivity.this, BaiTapDocActivity.class);
                }
                else if (typeCheck.contains("nói") || typeCheck.contains("speaking")) {
                    i = new Intent(ChiTietBaiTapActivity.this, BaiTapNoiActivity.class);
                }
                else if (typeCheck.contains("viết") || typeCheck.contains("writing")) {
                    Toast.makeText(this, "Chức năng Luyện Viết đang cập nhật!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    // Mặc định nếu không nhận diện được thì mở bài Đọc (hoặc báo lỗi)
                    Toast.makeText(this, "Loại bài tập: " + loaiBaiString + ". Đang mở mặc định!", Toast.LENGTH_SHORT).show();
                    i = new Intent(ChiTietBaiTapActivity.this, BaiTapDocActivity.class);
                }

                // --- QUAN TRỌNG: TRUYỀN ID BÀI TẬP VÀO INTENT ---
                i.putExtra("ID_BAI_TAP", baiTapId);

                // Truyền thêm thông tin phụ trợ (nếu cần hiển thị ở màn sau)
                i.putExtra("TEN_BAI_TAP", tvTieuDe.getText().toString());
                i.putExtra("MUC_DO", tvCapDo.getText().toString());

                startActivity(i);
            });
        }
    }

    // --- Các hàm phụ trợ giữ nguyên ---
    private void anhXaView() {
        tvLoaiBai = findViewById(R.id.tag_vocabulary);
        tvTieuDe = findViewById(R.id.tv_tieude);
        tvCapDo = findViewById(R.id.text_beginner);
        tvThoiGian = findViewById(R.id.text_time);
        btnBack = findViewById(R.id.btn_back_detail);
        tvGioiThieu = findViewById(R.id.tv_introduction_content);
        tvDiemChinh1 = findViewById(R.id.tv_point_1);
        tvDiemChinh2 = findViewById(R.id.tv_point_2);
        tvViDuTu = findViewById(R.id.tv_example_word);
        tvViDuCau = findViewById(R.id.tv_example_sentence);
        btnLamBaiTap = findViewById(R.id.button_do_exercise);
    }

    private void hienThiNoiDungChiTiet(String tenBaiHoc) {
        if (tenBaiHoc.contains("thành viên") || tenBaiHoc.contains("Family")) {
            setTextSafe(tvGioiThieu, "Từ vựng tiếng Anh cơ bản về các mối quan hệ trong gia đình.");
            setTextSafe(tvDiemChinh1, "Gia đình ruột thịt (bố, mẹ, anh, chị, em).");
            setTextSafe(tvDiemChinh2, "Họ hàng (cậu, dì, chú, bác, anh em họ, ông bà).");
            setTextSafe(tvViDuTu, "Father / Mother");
            setTextSafe(tvViDuCau, "\"My father works in a hospital.\"");
        } else {
            setTextSafe(tvGioiThieu, "Chi tiết bài tập: " + tenBaiHoc);
            setTextSafe(tvDiemChinh1, "Kỹ năng: " + loaiBaiString);
            setTextSafe(tvDiemChinh2, "Hoàn thành 100% để nhận điểm thưởng.");
            setTextSafe(tvViDuTu, "Example");
            setTextSafe(tvViDuCau, "Example Sentence");
        }
    }

    private void setTextSafe(TextView tv, String text) {
        if (tv != null) tv.setText(text != null ? text : "");
    }
}