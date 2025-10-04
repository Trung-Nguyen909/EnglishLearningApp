package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.KetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaRepository extends JpaRepository<KetQua, Integer> {
}
