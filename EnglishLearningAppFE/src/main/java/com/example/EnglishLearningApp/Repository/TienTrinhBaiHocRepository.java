package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.dto.response.LatestLearningDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TienTrinhBaiHocRepository
        extends JpaRepository<TienTrinhBaiHoc, Integer> {

    @Query(value = """
        SELECT TOP 1
            kh.tenKhoaHoc      AS tenKhoaHoc,
            k.PhanTram         AS phanTram,
            bh.tenBaiHoc       AS tenBaiHoc,
            b.NgayBatDau       AS ngayBatDau,
            bh.ID              AS baiHocId
        FROM TienTrinhKhoaHoc k
        JOIN TienTrinhBaiHoc b ON k.ID = b.IdTienTrinhKhoaHoc
        JOIN KhoaHoc kh ON kh.ID = k.IdKhoaHoc
        JOIN BaiHoc bh ON bh.ID = b.IDBaiHoc
        WHERE b.status = N'In Progress'
          AND k.IdUser = :userId
        ORDER BY b.NgayBatDau DESC
    """, nativeQuery = true)
    LatestLearningDto findLatestLessonByUserId(Integer userId);
}
