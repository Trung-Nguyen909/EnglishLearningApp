package com.example.EnglishLearningApp.Model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguoiDung_BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int idNguoiDung;
    int idBinhLuan;
    @Column(name = "Type")
    String type;
    @Column(name = "NgayTao")
    LocalDate ngayTao;
}
