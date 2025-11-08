package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "BinhLuan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDKhoaHoc", nullable = false)
    private Integer idKhoaHoc;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "NgayLam", nullable = false)
    private LocalDate ngayLam;

    @Column(name = "NoiDung", length = 1000)
    private String noiDung;

    @Column(name = "LikeCount")
    private Integer likeCount = 0;

    @Column(name = "DislikeCount")
    private Integer dislikeCount = 0;
}
