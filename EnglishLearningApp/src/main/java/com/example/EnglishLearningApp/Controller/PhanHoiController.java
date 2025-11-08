package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.PhanHoi;
import com.example.EnglishLearningApp.Service.PhanHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phanhoi")
@RequiredArgsConstructor
public class PhanHoiController {

    private final PhanHoiService phanHoiService;

    @GetMapping
    public List<PhanHoi> getAllPhanHoi() {
        return phanHoiService.getAllPhanHoi();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhanHoi> getPhanHoiById(@PathVariable Integer id) {
        return phanHoiService.getPhanHoiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PhanHoi createPhanHoi(@RequestBody PhanHoi phanHoi) {
        return phanHoiService.createPhanHoi(phanHoi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhanHoi> updatePhanHoi(@PathVariable Integer id, @RequestBody PhanHoi phanHoi) {
        return ResponseEntity.ok(phanHoiService.updatePhanHoi(id, phanHoi));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhanHoi(@PathVariable Integer id) {
        phanHoiService.deletePhanHoi(id);
        return ResponseEntity.noContent().build();
    }
}
