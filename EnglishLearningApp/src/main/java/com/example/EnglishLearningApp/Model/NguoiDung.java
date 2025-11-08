package com.example.EnglishLearningApp.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    String tenDangNhap;
    String matKhau;
    String email;
    String anhDaiDien;
    @Nullable
    LocalDate LastLogin;
    int Streak;
    String role;
}
