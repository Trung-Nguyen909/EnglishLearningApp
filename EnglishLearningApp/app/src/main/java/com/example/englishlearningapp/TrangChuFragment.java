package com.example.englishlearningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.KyNangModel;
import com.example.englishlearningapp.Model.NgayModel;

import java.util.ArrayList;
import java.util.List;

public class TrangChuFragment extends Fragment {

    private RecyclerView rcvKiemTraNhanh, rcvLich;
    private KyNangAdapter adapterKyNang;
    private AppCompatButton btnTiepTucHoc;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trang_chu, container, false);

        // 2. Ánh xạ View
        rcvKiemTraNhanh = view.findViewById(R.id.rcv_kiem_tra_nhanh);
        rcvLich = view.findViewById(R.id.rcv_lich);
        btnTiepTucHoc = view.findViewById(R.id.btn_tiep_tuc_hoc);

        // --- XỬ LÝ SỰ KIỆN CLICK "TIẾP TỤC HỌC" ---
        if (btnTiepTucHoc != null) {
            btnTiepTucHoc.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), BaiHocActivity.class);
                intent.putExtra("SUB_ITEM_ID", 1);
                intent.putExtra("SUB_ITEM_NAME", "Thành viên trong gia đình");
                startActivity(intent);
            });
        }

        // --- XỬ LÝ PHẦN KỸ NĂNG (QUICK TEST) ---
        List<KyNangModel> danhSachKyNang = new ArrayList<>();
        danhSachKyNang.add(new KyNangModel("Nghe", R.drawable.ic_listening));
        danhSachKyNang.add(new KyNangModel("Nói", R.drawable.ic_speaking));
        danhSachKyNang.add(new KyNangModel("Đọc", R.drawable.ic_reading));
        danhSachKyNang.add(new KyNangModel("Viết", R.drawable.ic_writing));

        adapterKyNang = new KyNangAdapter(getContext(), danhSachKyNang);

        // BẮT SỰ KIỆN CLICK KỸ NĂNG
        adapterKyNang.setLangNgheSuKienClick(new KyNangAdapter.LangNgheSuKienClick() {
            @Override
            public void khiClickVaoItem(KyNangModel kyNang) {
                // Lấy tên kỹ năng (Listening, Reading, Speaking, Writing)
                String tenKyNang = kyNang.getTenKyNang();

                // 1. Tạo Fragment
                KiemTraFragment kiemtraFragment = new KiemTraFragment();

                // 2. Đóng gói dữ liệu để gửi sang Fragment kia
                Bundle goiDuLieu = new Bundle();
                goiDuLieu.putString("TEN_CHU_DE", tenKyNang);
                kiemtraFragment.setArguments(goiDuLieu);

                // 3. Thực hiện chuyển đổi Fragment
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, kiemtraFragment) // Thay thế layout hiện tại
                            .addToBackStack(null) // Cho phép ấn Back để quay lại
                            .commit();
                }
            }
        });

        GridLayoutManager gridManagerKyNang = new GridLayoutManager(getContext(), 4);
        rcvKiemTraNhanh.setLayoutManager(gridManagerKyNang);
        rcvKiemTraNhanh.setAdapter(adapterKyNang);

        // --- XỬ LÝ PHẦN LỊCH (CALENDAR) ---
        List<NgayModel> danhSachNgay = new ArrayList<>();
        danhSachNgay.add(new NgayModel("29", "grayMonth"));
        danhSachNgay.add(new NgayModel("30", "grayMonth"));
        for (int i = 1; i <= 19; i++) {
            danhSachNgay.add(new NgayModel(String.valueOf(i), "blue"));
        }
        danhSachNgay.add(new NgayModel("20", "red"));
        danhSachNgay.add(new NgayModel("21", "red"));
        danhSachNgay.add(new NgayModel("22", "gray"));
        for (int i = 23; i <= 31; i++) {
            danhSachNgay.add(new NgayModel(String.valueOf(i), "normal"));
        }
        danhSachNgay.add(new NgayModel("1", "grayMonth"));
        danhSachNgay.add(new NgayModel("2", "grayMonth"));

        GridLayoutManager gridManagerLich = new GridLayoutManager(getContext(), 7);
        rcvLich.setLayoutManager(gridManagerLich);

        NgayAdapter adapterNgay = new NgayAdapter(danhSachNgay);
        rcvLich.setAdapter(adapterNgay);

        return view;
    }
}