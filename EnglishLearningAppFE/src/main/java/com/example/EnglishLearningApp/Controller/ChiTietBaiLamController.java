package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Service.ChiTietBaiLamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bai-lam")
@RequiredArgsConstructor
public class ChiTietBaiLamController {
    private final ChiTietBaiLamService baiLamService;

    @GetMapping("/lich-su/{id}")
    public ResponseEntity<?> getChiTiet(@PathVariable Integer id) {
        return ResponseEntity.ok(
                baiLamService.getChiTietBaiLam(id)
        );
    }
}
