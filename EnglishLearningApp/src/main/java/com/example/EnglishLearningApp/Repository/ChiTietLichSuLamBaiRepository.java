package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.ChiTietBaiLam;
import com.example.EnglishLearningApp.dto.response.ChiTietBaiLamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChiTietLichSuLamBaiRepository extends JpaRepository<ChiTietBaiLam, Integer> {
    @Query("""
    SELECT 
        c.id            AS cauHoiId,
        c.noiDungCauHoi AS noiDung,
        ct.userAns      AS dapAnNguoiDung,
        c.duLieuDapAn   AS dapAnDung,
        ct.isCorrect    AS dungSai,
        c.giaiThich     AS giaiThich
    FROM ChiTietBaiLam ct, CauHoi c
    WHERE ct.idCauHoi = c.id
      AND ct.idLichSuBaiLam = :lichSuId
""")
    List<ChiTietBaiLamDTO> findChiTietByLichSuId(
            @Param("lichSuId") Integer lichSuId
    );

}
