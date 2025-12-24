package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Adapter.CauHoiAdapter;
import com.example.englishlearningapp.DTO.Response.CauHoiResponse;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiTapDocActivity extends AppCompatActivity implements CauHoiAdapter.LangNgheSuKienChonDapAn {

    // --- BIẾN GIAO DIỆN ---
    private Button btnHoanThanh;
    private ProgressBar thanhTienTrinh;
    private TextView tvDemSoCauHoi;
    private TextView tvPhanTramTienTrinh;
    private RecyclerView rcvDanhSachCauHoi;
    private ImageView nutQuayLai;

    // --- BIẾN DỮ LIỆU ---
    private List<CauHoiResponse> danhSachCauHoi = new ArrayList<>();
    private CauHoiAdapter adapter;

    // SỬA ĐỔI 1: Dùng Map để lưu (ID Câu hỏi -> Đáp án người dùng chọn)
    private Map<Integer, String> danhSachDapAnNguoiDung = new HashMap<>();

    private String capDoHienTai = "Basic";
    private int idBaiTapHienTai = -1;
    private String tenBaiTap = "";

    private int soGiayLamBai = 0; // Biến lưu tổng số giây
    private Handler boDemGio = new Handler();
    private Runnable tacVuDemGio = new Runnable() {
        @Override
        public void run() {
            soGiayLamBai++; // Tăng 1 giây

            // Nếu muốn hiện lên màn hình thì setText ở đây:
            // tvDongHo.setText(dinhDangGio(soGiayLamBai));

            boDemGio.postDelayed(this, 1000); // Lặp lại sau 1 giây
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_doc);

        if (getIntent() != null) {
            idBaiTapHienTai = getIntent().getIntExtra("ID_BAI_TAP", -1);
            capDoHienTai = getIntent().getStringExtra("MUC_DO");
            tenBaiTap = getIntent().getStringExtra("TEN_BAI_TAP");
        }

        anhXaView();
        setupRecyclerView();

        if (idBaiTapHienTai != -1) {
            goiApiLayCauHoi(idBaiTapHienTai);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bài tập!", Toast.LENGTH_SHORT).show();
        }

        xuLySuKien();
        batDauDemGio();
    }

    private void batDauDemGio() {
        soGiayLamBai = 0;
        boDemGio.postDelayed(tacVuDemGio, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (boDemGio != null) {
            boDemGio.removeCallbacks(tacVuDemGio);
        }
    }

    private void anhXaView() {
        rcvDanhSachCauHoi = findViewById(R.id.rcv_danh_sach_cau_hoi);
        nutQuayLai = findViewById(R.id.nut_quay_lai);
        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);
        thanhTienTrinh = findViewById(R.id.thanh_tien_trinh);
        tvDemSoCauHoi = findViewById(R.id.tv_dem_so_cau_hoi);
        tvPhanTramTienTrinh = findViewById(R.id.tv_phan_tram_tien_trinh);
    }

    private void setupRecyclerView() {
        adapter = new CauHoiAdapter(this, danhSachCauHoi);
        rcvDanhSachCauHoi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvDanhSachCauHoi.setAdapter(adapter);
    }

    private void xuLySuKien() {
        nutQuayLai.setOnClickListener(v -> finish());

        // Bấm nút hoàn thành sẽ tính điểm
        btnHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());
    }

    private void goiApiLayCauHoi(int baiTapId) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getCauHoiByBaiTapId(baiTapId).enqueue(new Callback<List<CauHoiResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiResponse>> call, Response<List<CauHoiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiResponse> listTuServer = response.body();

                    for (CauHoiResponse ch : listTuServer) {
                        ch.xuLyDuLieu(); // Parse JSON để lấy đáp án A, B, C, D và Correct
                        danhSachCauHoi.add(ch);
                    }

                    adapter.notifyDataSetChanged();
                    thanhTienTrinh.setMax(danhSachCauHoi.size());
                    capNhatTrangThaiTienTrinh();

                } else {
                    Toast.makeText(BaiTapDocActivity.this, "Bài tập này chưa có câu hỏi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CauHoiResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(BaiTapDocActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- SỬA ĐỔI 2: LOGIC TÍNH ĐIỂM CHÍNH XÁC ---
    private void chuyenSangTrangKetQua() {
        boDemGio.removeCallbacks(tacVuDemGio);
        int soCauDung = 0;

        // Duyệt qua tất cả câu hỏi trong danh sách
        for (CauHoiResponse cauHoi : danhSachCauHoi) {
            int id = cauHoi.getId();

            // Lấy đáp án đúng của câu hỏi (Từ JSON database đã xử lý)
            String dapAnDungHeThong = cauHoi.getDapAnDung(); // Ví dụ: "Father"

            // Lấy đáp án người dùng chọn từ Map
            String dapAnNguoiDungChon = danhSachDapAnNguoiDung.get(id); // Ví dụ: "Father"

            // So sánh (Dùng equals để so sánh chuỗi)
            if (dapAnNguoiDungChon != null && dapAnDungHeThong != null) {
                if (dapAnNguoiDungChon.trim().equalsIgnoreCase(dapAnDungHeThong.trim())) {
                    soCauDung++;
                }
            }
        }

        Intent intent = new Intent(BaiTapDocActivity.this, KetQuaActivity.class);

        // Truyền kết quả CHÍNH XÁC vừa tính được
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauDung);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());

        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, soGiayLamBai);

        intent.putExtra("ID_BAI_TAP", idBaiTapHienTai);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Đọc");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    // --- SỬA ĐỔI 3: CẬP NHẬT MAP KHI CHỌN ĐÁP ÁN ---
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        if (dapAnDuocChon != null) {
            // Lưu vào Map: ID câu hỏi -> Nội dung chọn (Ví dụ: 1 -> "Father")
            danhSachDapAnNguoiDung.put(maCauHoi, dapAnDuocChon);
        } else {
            danhSachDapAnNguoiDung.remove(maCauHoi);
        }
        capNhatTrangThaiTienTrinh();
    }

    private void capNhatTrangThaiTienTrinh() {
        // Đếm số lượng phần tử trong Map để biết đã làm bao nhiêu câu
        int soCauDaTraLoi = danhSachDapAnNguoiDung.size();
        int tongSoCau = danhSachCauHoi.size();

        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauDaTraLoi / (float) tongSoCau) * 100) : 0;

        tvDemSoCauHoi.setText("Câu " + soCauDaTraLoi + "/" + tongSoCau);
        tvPhanTramTienTrinh.setText(phanTram + "%");
        thanhTienTrinh.setProgress(soCauDaTraLoi);

        if (soCauDaTraLoi == tongSoCau && tongSoCau > 0) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.royalBlue)));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
        }
    }
}