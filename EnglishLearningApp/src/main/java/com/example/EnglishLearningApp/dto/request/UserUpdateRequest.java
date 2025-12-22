package com.example.EnglishLearningApp.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String tenDangNhap;
    private String email;
    private String role;

    // optional: nếu muốn đổi mật khẩu ngay trong update
    private String matKhau; // null => không đổi
}
