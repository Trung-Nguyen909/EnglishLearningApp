package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.CapDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapDoRepository extends JpaRepository<CapDo,Integer> {
}
