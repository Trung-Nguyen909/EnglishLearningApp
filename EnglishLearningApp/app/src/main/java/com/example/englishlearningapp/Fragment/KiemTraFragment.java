package com.example.englishlearningapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.BaiTapDocActivity;
import com.example.englishlearningapp.BaiTapNgheActivity;
import com.example.englishlearningapp.BaiTapNoiActivity;
import com.example.englishlearningapp.BaiTapVietActivity;
import com.example.englishlearningapp.DTO.Response.BaiTapResponse;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KiemTraFragment extends Fragment {

    // --- BIẾN ĐIỀU HƯỚNG ---
    private boolean denTuTrangChu = false;

    // --- BIẾN UI ---
    private LinearLayout btnCoBan, btnTrungBinh, btnNangCao;
    private Button btnKhoaHoc, btnKiemTra;
    private CardView btnBatDauKiemTra;
    private TextView tvTieuDeChuDe;
    private Spinner spinnerChuDe;

    // --- BIẾN DỮ LIỆU ---
    private String mucDoDaChon = ""; // Biến này rỗng nghĩa là chưa chọn mức độ
    private String chuDeHienTai = "Đọc";
    private BaiTapResponse baiTapDangChon = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_kiemtra, container, false);

        anhXaView(view);

        if (getArguments() != null) {
            String tenKyNang = getArguments().getString("TEN_CHU_DE");
            if (tenKyNang != null) {
                chuDeHienTai = tenKyNang;
                if (tvTieuDeChuDe != null) tvTieuDeChuDe.setText(chuDeHienTai);
            }
            denTuTrangChu = getArguments().getBoolean("TU_TRANG_CHU", false);
        }

        caiDatSuKien();
        goiApiLayBaiTap(chuDeHienTai);

        return view;
    }

    private void anhXaView(View view) {
        btnCoBan = view.findViewById(R.id.btn_co_ban);
        btnTrungBinh = view.findViewById(R.id.btn_trung_binh);
        btnNangCao = view.findViewById(R.id.btn_nang_cao);
        btnKhoaHoc = view.findViewById(R.id.btn_khoa_hoc);
        btnKiemTra = view.findViewById(R.id.btn_kiem_tra);
        btnBatDauKiemTra = view.findViewById(R.id.btn_bat_dau_kiem_tra);
        tvTieuDeChuDe = view.findViewById(R.id.tv_tieu_de_chu_de);
        spinnerChuDe = view.findViewById(R.id.spinner_chu_de);
    }

    private void caiDatSuKien() {
        // --- NÚT QUAY LẠI / KHÓA HỌC ---
        if (denTuTrangChu) {
            btnKhoaHoc.setText("Quay lại");
            btnKhoaHoc.setOnClickListener(v -> {
                if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
            });
        } else {
            btnKhoaHoc.setText("Khóa học");
            btnKhoaHoc.setOnClickListener(v -> {
                if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
            });
        }

        // --- CHỌN MỨC ĐỘ (Lưu giá trị tiếng Việt không dấu) ---
        btnCoBan.setOnClickListener(v -> {
            mucDoDaChon = "CoBan";
            Toast.makeText(getContext(), "Đã chọn: Cơ bản", Toast.LENGTH_SHORT).show();
        });
        btnTrungBinh.setOnClickListener(v -> {
            mucDoDaChon = "TrungBinh";
            Toast.makeText(getContext(), "Đã chọn: Trung bình", Toast.LENGTH_SHORT).show();
        });
        btnNangCao.setOnClickListener(v -> {
            mucDoDaChon = "NangCao";
            Toast.makeText(getContext(), "Đã chọn: Nâng cao", Toast.LENGTH_SHORT).show();
        });

        // --- NÚT BẮT ĐẦU ---
        btnBatDauKiemTra.setOnClickListener(v -> {
            // 1. Kiểm tra Bài tập
            if (baiTapDangChon == null) {
                Toast.makeText(getContext(), "Vui lòng chọn bài tập!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. KIỂM TRA MỨC ĐỘ (MỚI THÊM VÀO)
            if (mucDoDaChon.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn mức độ (Cơ bản/Trung bình/Cao cấp)!", Toast.LENGTH_SHORT).show();
                return; // Dừng lại, không cho chuyển màn hình
            }

            Intent intent = null;
            switch (chuDeHienTai) {
                case "Nghe":
                    intent = new Intent(getContext(), BaiTapNgheActivity.class);
                    break;
                case "Nói":
                    intent = new Intent(getContext(), BaiTapNoiActivity.class);
                    break;
                case "Viết":
                    intent = new Intent(getContext(), BaiTapVietActivity.class);
                    break;
                case "Đọc":
                default:
                    intent = new Intent(getContext(), BaiTapDocActivity.class);
                    break;
            }

            if (intent != null) {
                // Truyền dữ liệu sang màn hình làm bài (Key Tiếng Việt)
                intent.putExtra("ID_BAI_TAP", baiTapDangChon.getId());
                intent.putExtra("TEN_BAI_TAP", baiTapDangChon.getTenBaiTap());
                intent.putExtra("MUC_DO", mucDoDaChon);
                intent.putExtra("CHU_DE", chuDeHienTai);

                startActivity(intent);
            }
        });
    }

    // --- HÀM GỌI API ---
    private void goiApiLayBaiTap(String kynang) {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getBaiTapByLoai(kynang).enqueue(new Callback<List<BaiTapResponse>>() {
            @Override
            public void onResponse(Call<List<BaiTapResponse>> call, Response<List<BaiTapResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    xuLyHienThiLenSpinner(response.body());
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy bài tập nào", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<BaiTapResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
            }
        });
    }

    // --- HÀM ĐỔ DỮ LIỆU SPINNER (Dùng layout custom) ---
    private void xuLyHienThiLenSpinner(List<BaiTapResponse> listBaiTap) {
        List<String> listTenBai = new ArrayList<>();
        for (BaiTapResponse bt : listBaiTap) {
            listTenBai.add(bt.getTenBaiTap());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_spinner_danh_sach, // File layout dòng (Nhớ phải tạo file này rồi nhé)
                R.id.tv_ten_bai_tap,             // ID trong file đó
                listTenBai
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_danh_sach);

        if (spinnerChuDe != null) {
            spinnerChuDe.setAdapter(adapter);
            spinnerChuDe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    baiTapDangChon = listBaiTap.get(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    baiTapDangChon = null;
                }
            });
        }
    }
}