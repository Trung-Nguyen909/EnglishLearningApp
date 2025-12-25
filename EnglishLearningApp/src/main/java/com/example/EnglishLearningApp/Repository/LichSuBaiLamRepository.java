package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.LichSuBaiLam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LichSuBaiLamRepository extends JpaRepository<LichSuBaiLam, Integer> {
    List<LichSuBaiLam> findByIdNguoiDung(Integer idNguoiDung);
}
