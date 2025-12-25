package com.example.EnglishLearningApp.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String tenDangNhap;
    String email;
    String anhDaiDien;
    LocalDateTime LastLogin;
    int Streak;
    int tongThoiGianHoatDong;
}
