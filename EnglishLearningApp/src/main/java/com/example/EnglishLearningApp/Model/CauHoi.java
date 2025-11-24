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

    @Column(name = "IDCapDo", nullable = false)
    private Integer idCapDo;

    @Column(name = "IDBaiTap")
    private Integer idBaiTap; // Có thể null nếu dùng cho Test riêng

    @Lob // Báo hiệu đây là đoạn văn bản dài
    @Column(name = "NoiDungCauHoi", nullable = false)
    private String noiDungCauHoi;

    @Column(name = "LoaiCauHoi", nullable = false, length = 50)
    private String loaiCauHoi; // 'ABCD', 'Listening', 'FillBlank'

    @Lob
    @Column(name = "DuLieuDapAn", nullable = false)
    private String duLieuDapAn; // Lưu JSON String

    @Lob
    @Column(name = "GiaiThich")
    private String giaiThich;

    @Column(name = "Audio_URL")
    private String audioUrl;
}
