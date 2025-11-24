package com.example.englishlearningapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.ArrayList;
import java.util.List;

public class BaiTapVietActivity extends AppCompatActivity implements BaiTapVietAdapter.LangNgheSuKienViet {

    private RecyclerView rcvDanhSachCauHoi;
    private Button nutHoanThanh;
    private ImageButton nutQuayLai;
    private ProgressBar thanhTienDo;
    private TextView tvDemCau, tvPhanTram;
    private List<CauHoiModel> danhSachCauHoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_viet);
        anhXaView();
        khoiTaoDuLieu();
    }

    private void anhXaView() {
        rcvDanhSachCauHoi = findViewById(R.id.rcv_danh_sach_cau_hoi_viet);
        nutHoanThanh = findViewById(R.id.btn_hoan_thanh);
        nutQuayLai = findViewById(R.id.nut_quay_lai);
        thanhTienDo = findViewById(R.id.thanh_tien_trinh);
        tvDemCau = findViewById(R.id.tv_dem_so_cau);
        tvPhanTram = findViewById(R.id.tv_phan_tram_tien_trinh);

        if (nutQuayLai != null) {
            nutQuayLai.setOnClickListener(v -> finish());
        }

        if (nutHoanThanh != null) {
            nutHoanThanh.setText("Hoàn thành");
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0"))); // Màu xám

            nutHoanThanh.setOnClickListener(v -> {
                chuyenSangTrangKetQua();
            });
        }
    }

    private void khoiTaoDuLieu() {
        danhSachCauHoi = new ArrayList<>();
        danhSachCauHoi.add(new CauHoiModel(1, "Câu 1", "Describe your daily routine.", ""));
        danhSachCauHoi.add(new CauHoiModel(2, "Câu 2", "What is your favorite food?", ""));
        danhSachCauHoi.add(new CauHoiModel(3, "Câu 3", "Write about your last holiday.", ""));
        danhSachCauHoi.add(new CauHoiModel(4, "Câu 4", "Why do you learn English?", ""));
        danhSachCauHoi.add(new CauHoiModel(5, "Câu 5", "Describe your best friend.", ""));

        BaiTapVietAdapter adapter = new BaiTapVietAdapter(danhSachCauHoi, this);
        rcvDanhSachCauHoi.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhSachCauHoi.setAdapter(adapter);

        capNhatTrangThai();
    }

    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAn) {
        capNhatTrangThai();
    }

    private void capNhatTrangThai() {
        int soCauDaLam = 0;
        int tongSoCau = danhSachCauHoi.size();

        for (CauHoiModel cauHoi : danhSachCauHoi) {
            if (cauHoi.getDapAnDaChon() != null && !cauHoi.getDapAnDaChon().trim().isEmpty()) {
                soCauDaLam++;
            }
        }

        thanhTienDo.setMax(danhSachCauHoi.size());
        thanhTienDo.setProgress(soCauDaLam);

        tvDemCau.setText("Câu " + soCauDaLam + "/" + tongSoCau);

        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauDaLam / tongSoCau) * 100) : 0;
        tvPhanTram.setText(phanTram + "%");

        // Logic nút Hoàn thành
        if (soCauDaLam == tongSoCau) {
            nutHoanThanh.setEnabled(true);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1"))); // Màu xanh
        } else {
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0"))); // Màu xám
        }
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapVietActivity.this, KetQuaActivity.class);

        // Tính toán số câu đã làm
        int soCauHoanThanh = 0;
        for (CauHoiModel q : danhSachCauHoi) {
            if (q.getDapAnDaChon() != null && !q.getDapAnDaChon().trim().isEmpty()) {
                soCauHoanThanh++;
            }
        }

        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauHoanThanh);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, 0);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Writing");

        String level = getIntent().getStringExtra("SELECTED_LEVEL");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, level != null ? level : "Basic");

        startActivity(intent);
        finish();
    }
}