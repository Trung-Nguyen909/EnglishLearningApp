package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietBaiLam")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietBaiLam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDLichSuBaiLam", nullable = false)
    private Integer idLichSuBaiLam;

    @Column(name = "IDCauHoi", nullable = false)
    private Integer idCauHoi;

    @Lob
    @Column(name = "UserAns")
    private String userAns; // Câu trả lời của user

    @Column(name = "IsCorrect")
    private Boolean isCorrect; // True/False
}