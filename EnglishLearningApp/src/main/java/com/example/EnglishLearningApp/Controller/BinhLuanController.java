package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.BinhLuan;
import com.example.EnglishLearningApp.Service.BinhLuanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/binhluan")
@RequiredArgsConstructor
public class BinhLuanController {

    private final BinhLuanService service;

    @GetMapping
    public List<BinhLuan> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BinhLuan> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BinhLuan create(@RequestBody BinhLuan binhLuan) {
        return service.create(binhLuan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BinhLuan> update(@PathVariable Integer id, @RequestBody BinhLuan binhLuan) {
        return ResponseEntity.ok(service.update(id, binhLuan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
