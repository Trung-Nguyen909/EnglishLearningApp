package com.example.EnglishLearningApp.Controller;


import com.example.EnglishLearningApp.Model.BaiHoc;
import com.example.EnglishLearningApp.Service.BaiHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baihoc")
@RequiredArgsConstructor
public class BaiHocController {

    private final BaiHocService baiHocService;

    @GetMapping
    public List<BaiHoc> getAllBaiHoc() {
        return baiHocService.getAllBaiHoc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaiHoc> getBaiHocById(@PathVariable Integer id) {
        return baiHocService.getBaiHocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BaiHoc createBaiHoc(@RequestBody BaiHoc baiHoc) {
        return baiHocService.createBaiHoc(baiHoc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaiHoc> updateBaiHoc(@PathVariable Integer id, @RequestBody BaiHoc baiHoc) {
        return ResponseEntity.ok(baiHocService.updateBaiHoc(id, baiHoc));
    }

    @PostMapping("/{id}/upload-icon")
    public ResponseEntity<?> uploadIcon(@PathVariable Integer id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            String iconUrl = baiHocService.updateBaiHocIcon(id, file);
            return ResponseEntity.ok(java.util.Collections.singletonMap("iconUrl", iconUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaiHoc(@PathVariable Integer id) {
        baiHocService.deleteBaiHoc(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/khoahoc/{idKhoaHoc}")
    public ResponseEntity<List<BaiHoc>> getBaiHocByKhoaHoc(@PathVariable Integer idKhoaHoc) {
        return ResponseEntity.ok(baiHocService.getBaiHocByKhoaHocId(idKhoaHoc));
    }
}
