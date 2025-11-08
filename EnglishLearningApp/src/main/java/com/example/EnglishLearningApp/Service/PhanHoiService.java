package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.PhanHoi;
import com.example.EnglishLearningApp.Repository.PhanHoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhanHoiService {

    private final PhanHoiRepository phanHoiRepository;

    public List<PhanHoi> getAllPhanHoi() {
        return phanHoiRepository.findAll();
    }

    public Optional<PhanHoi> getPhanHoiById(Integer id) {
        return phanHoiRepository.findById(id);
    }

    public PhanHoi createPhanHoi(PhanHoi phanHoi) {
        return phanHoiRepository.save(phanHoi);
    }

    public PhanHoi updatePhanHoi(Integer id, PhanHoi phanHoiDetails) {
        return phanHoiRepository.findById(id)
                .map(p -> {
                    p.setIdNguoiDung(phanHoiDetails.getIdNguoiDung());
                    p.setTitle(phanHoiDetails.getTitle());
                    p.setNoiDung(phanHoiDetails.getNoiDung());
                    p.setNgayTao(phanHoiDetails.getNgayTao());
                    return phanHoiRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("PhanHoi not found with id " + id));
    }

    public void deletePhanHoi(Integer id) {
        phanHoiRepository.deleteById(id);
    }
}
