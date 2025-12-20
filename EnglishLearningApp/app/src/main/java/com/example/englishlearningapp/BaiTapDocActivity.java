package com.example.englishlearningapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Adapter.CauHoiAdapter;
// QUAN TRỌNG: Đổi sang dùng CauHoiResponse thay vì CauHoiModel
import com.example.englishlearningapp.DTO.Response.CauHoiResponse;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    // Sửa List<CauHoiModel> thành List<CauHoiResponse>
    private List<CauHoiResponse> danhSachCauHoi = new ArrayList<>();
    private CauHoiAdapter adapter;
    private Set<Integer> tapHopIdCauHoiDaTraLoi = new HashSet<>();

    // Biến nhận từ màn hình trước
    private String capDoHienTai = "Basic";
    private int idBaiTapHienTai = -1;
    private String tenBaiTap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_doc);

        // 1. NHẬN DỮ LIỆU TỪ INTENT
        if (getIntent() != null) {
            idBaiTapHienTai = getIntent().getIntExtra("ID_BAI_TAP", -1);
            capDoHienTai = getIntent().getStringExtra("MUC_DO");
            tenBaiTap = getIntent().getStringExtra("TEN_BAI_TAP");
        }

        // 2. ÁNH XẠ VIEW
        anhXaView();

        // 3. THIẾT LẬP RECYCLERVIEW
        setupRecyclerView();

        // 4. GỌI API LẤY CÂU HỎI
        if (idBaiTapHienTai != -1) {
            goiApiLayCauHoi(idBaiTapHienTai);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bài tập!", Toast.LENGTH_SHORT).show();
        }

        // 5. XỬ LÝ SỰ KIỆN
        xuLySuKien();
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
        // Khởi tạo Adapter với Context và Danh sách Response
        adapter = new CauHoiAdapter(this, danhSachCauHoi);
        rcvDanhSachCauHoi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvDanhSachCauHoi.setAdapter(adapter);
    }

    private void xuLySuKien() {
        nutQuayLai.setOnClickListener(v -> finish());
        btnHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());
    }

    // --- GỌI API ---
    private void goiApiLayCauHoi(int baiTapId) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        // Lưu ý: Trong ApiService, bạn phải sửa return type thành Call<List<CauHoiResponse>>
        apiService.getCauHoiByBaiTapId(baiTapId).enqueue(new Callback<List<CauHoiResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiResponse>> call, Response<List<CauHoiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiResponse> listTuServer = response.body();

                    // Duyệt qua từng câu hỏi
                    for (CauHoiResponse ch : listTuServer) {
                        // QUAN TRỌNG: Gọi hàm có sẵn trong Class để parse JSON
                        ch.xuLyDuLieu();
                        danhSachCauHoi.add(ch);
                    }

                    // Cập nhật giao diện
                    adapter.notifyDataSetChanged();

                    // Cập nhật thanh tiến trình
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

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapDocActivity.this, KetQuaActivity.class);

        // Gửi kết quả
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, tapHopIdCauHoiDaTraLoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());

        // --- DÒNG QUAN TRỌNG CÒN THIẾU ---
        // Phải gửi ID bài tập hiện tại sang để bên kia biết mà "Làm lại"
        intent.putExtra("ID_BAI_TAP", idBaiTapHienTai);

        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Đọc");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    // --- SỰ KIỆN TỪ ADAPTER ---
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        if (dapAnDuocChon != null) {
            tapHopIdCauHoiDaTraLoi.add(maCauHoi);
        } else {
            tapHopIdCauHoiDaTraLoi.remove(maCauHoi);
        }
        capNhatTrangThaiTienTrinh();
    }

    private void capNhatTrangThaiTienTrinh() {
        int soCauDaTraLoi = tapHopIdCauHoiDaTraLoi.size();
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