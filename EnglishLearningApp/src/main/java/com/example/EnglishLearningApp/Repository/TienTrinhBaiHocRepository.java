package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Model.TienTrinhKhoaHoc;
import com.example.EnglishLearningApp.dto.response.LatestLearningDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TienTrinhBaiHocRepository extends JpaRepository<TienTrinhBaiHoc, Integer> {
    List<TienTrinhBaiHoc> findByIdTienTrinhKhoaHoc(Integer idTienTrinhKhoaHoc);

    @Query("SELECT new com.example.EnglishLearningApp.dto.response.LatestLearningDto(" +
            "kh.tenKhoaHoc, bh.tenBaiHoc, bh.id, ttkh.phanTram) " +
            "FROM TienTrinhBaiHoc ttbh " +
            "JOIN ttbh.tienTrinhKhoaHoc ttkh " +
            "JOIN ttbh.baiHoc bh " +
            "JOIN ttkh.khoaHoc kh " +
            "WHERE ttkh.idUser = :userId " +
            "ORDER BY ttbh.ngaybatdau DESC LIMIT 1")
    static LatestLearningDto findLatestLessonByUserId(Integer userId) {
        return null;
    }
}