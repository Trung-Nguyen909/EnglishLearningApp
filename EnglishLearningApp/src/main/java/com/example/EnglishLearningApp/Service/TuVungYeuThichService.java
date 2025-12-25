package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.TuVungYeuThich;
import com.example.EnglishLearningApp.Repository.TuVungYeuThichRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TuVungYeuThichService {

    private final TuVungYeuThichRepository repository;

    public List<TuVungYeuThich> getAll() {
        return repository.findAll();
    }

    public Optional<TuVungYeuThich> getById(Integer id) {
        return repository.findById(id);
    }

    public TuVungYeuThich create(TuVungYeuThich tuVungYeuThich) {
        return repository.save(tuVungYeuThich);
    }

    public TuVungYeuThich update(Integer id, TuVungYeuThich details) {
        return repository.findById(id)
                .map(tvyt -> {
                    tvyt.setIdNguoiDung(details.getIdNguoiDung());
                    tvyt.setIdTuVung(details.getIdTuVung());
                    return repository.save(tvyt);
                })
                .orElseThrow(() -> new RuntimeException("TuVungYeuThich not found with id " + id));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
