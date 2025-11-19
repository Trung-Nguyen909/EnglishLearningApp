package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExerciseProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp giao diện từ file XML cũ
        View view = inflater.inflate(R.layout.activity_exercise_progress, container, false);

        /*
         * LƯU Ý QUAN TRỌNG:
         * Nếu "btn_profile" là nút nằm ở thanh menu cũ dưới đáy -> BẠN NÊN XÓA ĐOẠN CODE NÀY.
         * Vì bây giờ thanh menu nằm ở MainActivity, Fragment này không cần xử lý nó nữa.
         * Nếu để lại, khi bạn xóa thanh menu trong XML, dòng findViewById bên dưới sẽ gây lỗi (Crash app).
         */

        // LinearLayout profileButton = view.findViewById(R.id.btn_profile);
        // if (profileButton != null) {
        //     profileButton.setOnClickListener(new View.OnClickListener() {
        //         @Override
        //         public void onClick(View v) {
        //             // MainActivity đã lo việc chuyển trang rồi, không cần code này nữa
        //         }
        //     });
        // }

        return view;
    }
}