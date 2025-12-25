package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Repository.TienTrinhBaiHocRepository;
import com.example.EnglishLearningApp.Service.TienTrinhBaiHocService;
import com.example.EnglishLearningApp.dto.response.LatestLearningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tientrinhbaihoc")
@RequiredArgsConstructor
public class TienTrinhBaiHocController {
    private final TienTrinhBaiHocService service;
    private final TienTrinhBaiHocRepository tienTrinhBaiHocRepository;

    @GetMapping
    public List<TienTrinhBaiHoc> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TienTrinhBaiHoc> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TienTrinhBaiHoc create(@RequestBody TienTrinhBaiHoc tienTrinh) {
        return service.create(tienTrinh);
    }

    @PutMapping("/{id}")
    public TienTrinhBaiHoc update(@PathVariable Integer id, @RequestBody TienTrinhBaiHoc tienTrinh) {
        return service.update(id, tienTrinh);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/gan-nhat")
    public ResponseEntity<LatestLearningDto> getLatestLearning(Authentication authentication) {
        LatestLearningDto result = tienTrinhBaiHocRepository.findLatestLessonByUserId(Integer.valueOf(authentication.getName()));
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.noContent().build();
    }
}