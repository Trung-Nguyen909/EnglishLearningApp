package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiTapRepository extends JpaRepository<BaiTap,Integer> {
    void deleteByIdBaiHoc(Integer idBaiHoc);

    List<BaiTap> findByIdBaiHoc(Integer idBaiHoc);
}
