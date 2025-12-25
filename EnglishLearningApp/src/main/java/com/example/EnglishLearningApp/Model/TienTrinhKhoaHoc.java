package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "TienTrinhKhoaHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TienTrinhKhoaHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IdKhoaHoc", nullable = false)
    private Integer idKhoaHoc;

    @Column(name = "IdUser", nullable = false)
    private Integer idUser;

    @Column(name = "trangthai", length = 50)
    private String trangThai;

    // Sử dụng BigDecimal cho Phần trăm để đảm bảo độ chính xác (ví dụ: 50.5%)
    @Column(name = "PhanTram", precision = 5, scale = 2)
    private BigDecimal phanTram;

    // Lần làm gần nhất thường cần cả ngày và giờ
    @Column(name = "LanLamGanNhat")
    private LocalDateTime lanLamGanNhat;

    @Column(name = "Tgianhoc")
    private LocalTime tgianHoc;

    @Column(name = "NgayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "NgayHoanThanh")
    private LocalDate ngayHoanThanh;
}