package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "KhoaHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenKhoaHoc", nullable = false, length = 100)
    private String tenKhoaHoc;

    @Column(name = "moTa", length = 500)
    private String moTa;

    @Column(name = "trinhDo", length = 50)
    private String trinhDo;

    @Column(name = "ngayTao", nullable = false)
    private LocalDate ngayTao;

    @Column(name = "IconUrl", length = 255)
    private String iconUrl;
}