package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.KyNang;
import com.example.EnglishLearningApp.Service.KyNangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kynang")
@RequiredArgsConstructor
public class KyNangController {

    private final KyNangService kyNangService;

    @GetMapping
    public List<KyNang> getAllKyNang() {
        return kyNangService.getAllKyNang();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KyNang> getKyNangById(@PathVariable Integer id) {
        return kyNangService.getKyNangById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public KyNang createKyNang(@RequestBody KyNang kyNang) {
        return kyNangService.createKyNang(kyNang);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KyNang> updateKyNang(@PathVariable Integer id, @RequestBody KyNang kyNang) {
        return ResponseEntity.ok(kyNangService.updateKyNang(id, kyNang));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKyNang(@PathVariable Integer id) {
        kyNangService.deleteKyNang(id);
        return ResponseEntity.noContent().build();
    }
}
