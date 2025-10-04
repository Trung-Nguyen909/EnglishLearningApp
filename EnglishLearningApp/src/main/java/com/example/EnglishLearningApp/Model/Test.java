package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDKhoaHoc", nullable = false)
    private Integer idKhoaHoc;

    @Column(name = "TenBaiTest", nullable = false, length = 100)
    private String tenBaiTest;

    @Column(name = "SoCauHoi", nullable = false)
    private Integer soCauHoi;

    @Column(name = "TgianLam")
    private java.time.LocalTime tgianLam;
}
