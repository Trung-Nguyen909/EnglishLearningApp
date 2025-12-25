package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.NguPhap;
import com.example.EnglishLearningApp.Model.TuVung;
import com.example.EnglishLearningApp.Service.NguPhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nguphap")
@RequiredArgsConstructor
public class NguPhapController {

    private final NguPhapService nguPhapService;

    @GetMapping
    public List<NguPhap> getAllNguPhap() {
        return nguPhapService.getAllNguPhap();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NguPhap> getNguPhapById(@PathVariable Integer id) {
        return nguPhapService.getNguPhapById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NguPhap createNguPhap(@RequestBody NguPhap nguPhap) {
        return nguPhapService.createNguPhap(nguPhap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NguPhap> updateNguPhap(@PathVariable Integer id, @RequestBody NguPhap nguPhap) {
        return ResponseEntity.ok(nguPhapService.updateNguPhap(id, nguPhap));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNguPhap(@PathVariable Integer id) {
        nguPhapService.deleteNguPhap(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bai-hoc/{idBaiHoc}")
    public ResponseEntity<List<NguPhap>> getNguPhapByBaiHoc(@PathVariable Integer idBaiHoc) {
        List<NguPhap> list = nguPhapService.getNguPhapByBaiHoc(idBaiHoc);

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
