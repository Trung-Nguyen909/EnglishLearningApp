package com.example.EnglishLearningApp.dto.response;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int ID;
    String tenDangNhap;
    String email;
    String anhDaiDien;
    LocalDate LastLogin;
    int Streak;
}
