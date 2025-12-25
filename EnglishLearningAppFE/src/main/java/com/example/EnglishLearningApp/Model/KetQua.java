package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "KetQua")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDTest", nullable = false)
    private Integer idTest;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "Diem")
    private BigDecimal diem;

    @Column(name = "TgianLam")
    private LocalTime tgianLam;

    @Column(name = "NgayLam")
    private LocalDate ngayLam;
}
