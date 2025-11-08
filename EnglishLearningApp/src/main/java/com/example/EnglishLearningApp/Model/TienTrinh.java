package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "TienTrinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TienTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "IDKhoaHoc", nullable = false)
    private Integer idKhoaHoc;

    @Column(name = "ngayLam", nullable = false)
    private LocalDate ngayLam;

    @Column(name = "DaLamDuocBaoNhieu", precision = 4, scale = 2)
    private BigDecimal daLamDuocBaoNhieu;

    @Column(name = "TgianLam")
    private LocalTime tgianLam;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "SoLanLam")
    private Integer soLanLam = 0;
}
