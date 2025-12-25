package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.ThanhTich;
import com.example.EnglishLearningApp.Repository.ThanhTichRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThanhTichService {

    private final ThanhTichRepository thanhTichRepository;

    public List<ThanhTich> getAllThanhTich() {
        return thanhTichRepository.findAll();
    }

    public Optional<ThanhTich> getThanhTichById(Integer id) {
        return thanhTichRepository.findById(id);
    }

    public ThanhTich createThanhTich(ThanhTich thanhTich) {
        return thanhTichRepository.save(thanhTich);
    }

    public ThanhTich updateThanhTich(Integer id, ThanhTich thanhTichDetails) {
        return thanhTichRepository.findById(id)
                .map(t -> {
                    t.setTenThanhTich(thanhTichDetails.getTenThanhTich());
                    t.setMoTa(thanhTichDetails.getMoTa());
                    t.setBieuTuong(thanhTichDetails.getBieuTuong());
                    return thanhTichRepository.save(t);
                })
                .orElseThrow(() -> new RuntimeException("ThanhTich not found with id " + id));
    }

    public void deleteThanhTich(Integer id) {
        thanhTichRepository.deleteById(id);
    }
}
