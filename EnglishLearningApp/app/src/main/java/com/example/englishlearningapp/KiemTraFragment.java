package com.example.englishlearningapp;

import android.content.Intent;
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

public class KiemTraFragment extends Fragment {

    private ImageView btnBack;
    private LinearLayout btnBasic, btnMedium, btnAdvanced;
    private LinearLayout dropdownTopics;
    private CardView btnStartTest;
    private TextView tvTenChuDe;

    private String selectedLevel = "";
    private String currentTopic = "Reading"; // Mặc định

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_kiemtra, container, false);

        initViews(view);

        // Nhận dữ liệu tên kỹ năng từ trang trước
        if (getArguments() != null) {
            String skillName = getArguments().getString("TEN_CHU_DE");
            if (skillName != null) {
                currentTopic = skillName;
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
        // Nút Back
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Chọn Level (Giữ nguyên logic cũ của bạn)
        btnBasic.setOnClickListener(v -> {
            selectedLevel = "Basic";
            Toast.makeText(getContext(), "Đã chọn: Basic", Toast.LENGTH_SHORT).show();
        });

        btnMedium.setOnClickListener(v -> {
            selectedLevel = "Medium";
            Toast.makeText(getContext(), "Đã chọn: Medium", Toast.LENGTH_SHORT).show();
        });

        btnAdvanced.setOnClickListener(v -> {
            selectedLevel = "Advanced";
            Toast.makeText(getContext(), "Đã chọn: Advanced", Toast.LENGTH_SHORT).show();
        });

        dropdownTopics.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang chọn chủ đề: " + currentTopic, Toast.LENGTH_SHORT).show();
        });

        // >>>>>> SỰ KIỆN QUAN TRỌNG: BẮT ĐẦU KIỂM TRA <<<<<<
        btnStartTest.setOnClickListener(v -> {
            if (selectedLevel.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = null;

            // Kiểm tra tên kỹ năng để chuyển đúng trang
            switch (currentTopic) {
                case "Listening":
                    intent = new Intent(getContext(), BaiTapNgheActivity.class);
                    break;

                case "Speaking":
                    intent = new Intent(getContext(), BaiTapNoiActivity.class);
                    break;

                case "Writing":
                    intent = new Intent(getContext(), BaiTapVietActivity.class);
                    break;

                case "Reading":
                default:
                    intent = new Intent(getContext(), BaiTapDocActivity.class); // Mặc định
                    break;
            }

            if (intent != null) {
                // Gửi kèm dữ liệu cần thiết
                intent.putExtra("SELECTED_LEVEL", selectedLevel);
                intent.putExtra("SELECTED_TOPIC", currentTopic);

                startActivity(intent);
            }
        });
    }
}