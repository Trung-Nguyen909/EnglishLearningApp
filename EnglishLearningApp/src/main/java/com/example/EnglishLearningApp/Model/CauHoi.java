package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CauHoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String cauHoi;

    @Column(length = 200)
    private String a;

    @Column(length = 200)
    private String b;

    @Column(length = 200)
    private String c;

    @Column(length = 200)
    private String d;

    @Column(length = 5)
    private String cauTraLoi;
}
