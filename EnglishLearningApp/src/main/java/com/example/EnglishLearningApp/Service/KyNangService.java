package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.KyNang;
import com.example.EnglishLearningApp.Repository.KyNangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KyNangService {

    private final KyNangRepository kyNangRepository;

    public List<KyNang> getAllKyNang() {
        return kyNangRepository.findAll();
    }

    public Optional<KyNang> getKyNangById(Integer id) {
        return kyNangRepository.findById(id);
    }

    public KyNang createKyNang(KyNang kyNang) {
        return kyNangRepository.save(kyNang);
    }

    public KyNang updateKyNang(Integer id, KyNang kyNangDetails) {
        return kyNangRepository.findById(id)
                .map(k -> {
                    k.setTenKyNang(kyNangDetails.getTenKyNang());
                    return kyNangRepository.save(k);
                })
                .orElseThrow(() -> new RuntimeException("KyNang not found with id " + id));
    }

    public void deleteKyNang(Integer id) {
        kyNangRepository.deleteById(id);
    }
}
