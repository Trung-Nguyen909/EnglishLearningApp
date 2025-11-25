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
}
