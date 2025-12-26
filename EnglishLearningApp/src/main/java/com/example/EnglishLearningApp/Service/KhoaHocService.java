package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Mapper.KhoahocMapper;
import com.example.EnglishLearningApp.Model.KhoaHoc;
import com.example.EnglishLearningApp.Repository.KhoaHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KhoaHocService {
   private final KhoaHocRepository khoaHocRepository ;
   private final KhoahocMapper khoahocMapper;
    private final BaiHocService baiHocService;

   public List<KhoaHoc> getAllKhoaHoc(){
       return khoaHocRepository.findAll();
   }

   public Optional<KhoaHoc> findKhoaHocById(Integer id){
       return khoaHocRepository.findById(id);
   }

   public KhoaHoc taoKhoaHoc(KhoaHoc khoaHoc){
       return khoaHocRepository.save(khoaHoc);
   }

   public KhoaHoc capNhatKhoaHoc(Integer id, KhoaHoc khoaHocChiTiet) {
       return khoaHocRepository.findById(id).map(khoaHoc -> {
           if (khoaHocChiTiet.getTenKhoaHoc() != null) {
               khoaHoc.setTenKhoaHoc(khoaHocChiTiet.getTenKhoaHoc());
           }
           if (khoaHocChiTiet.getMoTa() != null) {
               khoaHoc.setMoTa(khoaHocChiTiet.getMoTa());
           }
           if (khoaHocChiTiet.getTrinhDo() != null) {
               khoaHoc.setTrinhDo(khoaHocChiTiet.getTrinhDo());
           }
           if (khoaHocChiTiet.getIconUrl() != null) {
               khoaHoc.setIconUrl(khoaHocChiTiet.getIconUrl());
           }
           return khoaHocRepository.save(khoaHoc);
       }).orElseThrow(() -> new RuntimeException("Không tìm thấy Khóa học với ID: " + id));

   }

   public void xoaKhoaHoc(Integer id){
        khoaHocRepository.deleteById(id);
   }
}
