package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    // Khai báo các nút bấm (Layout cha)
    private LinearLayout btnHome, btnLessons, btnExplore, btnProfile;

    // Khai báo các Icon để đổi màu
    private ImageView iconHome, iconLessons, iconExplore, iconProfile;

    // Khai báo các dấu chấm (dot) để hiện/ẩn
    private View dotHome, dotLessons, dotExplore, dotProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ View
        initViews();

        // 2. Mặc định chọn tab Lessons (Khóa học) khi mở app (hoặc tab nào bạn muốn)
        loadFragment(new CourseFragment());
        updateBottomNavUI(btnLessons, iconLessons, dotLessons);

        // 3. Bắt sự kiện click
        btnHome.setOnClickListener(v -> {
            //loadFragment(new HomeFragment()); // Nhớ tạo HomeFragment
            updateBottomNavUI(btnHome, iconHome, dotHome);
        });

        btnLessons.setOnClickListener(v -> {
            loadFragment(new CourseFragment());
            updateBottomNavUI(btnLessons, iconLessons, dotLessons);
        });

        btnExplore.setOnClickListener(v -> {
            // loadFragment(new ExploreFragment()); // Bỏ comment khi tạo Fragment này
            updateBottomNavUI(btnExplore, iconExplore, dotExplore);
        });

        btnProfile.setOnClickListener(v -> {
            //loadFragment(new ProfileFragment());
            updateBottomNavUI(btnProfile, iconProfile, dotProfile);
        });
    }

    private void initViews() {
        // Containers
        btnHome = findViewById(R.id.btn_home);
        btnLessons = findViewById(R.id.btn_lessons);
        btnExplore = findViewById(R.id.btn_explore);
        btnProfile = findViewById(R.id.btn_profile);

        // Icons
        iconHome = findViewById(R.id.icon_home);
        iconLessons = findViewById(R.id.icon_lessons);
        iconExplore = findViewById(R.id.icon_explore);
        iconProfile = findViewById(R.id.icon_profile);

        // Dots
        dotHome = findViewById(R.id.dot_home);
        dotLessons = findViewById(R.id.dot_lessons);
        dotExplore = findViewById(R.id.dot_explore);
        dotProfile = findViewById(R.id.dot_profile);
    }

    // Hàm nạp Fragment
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

    // Hàm cập nhật giao diện Bottom Nav (Đổi màu + Hiện dấu chấm)
    private void updateBottomNavUI(LinearLayout selectedBtn, ImageView selectedIcon, View selectedDot) {
        // Màu sắc
        int colorActive = ContextCompat.getColor(this, R.color.royalBlue); // Màu xanh chủ đạo (hoặc #4169E1)
        int colorInactive = ContextCompat.getColor(this, android.R.color.darker_gray); // Màu xám

        // 1. Reset tất cả về trạng thái chưa chọn (Màu xám, ẩn dấu chấm)
        iconHome.setColorFilter(colorInactive);
        iconLessons.setColorFilter(colorInactive);
        iconExplore.setColorFilter(colorInactive);
        iconProfile.setColorFilter(colorInactive);

        dotHome.setVisibility(View.INVISIBLE);
        dotLessons.setVisibility(View.INVISIBLE);
        dotExplore.setVisibility(View.INVISIBLE);
        dotProfile.setVisibility(View.INVISIBLE);

        // 2. Set trạng thái cho tab ĐƯỢC CHỌN (Màu xanh, hiện dấu chấm)
        selectedIcon.setColorFilter(colorActive);
        selectedDot.setVisibility(View.VISIBLE);
    }
}