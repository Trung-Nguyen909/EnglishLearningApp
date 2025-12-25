package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.LichSuBaiLam;
import com.example.EnglishLearningApp.Service.LichSuBaiLamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/lichsu")
@RestController
@RequiredArgsConstructor
public class LichSuBaiLamController {
    private final LichSuBaiLamService lichSuBaiLamService;

    @GetMapping("/user")
    public ResponseEntity<List<LichSuBaiLam>> getLichSuBaiLamByUserId(Authentication authentication) {
        return ResponseEntity.ok(lichSuBaiLamService.findByIdNguoiDung(Integer.valueOf(authentication.getName())));
    }
}
