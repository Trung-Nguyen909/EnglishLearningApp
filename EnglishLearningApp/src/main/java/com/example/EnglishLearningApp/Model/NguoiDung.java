package com.example.EnglishLearningApp.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenDangNhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @Column(name = "matKhau", nullable = false, length = 100)
    private String matKhau;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @Builder.Default
    @Column(name = "Streak")
    private Integer streak = 0;

    @Builder.Default
    @Column(name = "role", length = 30)
    private String role = "USER";
}
