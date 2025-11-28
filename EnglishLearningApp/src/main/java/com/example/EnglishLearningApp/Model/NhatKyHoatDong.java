package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "NhatKyHoatDong")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyHoatDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "NgayHoatDong", nullable = false)
    private LocalDate ngayHoatDong;

    @Column(name = "SoPhutHoc")
    private Integer soPhutHoc = 0;

    @Column(name = "TongSoBaiDaLam")
    private Integer tongSoBaiDaLam = 0;
}
