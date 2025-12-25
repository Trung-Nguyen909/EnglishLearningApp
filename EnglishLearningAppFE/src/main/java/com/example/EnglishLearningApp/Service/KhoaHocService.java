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

   public KhoaHoc capNhatKhoaHoc(Integer id, KhoaHoc khoaHocChiTiet){
       return khoaHocRepository.findById(id).map(khoaHoc -> {
              khoahocMapper.toKhoaHoc(khoaHocChiTiet);
              return khoaHocRepository.save(khoaHoc);
       }).orElseThrow(() -> new RuntimeException("Không tìm thấy Khóa học với ID: " + id));

   }

   public void xoaKhoaHoc(Integer id){
        khoaHocRepository.deleteById(id);
   }
}
