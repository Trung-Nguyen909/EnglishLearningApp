package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.TuVung;
import com.example.EnglishLearningApp.Repository.TuVungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TuVungService {

    private final TuVungRepository tuVungRepository;

    public List<TuVung> getAllTuVung() {
        return tuVungRepository.findAll();
    }

    public Optional<TuVung> getTuVungById(Integer id) {
        return tuVungRepository.findById(id);
    }

    public TuVung createTuVung(TuVung tuVung) {
        return tuVungRepository.save(tuVung);
    }

    public TuVung updateTuVung(Integer id, TuVung tuVungDetails) {
        return tuVungRepository.findById(id)
                .map(t -> {
                    t.setIdBaiHoc(tuVungDetails.getIdBaiHoc());
                    t.setTuTiengAnh(tuVungDetails.getTuTiengAnh());
                    t.setNghiaTiengViet(tuVungDetails.getNghiaTiengViet());
                    t.setPhienAm(tuVungDetails.getPhienAm());
                    t.setViDu(tuVungDetails.getViDu());
                    t.setAmThanhPhienAm(tuVungDetails.getAmThanhPhienAm());
                    return tuVungRepository.save(t);
                })
                .orElseThrow(() -> new RuntimeException("TuVung not found with id " + id));
    }

    public void deleteTuVung(Integer id) {
        tuVungRepository.deleteById(id);
    }
}
