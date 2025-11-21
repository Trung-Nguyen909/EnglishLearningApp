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

import com.example.englishlearningapp.Model.KyNang;
import com.example.englishlearningapp.Model.Ngay;

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
                Intent intent = new Intent(getContext(), BaihocActivity.class);
                intent.putExtra("SUB_ITEM_ID", 1);
                intent.putExtra("SUB_ITEM_NAME", "Family Members");
                startActivity(intent);
            });
        }

        // --- XỬ LÝ PHẦN KỸ NĂNG (QUICK TEST) ---
        List<KyNang> danhSachKyNang = new ArrayList<>();
        danhSachKyNang.add(new KyNang("Listening", R.drawable.ic_listening));
        danhSachKyNang.add(new KyNang("Speaking", R.drawable.ic_speaking));
        danhSachKyNang.add(new KyNang("Reading", R.drawable.ic_reading));
        danhSachKyNang.add(new KyNang("Writing", R.drawable.ic_writing));

        adapterKyNang = new KyNangAdapter(getContext(), danhSachKyNang);

        // BẮT SỰ KIỆN CLICK KỸ NĂNG
        adapterKyNang.setLangNgheSuKienClick(new KyNangAdapter.LangNgheSuKienClick() {
            @Override
            public void khiClickVaoItem(KyNang kyNang) {
                String tenKyNang = kyNang.getTenKyNang(); // Lấy tên kỹ năng

                if (tenKyNang.equals("Listening")) {
                    // Nếu chọn Listening -> Chuyển sang BaiTapNgheActivity
                    Intent intent = new Intent(getContext(), BaiTapNgheActivity.class);
                    intent.putExtra("SELECTED_LEVEL", "Intermediate");
                    startActivity(intent);
                }
                else if (tenKyNang.equals("Reading")) {
                    // Nếu chọn Reading -> Chuyển sang BaiTapActivity
                    Intent intent = new Intent(getContext(), BaiTapDocActivity.class);
                    intent.putExtra("SELECTED_LEVEL", "Basic");
                    startActivity(intent);
                }
                else {
                    // Các kỹ năng khác -> Chuyển sang Fragment Kiểm Tra
                    Kiemtra_Fragment kiemtraFragment = new Kiemtra_Fragment();
                    Bundle goiDuLieu = new Bundle();
                    goiDuLieu.putString("TEN_CHU_DE", tenKyNang);
                    kiemtraFragment.setArguments(goiDuLieu);

                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, kiemtraFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        GridLayoutManager gridManagerKyNang = new GridLayoutManager(getContext(), 4);
        rcvKiemTraNhanh.setLayoutManager(gridManagerKyNang);
        rcvKiemTraNhanh.setAdapter(adapterKyNang);

        // --- XỬ LÝ PHẦN LỊCH (CALENDAR) ---
        List<Ngay> danhSachNgay = new ArrayList<>();
        danhSachNgay.add(new Ngay("29", "grayMonth"));
        danhSachNgay.add(new Ngay("30", "grayMonth"));
        for (int i = 1; i <= 19; i++) {
            danhSachNgay.add(new Ngay(String.valueOf(i), "blue"));
        }
        danhSachNgay.add(new Ngay("20", "red"));
        danhSachNgay.add(new Ngay("21", "red"));
        danhSachNgay.add(new Ngay("22", "gray"));
        for (int i = 23; i <= 31; i++) {
            danhSachNgay.add(new Ngay(String.valueOf(i), "normal"));
        }
        danhSachNgay.add(new Ngay("1", "grayMonth"));
        danhSachNgay.add(new Ngay("2", "grayMonth"));

        GridLayoutManager gridManagerLich = new GridLayoutManager(getContext(), 7);
        rcvLich.setLayoutManager(gridManagerLich);

        NgayAdapter adapterNgay = new NgayAdapter(danhSachNgay);
        rcvLich.setAdapter(adapterNgay);

        return view;
    }
}