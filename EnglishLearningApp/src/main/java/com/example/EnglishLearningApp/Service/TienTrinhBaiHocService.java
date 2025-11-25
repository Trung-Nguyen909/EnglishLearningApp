package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Repository.TienTrinhBaiHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TienTrinhBaiHocService {
    private final TienTrinhBaiHocRepository repository;

    public List<TienTrinhBaiHoc> getAll() {
        return repository.findAll();
    }

    public Optional<TienTrinhBaiHoc> getById(Integer id) {
        return repository.findById(id);
    }

    public TienTrinhBaiHoc create(TienTrinhBaiHoc tienTrinh) {
        return repository.save(tienTrinh);
    }

    public TienTrinhBaiHoc update(Integer id, TienTrinhBaiHoc newData) {
        return repository.findById(id).map(existing -> {
            existing.setIdTienTrinhKhoaHoc(newData.getIdTienTrinhKhoaHoc());
            existing.setIdBaiHoc(newData.getIdBaiHoc());
            existing.setStatus(newData.getStatus());
            existing.setNgayBatDau(newData.getNgayBatDau());
            existing.setNgayKetThuc(newData.getNgayKetThuc());
            existing.setSoLanLam(newData.getSoLanLam());
            existing.setTgianLam(newData.getTgianLam());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy Tiến Trình Bài Học ID = " + id));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}