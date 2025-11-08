package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TuVungYeuThich;
import com.example.EnglishLearningApp.Service.TuVungYeuThichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tuvungyeuthich")
@RequiredArgsConstructor
public class TuVungYeuThichController {

    private final TuVungYeuThichService service;

    @GetMapping
    public List<TuVungYeuThich> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuVungYeuThich> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TuVungYeuThich create(@RequestBody TuVungYeuThich tuVungYeuThich) {
        return service.create(tuVungYeuThich);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TuVungYeuThich> update(@PathVariable Integer id, @RequestBody TuVungYeuThich tuVungYeuThich) {
        return ResponseEntity.ok(service.update(id, tuVungYeuThich));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
