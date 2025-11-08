package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.NguPhap;
import com.example.EnglishLearningApp.Repository.NguPhapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguPhapService {

    private final NguPhapRepository nguPhapRepository;

    public List<NguPhap> getAllNguPhap() {
        return nguPhapRepository.findAll();
    }

    public Optional<NguPhap> getNguPhapById(Integer id) {
        return nguPhapRepository.findById(id);
    }

    public NguPhap createNguPhap(NguPhap nguPhap) {
        return nguPhapRepository.save(nguPhap);
    }

    public NguPhap updateNguPhap(Integer id, NguPhap nguPhapDetails) {
        return nguPhapRepository.findById(id)
                .map(np -> {
                    np.setIdBaiHoc(nguPhapDetails.getIdBaiHoc());
                    np.setTenNguPhap(nguPhapDetails.getTenNguPhap());
                    np.setGiaiThich(nguPhapDetails.getGiaiThich());
                    np.setViDu(nguPhapDetails.getViDu());
                    return nguPhapRepository.save(np);
                })
                .orElseThrow(() -> new RuntimeException("NguPhap not found with id " + id));
    }

    public void deleteNguPhap(Integer id) {
        nguPhapRepository.deleteById(id);
    }
}
