package com.example.englishlearningapp.Activity;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.DTO.Request.UserLoginRequest;
import com.example.englishlearningapp.DTO.Response.AuthResponse;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiResponse;
import com.example.englishlearningapp.Retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import retrofit2.Call;

public class DangNhapActivity extends AppCompatActivity {
    Button btnDangKy, btnDangNhapTC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDangNhapTC = findViewById(R.id.btnDangNhapTC);
        btnDangNhapTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) findViewById(R.id.edtEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.edtPassword)).getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this,
                            "Nhập đầy đủ email và mật khẩu",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiService apiService =
                        ApiClient.getClient(DangNhapActivity.this)
                                .create(ApiService.class);

                UserLoginRequest loginRequest =
                        new UserLoginRequest(email, password);

                apiService.login(loginRequest).enqueue(new retrofit2.Callback<AuthResponse>() {

                    @Override
                    public void onResponse(Call<AuthResponse> call,
                                           retrofit2.Response<AuthResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            AuthResponse auth = response.body();

                            // 🔹 Convert USER sang JSON
                            Gson gson = new Gson();
                            String userJson = gson.toJson(auth.getUser());

                            // 🔹 Lưu TOKEN + USER
                            getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                                    .edit()
                                    .putString("TOKEN", auth.getToken())
                                    .putString("USER", userJson)
                                    .apply();

                            createTodayLog();

                            Toast.makeText(DangNhapActivity.this,
                                    "Xin chào " + auth.getUser().getTenDangNhap(),
                                    Toast.LENGTH_SHORT).show();

                            Intent intent =
                                    new Intent(DangNhapActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(DangNhapActivity.this,
                                    "Email hoặc mật khẩu không đúng",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(DangNhapActivity.this,
                                "Lỗi mạng: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void createTodayLog() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.createTodayActivityLog().enqueue(new retrofit2.Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, retrofit2.Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiRes = response.body();

                    String message = apiRes.getMessage();

                    android.util.Log.d("API", "Message từ BE: " + message);
                    Snackbar.make(findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG
                    ).show();
                }else {
                    Snackbar.make(findViewById(android.R.id.content),
                            "Không nhận được dữ liệu từ server",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                android.util.Log.e("API_ERROR", "Lỗi khi tạo nhật ký: " + t.getMessage());
            }
        });
    }

}

