package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NguPhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguPhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "IDBaiHoc", nullable = false)
    private Integer idBaiHoc;

    @Column(name = "tenNguPhap", nullable = false, length = 100)
    private String tenNguPhap;

    @Column(name = "giaiThich", length = 1000)
    private String giaiThich;

    @Column(name = "viDu", length = 500)
    private String viDu;
}
