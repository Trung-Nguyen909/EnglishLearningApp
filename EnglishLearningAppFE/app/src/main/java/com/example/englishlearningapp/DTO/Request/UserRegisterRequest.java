package com.example.englishlearningapp.DTO.Request;

public class UserRegisterRequest {
    private String tenDangNhap;
    private String matKhau;
    private String email;

    public UserRegisterRequest(String email, String password, String fullName) {
        this.email = email;
        this.matKhau = password;
        this.tenDangNhap = fullName;
    }

    // getter & setter nếu cần
}
