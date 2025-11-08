package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Integer> {
}
