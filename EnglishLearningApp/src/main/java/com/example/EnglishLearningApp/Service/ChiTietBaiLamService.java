package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Repository.ChiTietLichSuLamBaiRepository;
import com.example.EnglishLearningApp.dto.response.ChiTietBaiLamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietBaiLamService {
    private final ChiTietLichSuLamBaiRepository chiTietRepo;

    public List<ChiTietBaiLamDTO> getChiTietBaiLam(Integer lichSuId) {
        return chiTietRepo.findChiTietByLichSuId(lichSuId);
    }
}
