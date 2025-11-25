package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.TienTrinhKhoaHoc;
import com.example.EnglishLearningApp.Repository.TienTrinhKhoaHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TienTrinhKhoaHocService {
    private final TienTrinhKhoaHocRepository repository;

    public List<TienTrinhKhoaHoc> getAll() {
        return repository.findAll();
    }

    public Optional<TienTrinhKhoaHoc> getById(Integer id) {
        return repository.findById(id);
    }

    public TienTrinhKhoaHoc create(TienTrinhKhoaHoc tienTrinh) {
        return repository.save(tienTrinh);
    }

    public TienTrinhKhoaHoc update(Integer id, TienTrinhKhoaHoc newData) {
        return repository.findById(id).map(existing -> {
            existing.setIdUser(newData.getIdUser());
            existing.setIdKhoaHoc(newData.getIdKhoaHoc());
            existing.setTrangThai(newData.getTrangThai());
            existing.setPhanTram(newData.getPhanTram());
            existing.setLanLamGanNhat(newData.getLanLamGanNhat());
            existing.setTgianHoc(newData.getTgianHoc());
            existing.setNgayBatDau(newData.getNgayBatDau());
            existing.setNgayHoanThanh(newData.getNgayHoanThanh());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy Tiến Trình Khóa Học ID = " + id));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}