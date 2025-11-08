package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.DanhGiaKyNang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhGiaKyNangRepository extends JpaRepository<DanhGiaKyNang, Integer> {
}
