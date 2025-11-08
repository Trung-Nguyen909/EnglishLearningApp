package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.NguPhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguPhapRepository extends JpaRepository<NguPhap, Integer> {
}
