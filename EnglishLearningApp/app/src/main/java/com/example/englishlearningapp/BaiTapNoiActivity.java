package com.example.englishlearningapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiNoiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaiTapNoiActivity extends AppCompatActivity {
    private ImageButton nutQuayLai;
    private TextView tvDongHo, tvDemSoCau, tvPhanTramTienTrinh;
    private ProgressBar thanhTienTrinh;
    private RecyclerView rcvCauHoiNoi;
    private Button btnHoanThanh;
    private BaiTapNoiAdapter adapterNoi;
    private List<CauHoiNoiModel> danhSachCauHoi;
    private TextToSpeech mayDoc;

    private Handler boXuLy = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_noi);

        anhXaView();
        taoDuLieuGia();
        caiDatAdapter();
        caiDatMayDoc();

        capNhatTienDo();
    }

    private void anhXaView() {
        nutQuayLai = findViewById(R.id.nut_quay_lai);
        tvDongHo = findViewById(R.id.tv_dong_ho);
        tvDemSoCau = findViewById(R.id.tv_dem_so_cau);
        tvPhanTramTienTrinh = findViewById(R.id.tv_phan_tram_tien_trinh);
        thanhTienTrinh = findViewById(R.id.thanh_tien_trinh);
        rcvCauHoiNoi = findViewById(R.id.rcv_cau_hoi_noi);
        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);

        rcvCauHoiNoi.setLayoutManager(new LinearLayoutManager(this));

        tvDongHo.setText("00:20");

        nutQuayLai.setOnClickListener(v -> finish());

        btnHoanThanh.setOnClickListener(v -> {
            chuyenSangTrangKetQua();
        });
    }

    private void taoDuLieuGia() {
        danhSachCauHoi = new ArrayList<>();
        danhSachCauHoi.add(new CauHoiNoiModel("Good morning"));
        danhSachCauHoi.add(new CauHoiNoiModel("How are you?"));
        danhSachCauHoi.add(new CauHoiNoiModel("I like learning English"));
        danhSachCauHoi.add(new CauHoiNoiModel("What is your name?"));
        danhSachCauHoi.add(new CauHoiNoiModel("Nice to meet you"));
    }

    private void caiDatAdapter() {
        adapterNoi = new BaiTapNoiAdapter(this, danhSachCauHoi, new BaiTapNoiAdapter.LangNgheSuKienItem() {
            @Override
            public void khiAnGhiAm(int viTri) {
                giaLapGhiAm(viTri);
            }

            @Override
            public void khiAnNghe(String noiDung) {
                docVanBan(noiDung);
            }
        });
        rcvCauHoiNoi.setAdapter(adapterNoi);
    }

    // --- HÀM GIẢ LẬP GHI ÂM 3 GIÂY ---
    private void giaLapGhiAm(int viTri) {
        Toast.makeText(this, "Đang ghi âm...", Toast.LENGTH_SHORT).show();

        boXuLy.postDelayed(new Runnable() {
            @Override
            public void run() {
                hoanThanhCauHoi(viTri);
            }
        }, 3000);
    }

    private void hoanThanhCauHoi(int viTri) {
        if (viTri >= 0 && viTri < danhSachCauHoi.size()) {
            CauHoiNoiModel cauHoi = danhSachCauHoi.get(viTri);

            // Giả lập: Người dùng nói đúng câu mẫu
            cauHoi.setDapAnNguoiDung(cauHoi.getCauMau());
            cauHoi.setChinhXac(true);

            adapterNoi.notifyItemChanged(viTri);

            capNhatTienDo();

            Toast.makeText(this, "Đã xác nhận đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void capNhatTienDo() {
        int tongSoCau = danhSachCauHoi.size();
        int soCauHoanThanh = 0;
        for (CauHoiNoiModel q : danhSachCauHoi) {
            if (q.isChinhXac()) {
                soCauHoanThanh++;
            }
        }

        // Cập nhật ProgressBar
        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauHoanThanh / tongSoCau) * 100) : 0;
        thanhTienTrinh.setProgress(phanTram);
        tvPhanTramTienTrinh.setText(phanTram + "%");
        tvDemSoCau.setText("Câu " + soCauHoanThanh + "/" + tongSoCau + " hoàn thành");

        // Logic nút Hoàn thành
        if (soCauHoanThanh == tongSoCau) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        }
    }

    // --- MÁY ĐỌC (TTS) ---
    private void caiDatMayDoc() {
        mayDoc = new TextToSpeech(this, trangThai -> {
            if (trangThai == TextToSpeech.SUCCESS) {
                mayDoc.setLanguage(Locale.US);
            }
        });
    }

    private void docVanBan(String noiDung) {
        if (mayDoc != null) {
            mayDoc.speak(noiDung, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mayDoc != null) {
            mayDoc.stop();
            mayDoc.shutdown();
        }
        // Xóa các luồng đếm giờ nếu thoát app đột ngột
        boXuLy.removeCallbacksAndMessages(null);
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapNoiActivity.this, KetQuaActivity.class);

        // Tính số câu đúng
        int soCauDung = 0;
        for (CauHoiNoiModel q : danhSachCauHoi) {
            if (q.isChinhXac()) {
                soCauDung++;
            }
        }

        // Truyền dữ liệu sang màn hình kết quả
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauDung);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, 0);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Nói");

        // Lấy level từ Intent cũ chuyển sang (nếu có)
        String level = getIntent().getStringExtra("SELECTED_LEVEL");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, level != null ? level : "Basic");

        startActivity(intent);
        finish();
    }
}