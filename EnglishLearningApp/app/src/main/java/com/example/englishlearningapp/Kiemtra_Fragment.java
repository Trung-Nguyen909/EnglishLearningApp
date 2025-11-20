package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment; // Import Fragment

public class Kiemtra_Fragment extends Fragment {

    private ImageView btnBack;
    private LinearLayout btnBasic, btnMedium, btnAdvanced;
    private LinearLayout dropdownTopics;
    private CardView btnStartTest;

    // Biến lưu trạng thái
    private String selectedLevel = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp giao diện từ file layout test.xml
        View view = inflater.inflate(R.layout.test, container, false);

        // 2. Gọi hàm khởi tạo view và listener
        initViews(view);
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        // Phải có "view." đằng trước findViewById
        btnBack = view.findViewById(R.id.btn_back);
        btnBasic = view.findViewById(R.id.btn_basic);
        btnMedium = view.findViewById(R.id.btn_medium);
        btnAdvanced = view.findViewById(R.id.btn_advanced);
        dropdownTopics = view.findViewById(R.id.dropdown_topics);
        btnStartTest = view.findViewById(R.id.btn_start_test);
    }

    private void setupListeners() {
        // Nút Back: Quay lại màn hình trước đó
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        // Các nút chọn Level
        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Basic - 10 câu";
                // Dùng getContext() thay cho TestActivity.this
                Toast.makeText(getContext(), "Đã chọn: Basic", Toast.LENGTH_SHORT).show();
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Medium - 20 câu";
                Toast.makeText(getContext(), "Đã chọn: Medium", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Advanced - 30 câu";
                Toast.makeText(getContext(), "Đã chọn: Advanced", Toast.LENGTH_SHORT).show();
            }
        });

        dropdownTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Chọn chủ đề", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLevel.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Bắt đầu Test: " + selectedLevel, Toast.LENGTH_LONG).show();
                    // Tại đây bạn có thể viết lệnh chuyển sang màn hình làm bài thi thật sự
                }
            }
        });
    }
}