package com.example.EnglishLearningApp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ThanhTich")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhTich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenThanhTich", nullable = false, length = 100)
    private String tenThanhTich;

    @Column(name = "moTa", length = 500)
    private String moTa;

    @Column(name = "bieuTuong", length = 255)
    private String bieuTuong;

    @OneToMany(mappedBy = "thanhTich")
    @JsonIgnore
    private Set<NguoiDungThanhTich> nguoiDungs = new HashSet<>();

}
