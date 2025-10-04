package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.KyNang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KyNangRepository extends JpaRepository<KyNang, Integer> {
}
