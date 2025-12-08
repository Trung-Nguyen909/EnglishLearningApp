package com.example.englishlearningapp;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishlearningapp.DTO.Request.UserLoginRequest;
import com.example.englishlearningapp.DTO.Response.AuthResponse;
import com.example.englishlearningapp.Retrofit.ApiService;

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
                String email = ((EditText)findViewById(R.id.edtEmail)).getText().toString();
                String password = ((EditText)findViewById(R.id.edtPassword)).getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(DangNhapActivity.this, "Nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                UserLoginRequest loginRequest = new UserLoginRequest(email, password);

                apiService.login(loginRequest).enqueue(new retrofit2.Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {
                        if(response.isSuccessful() && response.body() != null){
                            AuthResponse auth = response.body();
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công: " + auth.getUser(), Toast.LENGTH_SHORT).show();
                            getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                                    .edit()
                                    .putString("TOKEN", auth.getToken())
                                    .apply();

                            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DangNhapActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(DangNhapActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
