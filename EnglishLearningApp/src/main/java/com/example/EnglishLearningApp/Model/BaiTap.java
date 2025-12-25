package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "BaiTap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDBaiHoc", nullable = false)
    private Integer idBaiHoc;

    @Column(name = "TenBaiTap", columnDefinition = "nvarchar(max)")
    private String tenBaiTap;

    @Column(name = "loaiBaiTap", length = 50)
    private String loaiBaiTap;

    @Column(name = "trangThai", length = 50)
    private String trangThai;

    @Column(name = "capdo", length = 100)
    private String capdo;

    @Column(name = "thoigian")
    private LocalTime thoigian;
}