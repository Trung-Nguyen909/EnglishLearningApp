package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TuVung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuVung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDBaiHoc", nullable = false)
    private Integer idBaiHoc;

    @Column(name = "tuTiengAnh", nullable = false, length = 100)
    private String tuTiengAnh;

    @Column(name = "nghiaTiengViet", nullable = false, length = 100)
    private String nghiaTiengViet;

    @Column(name = "phienAm", length = 100)
    private String phienAm;

    @Column(name = "viDu", length = 500)
    private String viDu;

    @Column(name = "amThanhPhienAm", length = 255)
    private String amThanhPhienAm;
}
