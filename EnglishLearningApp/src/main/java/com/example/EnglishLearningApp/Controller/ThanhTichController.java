package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.ThanhTich;
import com.example.EnglishLearningApp.Service.ThanhTichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thanhtich")
@RequiredArgsConstructor
public class ThanhTichController {

    private final ThanhTichService thanhTichService;

    @GetMapping
    public List<ThanhTich> getAllThanhTich() {
        return thanhTichService.getAllThanhTich();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThanhTich> getThanhTichById(@PathVariable Integer id) {
        return thanhTichService.getThanhTichById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ThanhTich createThanhTich(@RequestBody ThanhTich thanhTich) {
        return thanhTichService.createThanhTich(thanhTich);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThanhTich> updateThanhTich(@PathVariable Integer id, @RequestBody ThanhTich thanhTich) {
        return ResponseEntity.ok(thanhTichService.updateThanhTich(id, thanhTich));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThanhTich(@PathVariable Integer id) {
        thanhTichService.deleteThanhTich(id);
        return ResponseEntity.noContent().build();
    }
}
