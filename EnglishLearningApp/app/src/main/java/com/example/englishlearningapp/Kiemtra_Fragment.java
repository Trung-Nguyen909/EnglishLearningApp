package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Kiemtra_Fragment extends Fragment {

    private ImageView btnBack;
    private LinearLayout btnBasic, btnMedium, btnAdvanced;
    private LinearLayout dropdownTopics;
    private CardView btnStartTest;

    // Biến này sẽ trỏ vào cái Tiêu đề "Chọn chủ đề(tùy chọn)"
    private TextView tvTenChuDe;

    private String selectedLevel = "";
    private String currentTopic = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);

        initViews(view);

        // >>> NHẬN DỮ LIỆU TỪ TRANG CHỦ <<<
        if (getArguments() != null) {
            String skillName = getArguments().getString("TEN_CHU_DE");
            if (skillName != null) {
                currentTopic = skillName;

                // Thay đổi tiêu đề thành tên kỹ năng (Ví dụ: "Listening")
                if (tvTenChuDe != null) {
                    tvTenChuDe.setText(currentTopic);
                }
            }
        }

        setupListeners();
        return view;
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btn_back);
        btnBasic = view.findViewById(R.id.btn_basic);
        btnMedium = view.findViewById(R.id.btn_medium);
        btnAdvanced = view.findViewById(R.id.btn_advanced);
        dropdownTopics = view.findViewById(R.id.dropdown_topics);
        btnStartTest = view.findViewById(R.id.btn_start_test);
        tvTenChuDe = view.findViewById(R.id.tv_header_topic);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnBasic.setOnClickListener(v -> {
            selectedLevel = "Basic - 10 câu";
            Toast.makeText(getContext(), "Đã chọn: Basic", Toast.LENGTH_SHORT).show();
        });

        btnMedium.setOnClickListener(v -> {
            selectedLevel = "Medium - 20 câu";
            Toast.makeText(getContext(), "Đã chọn: Medium", Toast.LENGTH_SHORT).show();
        });

        btnAdvanced.setOnClickListener(v -> {
            selectedLevel = "Advanced - 30 câu";
            Toast.makeText(getContext(), "Đã chọn: Advanced", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện click vào dropdown vẫn giữ nguyên (Hiển thị toast hoặc mở dialog chọn sau này)
        dropdownTopics.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mở danh sách chủ đề con của: " + (currentTopic.isEmpty() ? "Tất cả" : currentTopic), Toast.LENGTH_SHORT).show();
        });

        btnStartTest.setOnClickListener(v -> {
            if (selectedLevel.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
            } else {
                String message = "Bắt đầu: " + (currentTopic.isEmpty() ? "Tất cả chủ đề" : currentTopic) + " - " + selectedLevel;
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}