package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.LichSuBaiLam;
import com.example.EnglishLearningApp.Repository.LichSuBaiLamRepository;
import com.example.EnglishLearningApp.dto.request.SubmitBaiLamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LichSuBaiLamService {
    private final LichSuBaiLamRepository lichSuBaiLamRepository;
    private final ChiTietBaiLamService chiTietBaiLamService;

    public List<LichSuBaiLam> findByIdNguoiDung(Integer idNguoiDung) {
        return lichSuBaiLamRepository.findByIdNguoiDung(idNguoiDung);
    }

    public LichSuBaiLam submitBaiLam(Integer idNguoiDung, SubmitBaiLamRequest submitRequest) {
        // Tính điểm dựa trên số câu trả lời đúng
        long correctAnswers = submitRequest.getCauTraLoi().stream()
                .filter(ct -> ct.getUserAns() != null &&
                             ct.getUserAns().equalsIgnoreCase(ct.getCorrectAns()))
                .count();

        BigDecimal diem = BigDecimal.valueOf((correctAnswers * 10) / (double) submitRequest.getCauTraLoi().size());

        // Tạo LichSuBaiLam record
        LichSuBaiLam lichSuBaiLam = LichSuBaiLam.builder()
                .idNguoiDung(idNguoiDung)
                .idTest(submitRequest.getIdTest())
                .idBaiTap(submitRequest.getIdBaiTap())
                .tenBai(submitRequest.getTenBai())
                .loaiBai(submitRequest.getLoaiBai())
                .diemSo(diem)
                .trangThai("Hoàn thành")
                .tgianLam(submitRequest.getTgianLam())
                .build();

        LichSuBaiLam savedLichSu = lichSuBaiLamRepository.save(lichSuBaiLam);

        // Lưu các ChiTietBaiLam records
        chiTietBaiLamService.saveChiTietBaiLam(savedLichSu.getId(), submitRequest.getCauTraLoi());

        return savedLichSu;
    }
}
