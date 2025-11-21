package com.example.englishlearningapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Interface.LangNgheSuKien;
import com.example.englishlearningapp.Model.CauHoi;

import java.util.ArrayList;
import java.util.List;

public class BaiTapVietActivity extends AppCompatActivity implements LangNgheSuKien {

    private RecyclerView rcvDanhSachCauHoi;
    private Button nutHoanThanh;
    private ImageButton nutQuayLai;
    private ProgressBar thanhTienDo;
    private TextView tvDemCau, tvPhanTram;
    private List<CauHoi> danhSachCauHoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_viet);
        anhXaView();
        khoiTaoDuLieu();
    }

    private void anhXaView() {
        rcvDanhSachCauHoi = findViewById(R.id.questions_recycler_view);
        nutHoanThanh = findViewById(R.id.btn_hoan_thanh);
        nutQuayLai = findViewById(R.id.btn_back);
        thanhTienDo = findViewById(R.id.progress_bar);
        tvDemCau = findViewById(R.id.tv_question_counter);
        tvPhanTram = findViewById(R.id.tv_progress_percentage);

        if (nutQuayLai != null) {
            nutQuayLai.setOnClickListener(v -> finish());
        }

        if (nutHoanThanh != null) {
            nutHoanThanh.setText("Hoàn thành");
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));

            nutHoanThanh.setOnClickListener(v -> {
                chuyenSangTrangKetQua();
            });
        }
    }
    private void khoiTaoDuLieu() {
        danhSachCauHoi = new ArrayList<>();
        // Constructor 4 tham số: id, huongDan, noiDung, dapAnGoiY
        danhSachCauHoi.add(new CauHoi(1, "Câu 1", "Describe your daily routine.", ""));
        danhSachCauHoi.add(new CauHoi(2, "Câu 2", "What is your favorite food?", ""));
        danhSachCauHoi.add(new CauHoi(3, "Câu 3", "Write about your last holiday.", ""));
        danhSachCauHoi.add(new CauHoi(4, "Câu 4", "Why do you learn English?", ""));
        danhSachCauHoi.add(new CauHoi(5, "Câu 5", "Describe your best friend.", ""));

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

        for (CauHoi cauHoi : danhSachCauHoi) {
            if (cauHoi.getDapAnDaChon() != null && !cauHoi.getDapAnDaChon().trim().isEmpty()) {
                soCauDaLam++;
            }
        }

        thanhTienDo.setMax(danhSachCauHoi.size());
        thanhTienDo.setProgress(soCauDaLam);

        // Chỉ cho phép nộp khi làm đủ tất cả câu hỏi
        if (soCauDaLam == danhSachCauHoi.size()) {
            nutHoanThanh.setEnabled(true);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
        }
        tvDemCau.setText("Câu " + soCauDaLam + "/" + tongSoCau);

        int phanTram = (int) (((float) soCauDaLam / tongSoCau) * 100);
        tvPhanTram.setText(phanTram + "%");

        // Logic nút Hoàn thành (giữ nguyên)
        if (soCauDaLam == tongSoCau) {
            nutHoanThanh.setEnabled(true);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
        }
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapVietActivity.this, TestResultActivity.class);

        // Tính toán số câu đã làm
        int completedCount = 0;
        for (CauHoi q : danhSachCauHoi) {
            if (q.getDapAnDaChon() != null && !q.getDapAnDaChon().trim().isEmpty()) {
                completedCount++;
            }
        }

        intent.putExtra(TestResultActivity.EXTRA_CORRECT_ANSWERS, completedCount);
        intent.putExtra(TestResultActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());
        intent.putExtra(TestResultActivity.EXTRA_TIME_SPENT, 0);
        intent.putExtra(TestResultActivity.EXTRA_TOPIC, "Writing");

        String level = getIntent().getStringExtra("SELECTED_LEVEL");
        intent.putExtra(TestResultActivity.EXTRA_LEVEL, level != null ? level : "Basic");

        startActivity(intent);
        finish();
    }
}