package com.example.englishlearningapp.Activity;

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

import com.example.englishlearningapp.Adapter.CauHoiNoiAdapter;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.DTO.Response.CauHoiNoiResponse;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiTapNoiActivity extends AppCompatActivity {
    private ImageButton nutQuayLai;
    private TextView tvDongHo, tvDemSoCau, tvPhanTramTienTrinh;
    private ProgressBar thanhTienTrinh;
    private RecyclerView rcvCauHoiNoi;
    private Button btnHoanThanh;

    private CauHoiNoiAdapter adapterNoi;
    private List<CauHoiNoiResponse> danhSachCauHoi = new ArrayList<>();
    private TextToSpeech mayDoc;

    private Handler boXuLy = new Handler(Looper.getMainLooper());

    // Biến nhận dữ liệu
    private int idBaiTap = -1;
    private String level = "Basic";

    // Biến đếm giờ
    private int soGiayLamBai = 0;
    private Runnable tacVuDemGio = new Runnable() {
        @Override
        public void run() {
            soGiayLamBai++;
            tvDongHo.setText(formatTime(soGiayLamBai));
            boXuLy.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_noi);

        // Nhận ID từ màn hình trước
        Intent intent = getIntent();
        if (intent != null) {
            idBaiTap = intent.getIntExtra("ID_BAI_TAP", -1);
            level = intent.getStringExtra("MUC_DO");
        }

        anhXaView();
        caiDatAdapter();
        caiDatMayDoc();

        if (idBaiTap != -1) {
            goiApiLayCauHoi(idBaiTap);
        } else {
            Toast.makeText(this, "Không tìm thấy bài tập!", Toast.LENGTH_SHORT).show();
        }

        // Bắt đầu đếm giờ
        boXuLy.postDelayed(tacVuDemGio, 1000);
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

        nutQuayLai.setOnClickListener(v -> finish());
        btnHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());
    }

    private void caiDatAdapter() {
        adapterNoi = new CauHoiNoiAdapter(this, danhSachCauHoi, new CauHoiNoiAdapter.LangNgheSuKienItem() {
            @Override
            public void khiAnGhiAm(int viTri) {
                // Khi ấn nút Mic -> Gọi hàm giả lập
                giaLapGhiAm(viTri);
            }

            @Override
            public void khiAnNghe(String noiDung) {
                docVanBan(noiDung);
            }
        });
        rcvCauHoiNoi.setAdapter(adapterNoi);
    }

    // --- GỌI API LẤY CÂU HỎI (GIỮ NGUYÊN) ---
    private void goiApiLayCauHoi(int id) {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.getCauHoiNoiByBaiTapId(id).enqueue(new Callback<List<CauHoiNoiResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiNoiResponse>> call, Response<List<CauHoiNoiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiNoiResponse> list = response.body();
                    for (CauHoiNoiResponse q : list) {
                        q.xuLyDuLieu();
                        danhSachCauHoi.add(q);
                    }
                    adapterNoi.notifyDataSetChanged();
                    capNhatTienDo();
                } else {
                    Toast.makeText(BaiTapNoiActivity.this, "Chưa có câu hỏi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CauHoiNoiResponse>> call, Throwable t) {
                Toast.makeText(BaiTapNoiActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- HÀM GIẢ LẬP GHI ÂM (QUAN TRỌNG) ---
    private void giaLapGhiAm(int viTri) {
        Toast.makeText(this, "Đang nghe...", Toast.LENGTH_SHORT).show();

        // Giả vờ đợi 2 giây rồi tự điền đáp án đúng
        boXuLy.postDelayed(() -> {
            if (viTri >= 0 && viTri < danhSachCauHoi.size()) {
                CauHoiNoiResponse q = danhSachCauHoi.get(viTri);

                // 1. Lấy câu mẫu gán vào đáp án người dùng (Coi như nói đúng)
                q.setDapAnNguoiDung(q.getCauMau());

                // 2. Đánh dấu là Chính xác
                q.setChinhXac(true);

                // 3. Cập nhật giao diện dòng đó
                adapterNoi.notifyItemChanged(viTri);

                // 4. Cập nhật thanh tiến trình
                capNhatTienDo();

                Toast.makeText(this, "Chính xác!", Toast.LENGTH_SHORT).show();
            }
        }, 2000); // 2000ms = 2 giây
    }

    private void capNhatTienDo() {
        int tong = danhSachCauHoi.size();
        if (tong == 0) return;

        int hoanThanh = 0;
        for (CauHoiNoiResponse q : danhSachCauHoi) {
            if (q.isChinhXac()) hoanThanh++;
        }

        int phanTram = (int) (((float) hoanThanh / tong) * 100);
        thanhTienTrinh.setProgress(phanTram);
        tvPhanTramTienTrinh.setText(phanTram + "%");
        tvDemSoCau.setText("Câu " + hoanThanh + "/" + tong);

        if (hoanThanh == tong) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        }
    }

    // --- TTS ---
    private void caiDatMayDoc() {
        mayDoc = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) mayDoc.setLanguage(Locale.US);
        });
    }

    private void docVanBan(String text) {
        if (mayDoc != null) mayDoc.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    // --- CHUYỂN TRANG KẾT QUẢ (KHÔNG LƯU API) ---
    private void chuyenSangTrangKetQua() {
        boXuLy.removeCallbacks(tacVuDemGio); // Dừng giờ

        int soCauDung = 0;
        for (CauHoiNoiResponse q : danhSachCauHoi) {
            if (q.isChinhXac()) soCauDung++;
        }
        int tongSoCau = danhSachCauHoi.size();

        Intent intent = new Intent(this, KetQuaActivity.class);
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauDung);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, tongSoCau);
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, soGiayLamBai);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Nói");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, level);
        intent.putExtra("ID_BAI_TAP", idBaiTap);

        startActivity(intent);
        finish();
    }

    private String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d", m, s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mayDoc != null) { mayDoc.stop(); mayDoc.shutdown(); }
        boXuLy.removeCallbacksAndMessages(null);
    }
}