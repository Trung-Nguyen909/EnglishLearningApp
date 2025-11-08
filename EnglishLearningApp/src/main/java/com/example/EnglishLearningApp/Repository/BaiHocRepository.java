package com.example.EnglishLearningApp.Repository;


import com.example.EnglishLearningApp.Model.BaiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiHocRepository extends JpaRepository<BaiHoc, Integer> {
}
