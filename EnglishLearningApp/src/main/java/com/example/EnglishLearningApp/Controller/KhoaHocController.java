package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.KhoaHoc;
import com.example.EnglishLearningApp.Service.KhoaHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/KhoaHoc")
@RequiredArgsConstructor
public class KhoaHocController {
    private final KhoaHocService khoaHocService ;
    @GetMapping
    public List<KhoaHoc> getAllKhoaHoc(){
        return khoaHocService.getAllKhoaHoc();
    }
    @GetMapping("/{id}")
    public ResponseEntity<KhoaHoc> getKhoaHocById(@PathVariable Integer id){
        return khoaHocService.findKhoaHocById(id).map(ResponseEntity::ok).orElseThrow();
    }
    @PostMapping
    public KhoaHoc createKhoaHoc(@RequestBody KhoaHoc khoaHoc){
        return khoaHocService.taoKhoaHoc(khoaHoc);
    }
    @PutMapping("/id")
    public ResponseEntity<KhoaHoc> updateKhoaHoc(@PathVariable Integer id,@RequestBody KhoaHoc khoaHocChiTiet){
        return ResponseEntity.ok(khoaHocService.capNhatKhoaHoc(id,khoaHocChiTiet));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKhoaHoc(@PathVariable Integer id){
         khoaHocService.xoaKhoaHoc(id);
         return ResponseEntity.noContent().build();
    }
}
