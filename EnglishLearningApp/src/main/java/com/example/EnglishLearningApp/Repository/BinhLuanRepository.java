package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Integer> {
}
