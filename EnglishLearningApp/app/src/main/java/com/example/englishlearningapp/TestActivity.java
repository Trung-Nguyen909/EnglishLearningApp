package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TestActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout btnBasic, btnMedium, btnAdvanced;
    private LinearLayout dropdownTopics;
    private CardView btnStartTest;

    // Bottom Navigation
    private LinearLayout btnHome, btnLessons, btnExplore, btnProfile;

    // Variables
    private String selectedLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        initViews();
        setupListeners();
    }

    private void initViews() {

        btnBack = findViewById(R.id.btn_back);

        btnBasic = findViewById(R.id.btn_basic);
        btnMedium = findViewById(R.id.btn_medium);
        btnAdvanced = findViewById(R.id.btn_advanced);

        dropdownTopics = findViewById(R.id.dropdown_topics);

        btnStartTest = findViewById(R.id.btn_start_test);

        btnHome = findViewById(R.id.btn_home);
        btnLessons = findViewById(R.id.btn_lessons);
        btnExplore = findViewById(R.id.btn_explore);
        btnProfile = findViewById(R.id.btn_profile);
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Basic - 10 câu";
                Toast.makeText(TestActivity.this, "Đã chọn: Basic", Toast.LENGTH_SHORT).show();
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Medium - 20 câu";
                Toast.makeText(TestActivity.this, "Đã chọn: Medium", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = "Advanced - 30 câu";
                Toast.makeText(TestActivity.this, "Đã chọn: Advanced", Toast.LENGTH_SHORT).show();
            }
        });

        dropdownTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Chọn chủ đề", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLevel.isEmpty()) {
                    Toast.makeText(TestActivity.this, "Vui lòng chọn mức độ!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TestActivity.this, "Bắt đầu Test: " + selectedLevel, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Home", Toast.LENGTH_SHORT).show();
            }
        });

        btnLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Lessons", Toast.LENGTH_SHORT).show();
            }
        });

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Explore", Toast.LENGTH_SHORT).show();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}