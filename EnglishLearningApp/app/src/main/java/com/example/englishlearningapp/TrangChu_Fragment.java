package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Thay AppCompatActivity bằng Fragment
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.KyNang;
import com.example.englishlearningapp.Model.ItemNgay;

import java.util.ArrayList;
import java.util.List;

public class TrangChu_Fragment extends Fragment {

    private RecyclerView rvQuickTest, rvCalendar;
    private KyNangAdapter adapter;

    // Fragment sử dụng onCreateView thay vì onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp layout (Thay cho setContentView)
        // Vẫn dùng lại file xml cũ: activity_trang_chu
        View view = inflater.inflate(R.layout.activity_trang_chu, container, false);

        // 2. Ánh xạ View (Phải có "view." đằng trước findViewById)
        rvQuickTest = view.findViewById(R.id.rv_quick_test);
        rvCalendar = view.findViewById(R.id.recycler_calendar);

        // --- XỬ LÝ PHẦN KỸ NĂNG (QUICK TEST) ---
        List<KyNang> listData = new ArrayList<>();
        // Lưu ý: Đảm bảo bạn có các hình ảnh này trong thư mục drawable
        listData.add(new KyNang("Listening", R.drawable.ic_listening));
        listData.add(new KyNang("Speaking", R.drawable.ic_speaking));
        listData.add(new KyNang("Reading", R.drawable.ic_reading));
        listData.add(new KyNang("Writing", R.drawable.ic_writing));

        // 3. Setup Adapter
        // Lưu ý: Trong Fragment, dùng getContext() thay cho "this"
        adapter = new KyNangAdapter(getContext(), listData);

        // 4. Setup Layout (Dạng lưới 4 cột)
        // Lưu ý: Dùng getContext() thay cho "this"
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvQuickTest.setLayoutManager(gridLayoutManager);

        // 5. Gán adapter vào RecyclerView
        rvQuickTest.setAdapter(adapter);


        // --- XỬ LÝ PHẦN LỊCH (CALENDAR) ---
        List<ItemNgay> ngayList = new ArrayList<>();

        ngayList.add(new ItemNgay("29", "grayMonth"));
        ngayList.add(new ItemNgay("30", "grayMonth"));

        // Ngày 1-19: Blue
        for (int i = 1; i <= 19; i++) {
            ngayList.add(new ItemNgay(String.valueOf(i), "blue"));
        }

        // Ngày 20, 21: Red
        ngayList.add(new ItemNgay("20", "red"));
        ngayList.add(new ItemNgay("21", "red"));

        // Ngày 22: Gray
        ngayList.add(new ItemNgay("22", "gray"));

        // Ngày 23-31: Normal
        for (int i = 23; i <= 31; i++) {
            ngayList.add(new ItemNgay(String.valueOf(i), "normal"));
        }

        ngayList.add(new ItemNgay("1", "grayMonth"));
        ngayList.add(new ItemNgay("2", "grayMonth"));

        // Context: Thay "this" bằng "getContext()"
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        rvCalendar.setLayoutManager(layoutManager);

        NgayAdapter ngayAdapter = new NgayAdapter(ngayList);
        rvCalendar.setAdapter(ngayAdapter);

        return view;
    }
}