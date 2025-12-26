package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.BaiTap;
import com.example.EnglishLearningApp.Repository.BaiTapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaiTapService {
    private final BaiTapRepository baiTapRepository;

    public void xoaBaiTapTheoIdBaiHoc(Integer idBaiHoc){
        baiTapRepository.deleteByIdBaiHoc(idBaiHoc);
    }
    public List<BaiTap> getBaiTapTheoIdBaiHoc(Integer idBaiHoc){
        return baiTapRepository.findByIdBaiHoc(idBaiHoc);
    }
    public List<BaiTap> GetAllBaiTap(){
        return baiTapRepository.findAll();
    }

    public List<BaiTap> GetBaiTapByLoaiBaiTap(String loaiBT) {
        return baiTapRepository.findByLoaiBaiTap(loaiBT);
    }

    public BaiTap updateBaiTap(Integer id, BaiTap baiTapDetails) {
        return baiTapRepository.findById(id)
                .map(baiTap -> {
                    if (baiTapDetails.getIdBaiHoc() != null) baiTap.setIdBaiHoc(baiTapDetails.getIdBaiHoc());
                    if (baiTapDetails.getTenBaiTap() != null && !baiTapDetails.getTenBaiTap().isEmpty()) baiTap.setTenBaiTap(baiTapDetails.getTenBaiTap());
                    if (baiTapDetails.getLoaiBaiTap() != null) baiTap.setLoaiBaiTap(baiTapDetails.getLoaiBaiTap());
                    if (baiTapDetails.getTrangThai() != null) baiTap.setTrangThai(baiTapDetails.getTrangThai());
                    if (baiTapDetails.getCapdo() != null) baiTap.setCapdo(baiTapDetails.getCapdo());
                    if (baiTapDetails.getThoigian() != null) baiTap.setThoigian(baiTapDetails.getThoigian());
                    
                    return baiTapRepository.save(baiTap);
                })
                .orElseThrow(() -> new RuntimeException("BaiTap not found with id " + id));
    }

    public BaiTap addBaiTap(BaiTap baiTap) {
        return baiTapRepository.save(baiTap);
    }

    public void deleteBaiTap(Integer id) {
        baiTapRepository.deleteById(id);
    }
}
