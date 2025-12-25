package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Model.TienTrinhKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TienTrinhKhoaHocRepository extends JpaRepository<TienTrinhKhoaHoc, Integer> {
    List<TienTrinhKhoaHoc> findByIdUser(Integer idUser);
}