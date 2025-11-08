package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ThanhTich")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhTich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenThanhTich", nullable = false, length = 100)
    private String tenThanhTich;

    @Column(name = "moTa", length = 500)
    private String moTa;

    @Column(name = "bieuTuong", length = 255)
    private String bieuTuong;
}
