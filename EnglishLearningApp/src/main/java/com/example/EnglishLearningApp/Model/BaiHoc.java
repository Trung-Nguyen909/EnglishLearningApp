package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BaiHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDKhoaHoc", nullable = false)
    private Integer idKhoaHoc;

    @Column(name = "tenBaiHoc", nullable = false, length = 100)
    private String tenBaiHoc;

    @Column(name = "moTa", length = 500)
    private String moTa;

    @Lob
    @Column(name = "noiDung")
    private String noiDung;

    @Column(name = "thuTuBaiHoc")
    private Integer thuTuBaiHoc;

    @Column(name = "trangThai", length = 50)
    private String trangThai;

    @Column(name = "IconUrl", length = 255)
    private String iconUrl;
}