package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.NhatKyHoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhatKyHoatDongRepository extends JpaRepository<NhatKyHoatDong, Integer> {

    // Hàm mở rộng: Tìm tất cả nhật ký của 1 user cụ thể (Rất hay dùng để vẽ biểu đồ)
    List<NhatKyHoatDong> findByIdNguoiDung(Integer idNguoiDung);

    // Hàm mở rộng: Tìm nhật ký của user trong 1 ngày cụ thể (để check xem hôm nay học chưa)
    NhatKyHoatDong findByIdNguoiDungAndNgayHoatDong(Integer idNguoiDung, java.time.LocalDate ngayHoatDong);
}