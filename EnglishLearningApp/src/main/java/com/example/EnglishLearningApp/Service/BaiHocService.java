package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.BaiHoc;
import com.example.EnglishLearningApp.Repository.BaiHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaiHocService {

    private final BaiHocRepository baiHocRepository;

    public List<BaiHoc> getAllBaiHoc() {
        return baiHocRepository.findAll();
    }

    public Optional<BaiHoc> getBaiHocById(Integer id) {
        return baiHocRepository.findById(id);
    }

    public BaiHoc createBaiHoc(BaiHoc baiHoc) {
        return baiHocRepository.save(baiHoc);
    }

    public BaiHoc updateBaiHoc(Integer id, BaiHoc baiHocDetails) {
        return baiHocRepository.findById(id)
                .map(b -> {
                    b.setIdKhoaHoc(baiHocDetails.getIdKhoaHoc());
                    b.setTenBaiHoc(baiHocDetails.getTenBaiHoc());
                    b.setMoTa(baiHocDetails.getMoTa());
                    b.setNoiDung(baiHocDetails.getNoiDung());
                    b.setThuTuBaiHoc(baiHocDetails.getThuTuBaiHoc());
                    b.setTrangThai(baiHocDetails.getTrangThai());
                    return baiHocRepository.save(b);
                })
                .orElseThrow(() -> new RuntimeException("BaiHoc not found with id " + id));
    }

    public void deleteBaiHoc(Integer id) {
        baiHocRepository.deleteById(id);
    }
}
