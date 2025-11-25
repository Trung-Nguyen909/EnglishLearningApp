package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TienTrinhBaiHoc;
import com.example.EnglishLearningApp.Service.TienTrinhBaiHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tientrinhbaihoc")
@RequiredArgsConstructor
public class TienTrinhBaiHocController {
    private final TienTrinhBaiHocService service;

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
}