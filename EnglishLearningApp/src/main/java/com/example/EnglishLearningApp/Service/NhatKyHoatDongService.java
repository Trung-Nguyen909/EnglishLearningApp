package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.NhatKyHoatDong;
import com.example.EnglishLearningApp.Repository.NhatKyHoatDongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhatKyHoatDongService {

    private final NhatKyHoatDongRepository nhatKyHoatDongRepository;

    public List<NhatKyHoatDong> getAllNhatKy() {
        return nhatKyHoatDongRepository.findAll();
    }

    public Optional<NhatKyHoatDong> getNhatKyById(Integer id) {
        return nhatKyHoatDongRepository.findById(id);
    }

    // Hàm lấy danh sách theo User ID (hỗ trợ vẽ biểu đồ)
    public List<NhatKyHoatDong> getNhatKyByUserId(Integer userId) {
        return nhatKyHoatDongRepository.findByNguoiDung_Id(userId);
    }

    public NhatKyHoatDong createNhatKy(NhatKyHoatDong nhatKy) {
        return nhatKyHoatDongRepository.save(nhatKy);
    }

    public NhatKyHoatDong updateNhatKy(Integer id, NhatKyHoatDong nhatKyDetails) {
        return nhatKyHoatDongRepository.findById(id)
                .map(nk -> {
                    nk.setIdNguoiDung(nhatKyDetails.getIdNguoiDung());
                    nk.setNgayHoatDong(nhatKyDetails.getNgayHoatDong());
                    nk.setSoPhutHoc(nhatKyDetails.getSoPhutHoc());
                    nk.setTongSoBaiDaLam(nhatKyDetails.getTongSoBaiDaLam());
                    return nhatKyHoatDongRepository.save(nk);
                })
                .orElseThrow(() -> new RuntimeException("NhatKyHoatDong not found with id " + id));
    }

    public void deleteNhatKy(Integer id) {
        nhatKyHoatDongRepository.deleteById(id);
    }
}