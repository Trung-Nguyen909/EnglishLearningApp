package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findByEmail(String email);
}
