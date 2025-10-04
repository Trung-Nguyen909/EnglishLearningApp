package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TienTrinh;
import com.example.EnglishLearningApp.Service.TienTrinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tientrinh")
@RequiredArgsConstructor
public class TienTrinhController {
    private final TienTrinhService tienTrinhService;

    @GetMapping
    public List<TienTrinh> getAllTienTrinh(){
        return tienTrinhService.getAllTienTrinh();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TienTrinh> getTienTrinhById(@PathVariable Integer id){
        return tienTrinhService.getTienTrinhById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TienTrinh> updateTienTrinh(@PathVariable Integer id, @RequestBody TienTrinh tienTrinh) {
        return ResponseEntity.ok(tienTrinhService.updateTienTrinh(id, tienTrinh));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTienTrinh(@PathVariable Integer id) {
        tienTrinhService.deleteTienTrinh(id);
        return ResponseEntity.noContent().build();
    }
}
