package com.example.englishlearningapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
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

public class BaiTapNoiActivity extends AppCompatActivity {

    // Khai báo View
    private ImageButton nutQuayLai, nutGhiAm;
    private TextView tvDongHo, tvTrangThaiGhiAm, tvThoiGianGhiAm;
    private View viewSongAm;
    private LinearLayout layoutChucNangSauGhiAm;
    private AppCompatButton nutNgheLai, nutGhiLai, nutTiepTheo;
    private ProgressBar thanhTienDo;

    private boolean dangGhiAm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai_tap_noi);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.man_hinh_noi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupEvents();
    }

    private void initViews() {
        // Ánh xạ với ID tiếng Việt
        nutQuayLai = findViewById(R.id.nut_quay_lai);
        tvDongHo = findViewById(R.id.tv_dong_ho);
        nutGhiAm = findViewById(R.id.nut_ghi_am);
        tvTrangThaiGhiAm = findViewById(R.id.tv_trang_thai_ghi_am);
        tvThoiGianGhiAm = findViewById(R.id.tv_thoi_gian_ghi_am);
        viewSongAm = findViewById(R.id.view_song_am);
        layoutChucNangSauGhiAm = findViewById(R.id.layout_chuc_nang_sau_ghi_am);
        nutNgheLai = findViewById(R.id.nut_nghe_lai);
        nutGhiLai = findViewById(R.id.nut_ghi_lai);
        nutTiepTheo = findViewById(R.id.nut_tiep_theo);
        thanhTienDo = findViewById(R.id.thanh_tien_do);

        nutTiepTheo.setEnabled(false);
    }

    private void setupEvents() {
        nutQuayLai.setOnClickListener(v -> finish());

        nutGhiAm.setOnClickListener(v -> {
            if (!dangGhiAm) {
                batDauGhiAm();
            } else {
                dungGhiAm();
            }
        });

        nutGhiLai.setOnClickListener(v -> resetGiaoDien());
    }

    private void batDauGhiAm() {
        dangGhiAm = true;
        nutGhiAm.setImageResource(android.R.drawable.ic_media_pause);
        nutGhiAm.setColorFilter(Color.RED);
        tvTrangThaiGhiAm.setText("Đang ghi âm...");
        tvTrangThaiGhiAm.setTextColor(Color.RED);

        tvThoiGianGhiAm.setVisibility(View.VISIBLE);
        viewSongAm.setVisibility(View.VISIBLE);
        layoutChucNangSauGhiAm.setVisibility(View.GONE);
    }

    private void dungGhiAm() {
        dangGhiAm = false;
        nutGhiAm.setImageResource(android.R.drawable.ic_btn_speak_now);
        nutGhiAm.setColorFilter(Color.parseColor("#4169E1"));

        tvTrangThaiGhiAm.setText("Ghi âm hoàn tất!");
        tvTrangThaiGhiAm.setTextColor(Color.parseColor("#4CAF50"));

        viewSongAm.setVisibility(View.GONE);
        layoutChucNangSauGhiAm.setVisibility(View.VISIBLE);

        nutTiepTheo.setEnabled(true);
        nutTiepTheo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        nutTiepTheo.setTextColor(Color.WHITE);
    }

    private void resetGiaoDien() {
        dangGhiAm = false;
        tvTrangThaiGhiAm.setText("Nhấn vào micro để bắt đầu nói");
        tvTrangThaiGhiAm.setTextColor(Color.parseColor("#666666"));
        tvThoiGianGhiAm.setVisibility(View.GONE);
        layoutChucNangSauGhiAm.setVisibility(View.GONE);
        nutTiepTheo.setEnabled(false);
        nutTiepTheo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
        nutTiepTheo.setTextColor(Color.parseColor("#888888"));
    }
}