package com.example.englishlearningapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

public class ChinhSuaHoSoFragment extends Fragment {
    Button btnHuy, btnLuu;
    ImageButton nutQuayLai, nutDoiAnh;
    EditText etHoTen, etEmail, etGioiThieu;
    Spinner spCapDo;
    ImageView imgAnhDaiDien;

    public ChinhSuaHoSoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chinhsua_hoso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.layout_chinh_sua_ho_so), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnHuy = view.findViewById(R.id.btn_huy);
        btnLuu = view.findViewById(R.id.btn_luu);
        nutQuayLai = view.findViewById(R.id.nut_quay_lai);
        nutDoiAnh = view.findViewById(R.id.nut_doi_anh);

        etHoTen = view.findViewById(R.id.et_ho_ten);
        etEmail = view.findViewById(R.id.et_email);
        etGioiThieu = view.findViewById(R.id.et_gioi_thieu);

        spCapDo = view.findViewById(R.id.sp_cap_do);
        imgAnhDaiDien = view.findViewById(R.id.img_anh_dai_dien);

        // --- XỬ LÝ SỰ KIỆN ---
        // 1. Nút Hủy (Quay lại)
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // 2. Nút Back
        nutQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // 3. Nút Lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic lưu dữ liệu
                String hoTen = etHoTen.getText().toString();
                // ... Xử lý lưu ...

                Toast.makeText(getContext(), "Đã lưu thông tin!", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình Profile
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // 4. Sự kiện nút đổi ảnh
        nutDoiAnh.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng đổi ảnh đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }
}