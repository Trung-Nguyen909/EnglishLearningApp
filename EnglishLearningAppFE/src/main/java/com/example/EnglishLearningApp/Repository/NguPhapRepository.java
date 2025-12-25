package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.NguPhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguPhapRepository extends JpaRepository<NguPhap, Integer> {
    // Trong NguPhapRepository.java
    List<NguPhap> findByIdBaiHoc(Integer idBaiHoc);
}
