package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Test_CauHoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDTest", nullable = false)
    private Integer idTest;

    @Column(name = "IDCauHoi", nullable = false)
    private Integer idCauHoi;
}