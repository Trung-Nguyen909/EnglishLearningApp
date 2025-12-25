package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.LichSuBaiLam;
import com.example.EnglishLearningApp.Repository.LichSuBaiLamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LichSuBaiLamService {
    private final LichSuBaiLamRepository lichSuBaiLamRepository;

    public List<LichSuBaiLam> findByIdNguoiDung(Integer idNguoiDung) {
        return lichSuBaiLamRepository.findByIdNguoiDung(idNguoiDung);
    }
}
