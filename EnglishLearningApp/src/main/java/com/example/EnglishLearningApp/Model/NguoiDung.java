package com.example.EnglishLearningApp.Model;

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
    String hoTen;
    String email;
    String anhDaiDien;
    LocalDate LastLogin;
    int Streak;
}
