package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BaiTap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private Integer IDBaiHoc;

    private String loaiBaiTap;

    private String trangThai;
}
