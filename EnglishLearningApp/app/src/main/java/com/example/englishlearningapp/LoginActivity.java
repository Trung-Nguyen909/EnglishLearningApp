package com.example.englishlearningapp;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button btnDangKy, btnDangNhapTC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDangNhapTC = findViewById(R.id.btnDangNhapTC);
        btnDangNhapTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CourseFragment.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
