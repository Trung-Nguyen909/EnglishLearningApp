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

    // (MỚI) 2 Spinner
    private Spinner spinnerKyNang; // Chọn Nghe/Nói...
    private Spinner spinnerBaiTap; // Chọn bài tập cụ thể

    // --- BIẾN DỮ LIỆU ---
    private String mucDoDaChon = "";
    private String chuDeHienTai = "Đọc"; // Mặc định
    private BaiTapResponse baiTapDangChon = null;

    // Danh sách kỹ năng cứng để nạp vào Spinner trên
    private String[] danhSachKyNang = {"Nghe", "Nói", "Đọc", "Viết"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_kiemtra, container, false);

        anhXaView(view);

        // Nhận dữ liệu từ trang trước
        if (getArguments() != null) {
            String tenKyNang = getArguments().getString("TEN_CHU_DE");
            if (tenKyNang != null) {
                chuDeHienTai = tenKyNang;
            }
            denTuTrangChu = getArguments().getBoolean("TU_TRANG_CHU", false);
        }

        caiDatSuKien();

        // 1. Cài đặt Spinner Kỹ năng trước
        // Khi Spinner này được chọn -> Nó sẽ tự gọi API lấy bài tập
        caiDatSpinnerKyNang();

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

        // Ánh xạ 2 Spinner (Lưu ý ID tiếng Việt trong XML bên dưới)
        spinnerKyNang = view.findViewById(R.id.spinner_ky_nang);
        spinnerBaiTap = view.findViewById(R.id.spinner_chu_de); // Spinner chọn bài tập
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

        // --- CHỌN MỨC ĐỘ ---
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
            // Kiểm tra Bài tập
            if (baiTapDangChon == null) {
                Toast.makeText(getContext(), "Vui lòng chọn bài tập!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Kiểm tra Mức độ
            if (mucDoDaChon.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = null;
            // Chuyển màn hình dựa theo Kỹ năng đang chọn (chuDeHienTai)
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
                // Truyền dữ liệu (Key Tiếng Việt)
                intent.putExtra("ID_BAI_TAP", baiTapDangChon.getId());
                intent.putExtra("TEN_BAI_TAP", baiTapDangChon.getTenBaiTap());
                intent.putExtra("MUC_DO", mucDoDaChon);
                intent.putExtra("CHU_DE", chuDeHienTai);

                startActivity(intent);
            }
        });
    }

    // --- 1. CÀI ĐẶT SPINNER KỸ NĂNG (NGHE, NÓI...) ---
    private void caiDatSpinnerKyNang() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_spinner_danh_sach,
                R.id.tv_ten_bai_tap,
                danhSachKyNang
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_danh_sach);
        spinnerKyNang.setAdapter(adapter);

        // Tìm vị trí của kỹ năng được truyền sang
        int viTriMacDinh = 0;
        for (int i = 0; i < danhSachKyNang.length; i++) {
            if (danhSachKyNang[i].equalsIgnoreCase(chuDeHienTai)) {
                viTriMacDinh = i;
                break;
            }
        }
        spinnerKyNang.setSelection(viTriMacDinh);

        if (denTuTrangChu) {
            // Nếu từ trang chủ vào -> KHÓA KHÔNG CHO CHỌN
            spinnerKyNang.setEnabled(false);
            spinnerKyNang.setAlpha(0.5f); // Làm mờ đi một chút để biết là đang khóa
        } else {
            // Nếu từ nơi khác vào -> CHO PHÉP CHỌN BÌNH THƯỜNG
            spinnerKyNang.setEnabled(true);
            spinnerKyNang.setAlpha(1.0f);
        }
        // ============================================================

        spinnerKyNang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chuDeHienTai = danhSachKyNang[position];
                if(tvTieuDeChuDe != null) tvTieuDeChuDe.setText(chuDeHienTai);

                // Gọi API lấy bài tập tương ứng
                goiApiLayBaiTap(chuDeHienTai);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // --- 2. GỌI API LẤY BÀI TẬP THEO KỸ NĂNG ---
    private void goiApiLayBaiTap(String kynang) {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        apiService.getBaiTapByLoai(kynang).enqueue(new Callback<List<BaiTapResponse>>() {
            @Override
            public void onResponse(Call<List<BaiTapResponse>> call, Response<List<BaiTapResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BaiTapResponse> listBaiTap = response.body();
                    xuLyHienThiLenSpinnerBaiTap(listBaiTap);
                } else {
                    Toast.makeText(getContext(), "Không có bài tập nào", Toast.LENGTH_SHORT).show();
                    // Nếu không có bài tập, xóa danh sách cũ đi
                    xuLyHienThiLenSpinnerBaiTap(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<BaiTapResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
            }
        });
    }

    // --- 3. ĐỔ DỮ LIỆU VÀO SPINNER BÀI TẬP ---
    private void xuLyHienThiLenSpinnerBaiTap(List<BaiTapResponse> listBaiTap) {
        List<String> listTenBai = new ArrayList<>();
        for (BaiTapResponse bt : listBaiTap) {
            listTenBai.add(bt.getTenBaiTap());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_spinner_danh_sach,
                R.id.tv_ten_bai_tap,
                listTenBai
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_danh_sach);

        if (spinnerBaiTap != null) {
            spinnerBaiTap.setAdapter(adapter);
            spinnerBaiTap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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