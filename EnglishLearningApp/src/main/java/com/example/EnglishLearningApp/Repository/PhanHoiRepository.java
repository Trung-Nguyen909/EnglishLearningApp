package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.PhanHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanHoiRepository extends JpaRepository<PhanHoi, Integer> {
}
