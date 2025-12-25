package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.KetQua;
import com.example.EnglishLearningApp.Repository.KetQuaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KetQuaService {

    private final KetQuaRepository ketQuaRepository;

    public List<KetQua> getAllKetQua() {
        return ketQuaRepository.findAll();
    }

    public Optional<KetQua> getKetQuaById(Integer id) {
        return ketQuaRepository.findById(id);
    }

    public KetQua createKetQua(KetQua ketQua) {
        return ketQuaRepository.save(ketQua);
    }

    public KetQua updateKetQua(Integer id, KetQua ketQua) {
        ketQua.setId(id);
        return ketQuaRepository.save(ketQua);
    }

    public void deleteKetQua(Integer id) {
        ketQuaRepository.deleteById(id);
    }
}
