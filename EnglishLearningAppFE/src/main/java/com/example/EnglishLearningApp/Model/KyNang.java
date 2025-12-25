package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KyNang")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KyNang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TenKyNang", nullable = false, length = 100, unique = true)
    private String tenKyNang;
}
