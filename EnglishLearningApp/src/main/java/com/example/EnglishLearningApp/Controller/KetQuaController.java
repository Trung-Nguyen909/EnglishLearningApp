package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.KetQua;
import com.example.EnglishLearningApp.Service.KetQuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ketqua")
@RequiredArgsConstructor
public class KetQuaController {

    private final KetQuaService ketQuaService;

    @GetMapping
    public List<KetQua> getAllKetQua() {
        return ketQuaService.getAllKetQua();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KetQua> getKetQuaById(@PathVariable Integer id) {
        return ketQuaService.getKetQuaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public KetQua createKetQua(@RequestBody KetQua ketQua) {
        return ketQuaService.createKetQua(ketQua);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KetQua> updateKetQua(@PathVariable Integer id, @RequestBody KetQua ketQua) {
        return ResponseEntity.ok(ketQuaService.updateKetQua(id, ketQua));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKetQua(@PathVariable Integer id) {
        ketQuaService.deleteKetQua(id);
        return ResponseEntity.noContent().build();
    }
}
