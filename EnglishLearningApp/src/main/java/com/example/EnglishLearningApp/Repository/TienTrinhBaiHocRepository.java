package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Model.TienTrinhKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TienTrinhBaiHocRepository extends JpaRepository<TienTrinhBaiHoc, Integer> {
    List<TienTrinhBaiHoc> findByIdTienTrinhKhoaHoc(Integer idTienTrinhKhoaHoc);
}