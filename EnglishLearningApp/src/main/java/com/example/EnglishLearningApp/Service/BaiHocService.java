package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Mapper.BaihocMapper;
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
    private final BaihocMapper baihocMapper;

    public List<BaiHoc> getAllBaiHoc() {
        return baiHocRepository.findAll();
    }

    public Optional<BaiHoc> getBaiHocById(Integer id) {
        return baiHocRepository.findById(id);
    }

    public BaiHoc createBaiHoc(BaiHoc baiHoc) {
        return baiHocRepository.save(baiHoc);
    }

    public BaiHoc updateBaiHoc(Integer id, BaiHoc req) {
        BaiHoc old = baiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BaiHoc not found"));

        // idKhoaHoc NOT NULL
        old.setIdKhoaHoc(req.getIdKhoaHoc());
        old.setTenBaiHoc(req.getTenBaiHoc());
        old.setMoTa(req.getMoTa());
        old.setNoiDung(req.getNoiDung());
        old.setThuTuBaiHoc(req.getThuTuBaiHoc());
        old.setTrangThai(req.getTrangThai());

        return baiHocRepository.save(old);
    }


    public void deleteBaiHoc(Integer id) {
        baiHocRepository.deleteById(id);
    }

    public List<BaiHoc> getBaiHocByKhoaHocId(Integer khoaHocId) {
        return baiHocRepository.findByIdKhoaHoc(khoaHocId);
    }
}
