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
import androidx.fragment.app.FragmentTransaction; // Import thêm để chuyển Fragment
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.KyNang;
import com.example.englishlearningapp.Model.ItemNgay;

import java.util.ArrayList;
import java.util.List;

public class TrangChu_Fragment extends Fragment {

    private RecyclerView rvQuickTest, rvCalendar;
    private KyNangAdapter adapter;

    // Khai báo nút Tiếp tục học
    private AppCompatButton btnTiepTucHoc;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp layout
        View view = inflater.inflate(R.layout.activity_trang_chu, container, false);

        // 2. Ánh xạ View
        rvQuickTest = view.findViewById(R.id.rv_quick_test);
        rvCalendar = view.findViewById(R.id.recycler_calendar);

        // --- ÁNH XẠ NÚT TIẾP TỤC HỌC ---
        btnTiepTucHoc = view.findViewById(R.id.id_tieptuchoc);

        // --- XỬ LÝ SỰ KIỆN CLICK "TIẾP TỤC HỌC" ---
        if (btnTiepTucHoc != null) {
            btnTiepTucHoc.setOnClickListener(v -> {
                // Chuyển sang màn hình Bài Học
                Intent intent = new Intent(getContext(), BaihocActivity.class);

                // Truyền dữ liệu cho bài "Class" (Theo hình ảnh mẫu)
                intent.putExtra("SUB_ITEM_ID", 1);
                intent.putExtra("SUB_ITEM_NAME", "Family Members");

                startActivity(intent);
            });
        }

        // --- XỬ LÝ PHẦN KỸ NĂNG (QUICK TEST) ---
        List<KyNang> listData = new ArrayList<>();
        listData.add(new KyNang("Listening", R.drawable.ic_listening));
        listData.add(new KyNang("Speaking", R.drawable.ic_speaking));
        listData.add(new KyNang("Reading", R.drawable.ic_reading));
        listData.add(new KyNang("Writing", R.drawable.ic_writing));

        adapter = new KyNangAdapter(getContext(), listData);

        // >>>>>> PHẦN MỚI THÊM VÀO: SỰ KIỆN CLICK KỸ NĂNG <<<<<<
        adapter.setOnItemClickListener(new KyNangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(KyNang kyNang) {
                // 1. Tạo Fragment Kiểm tra
                Kiemtra_Fragment kiemtraFragment = new Kiemtra_Fragment();

                // 2. Đóng gói dữ liệu (Tên kỹ năng) để gửi đi
                Bundle args = new Bundle();

                // LƯU Ý: Kiểm tra Model KyNang của bạn dùng hàm nào: getTitle() hay getTenKyNang()
                // Hãy dùng đúng tên hàm trong Model của bạn
                args.putString("TEN_CHU_DE", kyNang.getTitle());

                kiemtraFragment.setArguments(args);

                // 3. Thực hiện chuyển màn hình (Fragment)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, kiemtraFragment) // Thay thế nội dung trong MainActivity
                        .addToBackStack(null) // Cho phép bấm Back để quay lại
                        .commit();
            }
        });
        // >>>>>> KẾT THÚC PHẦN THÊM MỚI <<<<<<

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvQuickTest.setLayoutManager(gridLayoutManager);
        rvQuickTest.setAdapter(adapter);

        // --- XỬ LÝ PHẦN LỊCH (CALENDAR) - GIỮ NGUYÊN ---
        List<ItemNgay> ngayList = new ArrayList<>();
        ngayList.add(new ItemNgay("29", "grayMonth"));
        ngayList.add(new ItemNgay("30", "grayMonth"));
        for (int i = 1; i <= 19; i++) {
            ngayList.add(new ItemNgay(String.valueOf(i), "blue"));
        }
        ngayList.add(new ItemNgay("20", "red"));
        ngayList.add(new ItemNgay("21", "red"));
        ngayList.add(new ItemNgay("22", "gray"));
        for (int i = 23; i <= 31; i++) {
            ngayList.add(new ItemNgay(String.valueOf(i), "normal"));
        }
        ngayList.add(new ItemNgay("1", "grayMonth"));
        ngayList.add(new ItemNgay("2", "grayMonth"));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        rvCalendar.setLayoutManager(layoutManager);
        NgayAdapter ngayAdapter = new NgayAdapter(ngayList);
        rvCalendar.setAdapter(ngayAdapter);

        return view;
    }
}