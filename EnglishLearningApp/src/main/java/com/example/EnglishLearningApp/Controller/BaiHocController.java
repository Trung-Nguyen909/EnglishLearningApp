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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaiHoc(@PathVariable Integer id) {
        baiHocService.deleteBaiHoc(id);
        return ResponseEntity.noContent().build();
    }
}
