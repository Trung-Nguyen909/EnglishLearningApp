package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.Adapter.CauHoiVietAdapter;
import com.example.englishlearningapp.DTO.Response.CauHoiVietResponse;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiTapVietActivity extends AppCompatActivity implements CauHoiVietAdapter.LangNgheSuKienViet {

    private RecyclerView rcvDanhSachCauHoi;
    private Button nutHoanThanh;
    private ImageButton nutQuayLai;
    private ProgressBar thanhTienDo;
    private TextView tvDemCau, tvPhanTram, tvDongHo;

    private List<CauHoiVietResponse> danhSachCauHoi = new ArrayList<>();
    private CauHoiVietAdapter adapter;

    // Biến nhận dữ liệu
    private int idBaiTap = -1;
    private String level = "Basic";

    // Biến tính giờ
    private int soGiayLamBai = 0;
    private Handler boXuLy = new Handler(Looper.getMainLooper());
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
        setContentView(R.layout.activity_bai_tap_viet);

        // Nhận dữ liệu
        Intent intent = getIntent();
        if (intent != null) {
            idBaiTap = intent.getIntExtra("ID_BAI_TAP", -1);
            level = intent.getStringExtra("MUC_DO");
        }

        anhXaView();

        if (idBaiTap != -1) {
            goiApiLayCauHoi(idBaiTap);
        } else {
            Toast.makeText(this, "Không tìm thấy bài tập!", Toast.LENGTH_SHORT).show();
        }

        // Bắt đầu đếm giờ
        boXuLy.postDelayed(tacVuDemGio, 1000);
    }

    private void anhXaView() {
        rcvDanhSachCauHoi = findViewById(R.id.rcv_danh_sach_cau_hoi_viet);
        nutHoanThanh = findViewById(R.id.btn_hoan_thanh);
        nutQuayLai = findViewById(R.id.nut_quay_lai);
        thanhTienDo = findViewById(R.id.thanh_tien_trinh);
        tvDemCau = findViewById(R.id.tv_dem_so_cau);
        tvPhanTram = findViewById(R.id.tv_phan_tram_tien_trinh);
        tvDongHo = findViewById(R.id.tv_dong_ho); // Đảm bảo layout có ID này

        if (nutQuayLai != null) nutQuayLai.setOnClickListener(v -> finish());

        if (nutHoanThanh != null) {
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
            nutHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());
        }
    }

    private void goiApiLayCauHoi(int id) {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.getCauHoiVietByBaiTapId(id).enqueue(new Callback<List<CauHoiVietResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiVietResponse>> call, Response<List<CauHoiVietResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiVietResponse> list = response.body();
                    for (CauHoiVietResponse q : list) {
                        q.xuLyDuLieu();
                        danhSachCauHoi.add(q);
                    }
                    setupRecyclerView();
                    capNhatTrangThai();
                } else {
                    Toast.makeText(BaiTapVietActivity.this, "Chưa có câu hỏi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CauHoiVietResponse>> call, Throwable t) {
                Toast.makeText(BaiTapVietActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new CauHoiVietAdapter(danhSachCauHoi, this);
        rcvDanhSachCauHoi.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhSachCauHoi.setAdapter(adapter);
    }

    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAn) {
        // Cập nhật đáp án vào list (Adapter đã set rồi nhưng gọi lại để update thanh tiến độ)
        capNhatTrangThai();
    }

    private void capNhatTrangThai() {
        int soCauDaLam = 0;
        int tongSoCau = danhSachCauHoi.size();

        for (CauHoiVietResponse cauHoi : danhSachCauHoi) {
            if (cauHoi.getDapAnNguoiDung() != null && !cauHoi.getDapAnNguoiDung().trim().isEmpty()) {
                soCauDaLam++;
            }
        }

        thanhTienDo.setMax(tongSoCau);
        thanhTienDo.setProgress(soCauDaLam);
        tvDemCau.setText("Câu " + soCauDaLam + "/" + tongSoCau);

        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauDaLam / tongSoCau) * 100) : 0;
        tvPhanTram.setText(phanTram + "%");

        if (soCauDaLam == tongSoCau) {
            nutHoanThanh.setEnabled(true);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
        } else {
            nutHoanThanh.setEnabled(false);
            nutHoanThanh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
        }
    }

    // --- KẾT THÚC & LƯU ---
    private void chuyenSangTrangKetQua() {
        boXuLy.removeCallbacks(tacVuDemGio); // Dừng đồng hồ

        int soCauDung = 0;
        int tongSoCau = danhSachCauHoi.size();

        for (CauHoiVietResponse q : danhSachCauHoi) {
            String userAns = q.getDapAnNguoiDung();
            String correctAns = q.getDapAnMau(); // Lấy đáp án mẫu từ Model

            // Kiểm tra xem người dùng có viết gì không
            boolean daLamBai = (userAns != null && !userAns.trim().isEmpty());

            if (daLamBai) {
                // TRƯỜNG HỢP 1: Có đáp án mẫu (Bài điền từ / Dịch)
                if (correctAns != null && !correctAns.isEmpty()) {
                    // So sánh không phân biệt hoa thường
                    if (userAns.trim().equalsIgnoreCase(correctAns)) {
                        soCauDung++;
                    }
                }
                // TRƯỜNG HỢP 2: Bài viết tự do (Không có đáp án mẫu)
                else {
                    // Cứ viết là tính điểm
                    soCauDung++;
                }
            }
        }

        // Tính điểm hệ 10
        double diem10 = 0;
        if (tongSoCau > 0) {
            diem10 = ((double) soCauDung / tongSoCau) * 10.0;
            diem10 = Math.round(diem10 * 100.0) / 100.0;
        }

        // Chuyển sang màn hình kết quả
        Intent intent = new Intent(this, KetQuaActivity.class);
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauDung); // Số câu đúng thực tế
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, tongSoCau);
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, soGiayLamBai);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Viết");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, level);
        intent.putExtra("ID_BAI_TAP", idBaiTap);

        // NẾU CẦN LƯU SERVER THÌ GỌI Ở ĐÂY
        // luuKetQua(diem10, soCauDung, tongSoCau);

        startActivity(intent);
        finish();
    }

    private void luuKetQua(double diem, int dung, int tong) {
        // ... (Logic gọi API giống bài Nói, chỉ thay loại kỹ năng là "Viết")
        // Nếu bạn chưa cần lưu thì gọi thẳng hàm chuyển trang:
        moManHinhKetQua(dung, tong);
    }

    private void moManHinhKetQua(int dung, int tong) {
        Intent intent = new Intent(this, KetQuaActivity.class);
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, dung);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, tong);
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, soGiayLamBai);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Viết");
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
        boXuLy.removeCallbacksAndMessages(null);
    }
}