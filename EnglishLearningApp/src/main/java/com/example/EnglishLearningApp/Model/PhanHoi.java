package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PhanHoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "NoiDung", length = 1000)
    private String noiDung;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;
}
