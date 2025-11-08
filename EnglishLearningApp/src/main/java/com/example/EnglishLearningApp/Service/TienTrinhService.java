package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.TienTrinh;
import com.example.EnglishLearningApp.Repository.TienTrinhRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TienTrinhService {
    private final TienTrinhRepository tienTrinhRepository ;

    public List<TienTrinh> getAllTienTrinh(){
        return tienTrinhRepository.findAll();
    }

    public Optional<TienTrinh> getTienTrinhById(Integer id){
        return tienTrinhRepository.findById(id);
    }

    public TienTrinh taoTienTrinh(TienTrinh tienTrinh){
        return tienTrinhRepository.save(tienTrinh);
    }

    public TienTrinh updateTienTrinh(Integer id, TienTrinh tienTrinhChiTiet){
        return tienTrinhRepository.findById(id).map(tienTrinh -> {
                    tienTrinh.setIdNguoiDung(tienTrinhChiTiet.getIdNguoiDung());
                    tienTrinh.setIdKhoaHoc(tienTrinhChiTiet.getIdKhoaHoc());
                    tienTrinh.setNgayLam(tienTrinhChiTiet.getNgayLam());
                    tienTrinh.setDaLamDuocBaoNhieu(tienTrinhChiTiet.getDaLamDuocBaoNhieu());
                    tienTrinh.setTgianLam(tienTrinhChiTiet.getTgianLam());
                    tienTrinh.setTrangThai(tienTrinhChiTiet.getTrangThai());
                    tienTrinh.setSoLanLam(tienTrinhChiTiet.getSoLanLam());
                    return tienTrinhRepository.save(tienTrinh);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Tiến Trình có ID = " + id));
    }

    public void deleteTienTrinh(Integer id) {
        tienTrinhRepository.deleteById(id);
    }
}
