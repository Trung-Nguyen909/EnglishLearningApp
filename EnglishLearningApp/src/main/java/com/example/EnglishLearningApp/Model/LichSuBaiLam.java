package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuBaiLam")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuBaiLam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "IDTest")
    private Integer idTest;   // Null nếu làm bài tập

    @Column(name = "IDBaiTap")
    private Integer idBaiTap; // Null nếu làm bài test

    @Column(name = "Tenbai", length = 100)
    private String tenBai;

    @Column(name = "LoaiBai", length = 20)
    private String loaiBai; // 'TEST' hoặc 'BAITAP'

    @Column(name = "DiemSo")
    private BigDecimal diemSo;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @Builder.Default
    @Column(name = "TgianNopBai")
    private LocalDateTime tgianNopBai = LocalDateTime.now();

    @Column(name = "TgianLam")
    private Integer tgianLam;
}