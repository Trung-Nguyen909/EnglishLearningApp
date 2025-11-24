package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast; // Thêm cái này để thông báo khi lưu

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class ChinhSuaHoSoFragment extends Fragment {

    // Khai báo biến (Chỉ giữ lại những nút CÓ trong XML hiện tại)
    Button btnCancel, btnSave;
    ImageButton btnBack;

    public ChinhSuaHoSoFragment() {
        // Constructor rỗng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chinhsua_hoso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main_edit_profile_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- ÁNH XẠ VIEW (Chỉ ánh xạ những gì còn tồn tại) ---
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave); // Nút Lưu mới thêm
        btnBack = view.findViewById(R.id.btn_back); // ID trong XML là btn_back

        // --- ĐÃ XÓA: btnexplore và btnprofile vì trong XML bạn đã xóa thanh menu dưới ---

        // --- XỬ LÝ SỰ KIỆN ---

        // 1. Nút Hủy (Cancel): Quay lại
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // 2. Nút Back (Mũi tên): Quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // 3. Nút Lưu (Save): Xử lý logic lưu (tạm thời hiển thị thông báo)
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sau này bạn viết code lưu dữ liệu vào Database ở đây
                Toast.makeText(getContext(), "Đã lưu thông tin!", Toast.LENGTH_SHORT).show();

                // Lưu xong thì quay lại màn hình Profile
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}