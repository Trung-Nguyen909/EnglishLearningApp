package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Mapper.BinhluanMapper;
import com.example.EnglishLearningApp.Model.BinhLuan;
import com.example.EnglishLearningApp.Repository.BinhLuanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BinhLuanService {

    private final BinhLuanRepository repository;
    private final BinhluanMapper mapper;

    public List<BinhLuan> getAll() {
        return repository.findAll();
    }

    public Optional<BinhLuan> getById(Integer id) {
        return repository.findById(id);
    }

    public BinhLuan create(BinhLuan binhLuan) {
        return repository.save(binhLuan);
    }

    public BinhLuan update(Integer id, BinhLuan details) {
        return repository.findById(id)
                .map(bl -> {
                    mapper.toBinhLuan(details);
                    return repository.save(bl);
                })
                .orElseThrow(() -> new RuntimeException("BinhLuan not found with id " + id));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
