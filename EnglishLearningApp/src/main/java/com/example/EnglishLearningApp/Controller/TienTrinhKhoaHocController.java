package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TienTrinhKhoaHoc;
import com.example.EnglishLearningApp.Service.TienTrinhKhoaHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tientrinhkhoahoc")
@RequiredArgsConstructor
public class TienTrinhKhoaHocController {
    private final TienTrinhKhoaHocService service;

    @GetMapping
    public List<TienTrinhKhoaHoc> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TienTrinhKhoaHoc> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TienTrinhKhoaHoc create(@RequestBody TienTrinhKhoaHoc tienTrinh) {
        return service.create(tienTrinh);
    }

    @PutMapping("/{id}")
    public TienTrinhKhoaHoc update(@PathVariable Integer id, @RequestBody TienTrinhKhoaHoc tienTrinh) {
        return service.update(id, tienTrinh);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}