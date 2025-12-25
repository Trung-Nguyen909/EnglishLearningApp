package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {
}
