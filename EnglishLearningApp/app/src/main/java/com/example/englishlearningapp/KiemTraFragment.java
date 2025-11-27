package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class KiemTraFragment extends Fragment {

    private boolean denTuTrangChu = false;
    private LinearLayout btnCoBan, btnTrungBinh, btnNangCao;

    private Button btnKhoaHoc, btnKiemTra;
    private CardView btnBatDauKiemTra;
    private LinearLayout khungChonChuDe;
    private TextView tvTieuDeChuDe;

    private String mucDoDaChon = "";
    private String chuDeHienTai = "Đọc";
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
                if (tvTieuDeChuDe != null) {
                    tvTieuDeChuDe.setText(chuDeHienTai);
                }
            }

            denTuTrangChu = getArguments().getBoolean("TU_TRANG_CHU", false);
        }

        caiDatSuKien();
        return view;
    }

    private void anhXaView(View view) {
        // Ánh xạ các nút chọn mức độ
        btnCoBan = view.findViewById(R.id.btn_co_ban);
        btnTrungBinh = view.findViewById(R.id.btn_trung_binh);
        btnNangCao = view.findViewById(R.id.btn_nang_cao);

        // Ánh xạ các nút điều hướng
        btnKhoaHoc = view.findViewById(R.id.btn_khoa_hoc);
        btnKiemTra = view.findViewById(R.id.btn_kiem_tra);

        // Nút bắt đầu
        btnBatDauKiemTra = view.findViewById(R.id.btn_bat_dau_kiem_tra);

        // Ánh xạ các view khác
        khungChonChuDe = view.findViewById(R.id.khung_chon_chu_de);
        tvTieuDeChuDe = view.findViewById(R.id.tv_tieu_de_chu_de);
    }

    private void caiDatSuKien() {

        // --- XỬ LÝ NÚT KHÓA HỌC / QUAY LẠI ---
        if (denTuTrangChu) {
            // Trường hợp 1: Đến từ trang chủ (Click icon) -> Nút thành "Quay lại"
            btnKhoaHoc.setText("Quay lại");

            btnKhoaHoc.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        } else {
            // Trường hợp 2: Mặc định (Click tab menu) -> Nút là "Khóa học"
            btnKhoaHoc.setText("Khóa học");

            btnKhoaHoc.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        // Chọn Mức độ (Level)
        btnCoBan.setOnClickListener(v -> {
            mucDoDaChon = "Basic";
            Toast.makeText(getContext(), "Đã chọn: Cơ bản", Toast.LENGTH_SHORT).show();
        });

        btnTrungBinh.setOnClickListener(v -> {
            mucDoDaChon = "Medium";
            Toast.makeText(getContext(), "Đã chọn: Trung bình", Toast.LENGTH_SHORT).show();
        });

        btnNangCao.setOnClickListener(v -> {
            mucDoDaChon = "Advanced";
            Toast.makeText(getContext(), "Đã chọn: Nâng cao", Toast.LENGTH_SHORT).show();
        });

        khungChonChuDe.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang chọn chủ đề: " + chuDeHienTai, Toast.LENGTH_SHORT).show();
        });

        // >>>>>> BẮT ĐẦU KIỂM TRA <<<<<<
        btnBatDauKiemTra.setOnClickListener(v -> {
            if (mucDoDaChon.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = null;

            // Kiểm tra tên kỹ năng
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
                intent.putExtra("SELECTED_LEVEL", mucDoDaChon);
                intent.putExtra("SELECTED_TOPIC", chuDeHienTai);

                startActivity(intent);
            }
        });
    }
}