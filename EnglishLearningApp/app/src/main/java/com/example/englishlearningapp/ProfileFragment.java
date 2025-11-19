package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {

    Button btnEdit;

    public ProfileFragment() {
        // Constructor rỗng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main_profile_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- ĐÃ XÓA: btn_explore vì nút này nằm trên thanh Bottom Navigation (đã xử lý ở MainActivity hoặc đã xóa khỏi XML) ---

        // Ánh xạ nút chỉnh sửa
        btnEdit = view.findViewById(R.id.btnEdit);

        // Xử lý sự kiện nút Chỉnh sửa -> Chuyển sang EditProfileFragment
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Lưu ý: Đảm bảo R.id.frame_container là đúng ID của khung chứa Fragment trong MainActivity
                transaction.replace(R.id.frame_container, editProfileFragment);

                // Thêm vào BackStack để ấn nút Back điện thoại sẽ quay lại trang Profile
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
    }
}