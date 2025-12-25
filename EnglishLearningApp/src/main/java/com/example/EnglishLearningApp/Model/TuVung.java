package com.example.EnglishLearningApp.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TuVung")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuVung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Key chính
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

    @ManyToMany(mappedBy = "tuVungs")
    @JsonBackReference
    private Set<NguoiDung> nguoiDungs = new HashSet<>();
}