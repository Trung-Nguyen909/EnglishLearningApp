package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearningapp.DTO.Request.UserRegisterRequest;
import com.example.englishlearningapp.DTO.Response.NguoiDungRespone;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {

    private Button btnDangNhap, btnCreateAccount;
    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        // Ánh xạ view
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.Password);
        edtConfirmPassword = findViewById(R.id.ConfirmPassword);

        // Nút quay về login
        btnDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút tạo tài khoản
        btnCreateAccount.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!password.equals(confirmPassword)){
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo request gửi server
            UserRegisterRequest request = new UserRegisterRequest(email, password, username);

            ApiService apiService = ApiClient.getClient(DangKyActivity.this).create(ApiService.class);
            apiService.register(request).enqueue(new Callback<NguoiDungRespone>() {
                @Override
                public void onResponse(Call<NguoiDungRespone> call, Response<NguoiDungRespone> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công: " + response.body().getEmail(), Toast.LENGTH_SHORT).show();

                        // Quay về Login
                        Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // --- PHẦN ĐÃ SỬA ---
                        try {
                            int statusCode = response.code();
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có phản hồi lỗi.";
                            Log.e("DangKyError", "Mã lỗi HTTP: " + statusCode);
                            Log.e("DangKyError", "Phản hồi lỗi từ Server: " + errorBody);
                            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại. Kiểm tra Logcat để biết chi tiết.", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại. Lỗi đọc phản hồi.", Toast.LENGTH_SHORT).show();
                        }
                        // ------------------
                    }
                }

                @Override
                public void onFailure(Call<NguoiDungRespone> call, Throwable t) {
                    Toast.makeText(DangKyActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
