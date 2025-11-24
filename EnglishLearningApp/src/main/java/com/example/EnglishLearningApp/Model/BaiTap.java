package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BaiTap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDBaiHoc", nullable = false)
    private Integer idBaiHoc;

    @Column(name = "loaiBaiTap", length = 50)
    private String loaiBaiTap;

    @Column(name = "trangThai", length = 50)
    private String trangThai;
}