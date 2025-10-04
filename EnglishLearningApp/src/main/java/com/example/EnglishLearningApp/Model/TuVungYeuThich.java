package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TuVungYeuThich")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuVungYeuThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDNguoiDung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "IDTuVung", nullable = false)
    private Integer idTuVung;
}
