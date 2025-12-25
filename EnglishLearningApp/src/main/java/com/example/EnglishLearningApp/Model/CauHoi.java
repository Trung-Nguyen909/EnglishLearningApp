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
    @Column(name = "ID")
    private Integer id;

    @Column(name = "IDCapDo", nullable = false)
    private Integer idCapDo;

    // Quan trọng: Ánh xạ với cột IDBaiTap trong SQL
    @Column(name = "IDBaiTap")
    private Integer idBaiTap;

    @Lob
    @Column(name = "NoiDungCauHoi", nullable = false)
    private String noiDungCauHoi;

    @Column(name = "LoaiCauHoi", nullable = false, length = 50)
    private String loaiCauHoi; // 'ABCD', 'Listening', 'FillBlank', 'Speaking'

    @Lob
    @Column(name = "DuLieuDapAn", nullable = false)
    private String duLieuDapAn; // JSON String: {"A":"...", "Correct":"..."}

    @Lob
    @Column(name = "GiaiThich")
    private String giaiThich;

    @Column(name = "Audio_URL")
    private String audioUrl;
}