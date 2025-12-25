package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "TienTrinhBaiHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TienTrinhBaiHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IdTienTRinhKhoaHoc", nullable = false)
    private Integer idTienTrinhKhoaHoc;

    @Column(name = "IDBaiHoc", nullable = false)
    private Integer idBaiHoc;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "Ngaybatdau")
    private LocalDate ngayBatDau;

    @Column(name = "Ngayketthuc")
    private LocalDate ngayKetThuc;

    @Column(name = "solanlam")
    private Integer soLanLam = 0; // Mặc định là 0 nếu chưa làm

    @Column(name = "tgianlam")
    private LocalTime tgianLam;
}