package com.example.EnglishLearningApp.Repository;


import com.example.EnglishLearningApp.Model.BaiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiHocRepository extends JpaRepository<BaiHoc, Integer> {
    List<BaiHoc> findByIdKhoaHoc(Integer idKhoaHoc);

}
