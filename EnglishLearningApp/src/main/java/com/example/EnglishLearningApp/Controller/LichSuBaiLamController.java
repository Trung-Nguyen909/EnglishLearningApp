package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.LichSuBaiLam;
import com.example.EnglishLearningApp.Service.LichSuBaiLamService;
import com.example.EnglishLearningApp.dto.request.SubmitBaiLamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/submit")
    public ResponseEntity<LichSuBaiLam> submitBaiLam(
            Authentication authentication,
            @RequestBody SubmitBaiLamRequest submitRequest) {
        Integer idNguoiDung = Integer.valueOf(authentication.getName());
        LichSuBaiLam savedRecord = lichSuBaiLamService.submitBaiLam(idNguoiDung, submitRequest);
        return ResponseEntity.ok(savedRecord);
    }
}
