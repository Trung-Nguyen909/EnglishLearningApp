package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.DanhGiaKyNang;
import com.example.EnglishLearningApp.Service.DanhGiaKyNangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/danhgiakynang")
@RequiredArgsConstructor
public class DanhGiaKyNangController {

    private final DanhGiaKyNangService danhGiaKyNangService;

    @GetMapping
    public List<DanhGiaKyNang> getAllDanhGiaKyNang() {
        return danhGiaKyNangService.getAllDanhGiaKyNang();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DanhGiaKyNang> getDanhGiaKyNangById(@PathVariable Integer id) {
        return danhGiaKyNangService.getDanhGiaKyNangById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DanhGiaKyNang createDanhGiaKyNang(@RequestBody DanhGiaKyNang danhGiaKyNang) {
        return danhGiaKyNangService.createDanhGiaKyNang(danhGiaKyNang);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhGiaKyNang> updateDanhGiaKyNang(@PathVariable Integer id, @RequestBody DanhGiaKyNang danhGiaKyNang) {
        return ResponseEntity.ok(danhGiaKyNangService.updateDanhGiaKyNang(id, danhGiaKyNang));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhGiaKyNang(@PathVariable Integer id) {
        danhGiaKyNangService.deleteDanhGiaKyNang(id);
        return ResponseEntity.noContent().build();
    }
}
