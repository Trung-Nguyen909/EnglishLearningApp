package com.example.englishlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.ItemBaiTap;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvQuickTest;
    private ItemBaiTapAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ RecyclerView từ layout activity_main.xml
        rvQuickTest = findViewById(R.id.rv_quick_test);

        // 2. Tạo danh sách dữ liệu (4 món)
        List<ItemBaiTap> listData = new ArrayList<>();
        // Thay R.drawable.ic_... bằng tên hình ảnh thực tế của bạn
        listData.add(new ItemBaiTap("Listening", R.drawable.ic_listening));
        listData.add(new ItemBaiTap("Speaking", R.drawable.ic_speaking));
        listData.add(new ItemBaiTap("Reading", R.drawable.ic_reading));
        listData.add(new ItemBaiTap("Writing", R.drawable.ic_writing));

        // 3. Setup Adapter
        adapter = new ItemBaiTapAdapter(this, listData);

        // 4. Setup Layout (Dạng lưới 4 cột)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvQuickTest.setLayoutManager(gridLayoutManager);

        // 5. Gán adapter vào RecyclerView
        rvQuickTest.setAdapter(adapter);
    }
}