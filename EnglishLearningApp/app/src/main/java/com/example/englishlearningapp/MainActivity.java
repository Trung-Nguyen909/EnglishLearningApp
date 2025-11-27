package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout btnTrangChu, btnKhoaHoc, btnTienDo, btnHoSo;
    private ImageView iconTrangChu, iconKhoaHoc, iconTienDo, iconHoSo;
    private View dotTrangChu, dotKhoaHoc, dotTienDo, dotHoSo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ View
        anhXaView();

        // 2. Mặc định chọn tab Trang chủ khi mở app
        napFragment(new TrangChuFragment());
        capNhatGiaoDienMenuDuoi(btnTrangChu, iconTrangChu, dotTrangChu);

        // 3. Bắt sự kiện click
        btnTrangChu.setOnClickListener(v -> {
            napFragment(new TrangChuFragment());
            capNhatGiaoDienMenuDuoi(btnTrangChu, iconTrangChu, dotTrangChu);
        });

        btnKhoaHoc.setOnClickListener(v -> {
            // Chuyển sang Fragment Khóa học
            napFragment(new KhoaHocFragment());
            capNhatGiaoDienMenuDuoi(btnKhoaHoc, iconKhoaHoc, dotKhoaHoc);
        });

        btnTienDo.setOnClickListener(v -> {
            // Chuyển sang Fragment Tiến độ
            napFragment(new TienDoBaiTapFragment());
            capNhatGiaoDienMenuDuoi(btnTienDo, iconTienDo, dotTienDo);
        });

        btnHoSo.setOnClickListener(v -> {
            // Chuyển sang Fragment Hồ sơ
            napFragment(new HoSoFragment());
            capNhatGiaoDienMenuDuoi(btnHoSo, iconHoSo, dotHoSo);
        });
    }

    private void anhXaView() {
        // Nút bấm (Container)
        btnTrangChu = findViewById(R.id.btn_home);
        btnKhoaHoc = findViewById(R.id.btn_baihoc);
        btnTienDo = findViewById(R.id.btn_tientrinh);
        btnHoSo = findViewById(R.id.btn_hoso);

        // Biểu tượng (Icon)
        iconTrangChu = findViewById(R.id.icon_home);
        iconKhoaHoc = findViewById(R.id.icon_baihoc);
        iconTienDo = findViewById(R.id.icon_tientrinh);
        iconHoSo = findViewById(R.id.icon_hoso);

        // Dấu chấm (Dot)
        dotTrangChu = findViewById(R.id.dot_home);
        dotKhoaHoc = findViewById(R.id.dot_baihoc);
        dotTienDo = findViewById(R.id.dot_tientrinh);
        dotHoSo = findViewById(R.id.dot_hoso);
    }

    // Hàm nạp Fragment vào FrameLayout
    private void napFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

    // Hàm cập nhật giao diện Bottom Nav (Đổi màu + Hiện dấu chấm)
    private void capNhatGiaoDienMenuDuoi(LinearLayout btnDuocChon, ImageView iconDuocChon, View dotDuocChon) {
        // Lấy màu sắc từ tài nguyên
        int mauKichHoat = ContextCompat.getColor(this, R.color.royalBlue); // Màu xanh chủ đạo
        int mauVoHieu = ContextCompat.getColor(this, android.R.color.darker_gray); // Màu xám

        // 1. Reset tất cả về trạng thái chưa chọn (Màu xám, ẩn dấu chấm)
        iconTrangChu.setColorFilter(mauVoHieu);
        iconKhoaHoc.setColorFilter(mauVoHieu);
        iconTienDo.setColorFilter(mauVoHieu);
        iconHoSo.setColorFilter(mauVoHieu);

        dotTrangChu.setVisibility(View.INVISIBLE);
        dotKhoaHoc.setVisibility(View.INVISIBLE);
        dotTienDo.setVisibility(View.INVISIBLE);
        dotHoSo.setVisibility(View.INVISIBLE);

        // 2. Set trạng thái cho tab ĐƯỢC CHỌN (Màu xanh, hiện dấu chấm)
        iconDuocChon.setColorFilter(mauKichHoat);
        dotDuocChon.setVisibility(View.VISIBLE);
    }
}