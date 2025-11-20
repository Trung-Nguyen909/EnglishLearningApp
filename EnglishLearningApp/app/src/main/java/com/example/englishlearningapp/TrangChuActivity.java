package com.example.englishlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.KyNang;
import com.example.englishlearningapp.Model.ItemNgay;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private RecyclerView rvQuickTest, rvCalendar;
    private KyNangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ RecyclerView từ layout activity_main.xml
        rvQuickTest = findViewById(R.id.rv_quick_test);

        // 2. Tạo danh sách dữ liệu (4 món)
        List<KyNang> listData = new ArrayList<>();
        // Thay R.drawable.ic_... bằng tên hình ảnh thực tế của bạn
        listData.add(new KyNang("Listening", R.drawable.ic_listening));
        listData.add(new KyNang("Speaking", R.drawable.ic_speaking));
        listData.add(new KyNang("Reading", R.drawable.ic_reading));
        listData.add(new KyNang("Writing", R.drawable.ic_writing));

        // 3. Setup Adapter
        adapter = new KyNangAdapter(this, listData);

        // 4. Setup Layout (Dạng lưới 4 cột)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvQuickTest.setLayoutManager(gridLayoutManager);

        // 5. Gán adapter vào RecyclerView
        rvQuickTest.setAdapter(adapter);

        rvCalendar = findViewById(R.id.recycler_calendar);

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);
        rvCalendar.setLayoutManager(layoutManager);
        NgayAdapter ngayAdapter = new NgayAdapter(ngayList);
        rvCalendar.setAdapter(ngayAdapter);
    }
}