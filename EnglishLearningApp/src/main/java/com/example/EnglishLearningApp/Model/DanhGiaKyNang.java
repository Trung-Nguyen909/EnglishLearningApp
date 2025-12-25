package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGiaKyNang")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGiaKyNang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDKyNang", nullable = false)
    private Integer idKyNang;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    private BigDecimal diem;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;
}
