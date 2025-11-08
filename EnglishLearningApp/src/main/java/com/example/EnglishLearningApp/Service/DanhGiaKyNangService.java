package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.DanhGiaKyNang;
import com.example.EnglishLearningApp.Repository.DanhGiaKyNangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhGiaKyNangService {

    private final DanhGiaKyNangRepository danhGiaKyNangRepository;

    public List<DanhGiaKyNang> getAllDanhGiaKyNang() {
        return danhGiaKyNangRepository.findAll();
    }

    public Optional<DanhGiaKyNang> getDanhGiaKyNangById(Integer id) {
        return danhGiaKyNangRepository.findById(id);
    }

    public DanhGiaKyNang createDanhGiaKyNang(DanhGiaKyNang danhGiaKyNang) {
        return danhGiaKyNangRepository.save(danhGiaKyNang);
    }

    public DanhGiaKyNang updateDanhGiaKyNang(Integer id, DanhGiaKyNang danhGiaKyNangDetails) {
        return danhGiaKyNangRepository.findById(id)
                .map(d -> {
                    d.setIdKyNang(danhGiaKyNangDetails.getIdKyNang());
                    d.setIdNguoiDung(danhGiaKyNangDetails.getIdNguoiDung());
                    d.setDiem(danhGiaKyNangDetails.getDiem());
                    d.setNgayCapNhat(danhGiaKyNangDetails.getNgayCapNhat());
                    return danhGiaKyNangRepository.save(d);
                })
                .orElseThrow(() -> new RuntimeException("DanhGiaKyNang not found with id " + id));
    }

    public void deleteDanhGiaKyNang(Integer id) {
        danhGiaKyNangRepository.deleteById(id);
    }
}
