package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.TuVung;
import com.example.EnglishLearningApp.Service.TuVungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tuvung")
@RequiredArgsConstructor
public class TuVungController {

    private final TuVungService tuVungService;

    @GetMapping
    public List<TuVung> getAllTuVung() {
        return tuVungService.getAllTuVung();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuVung> getTuVungById(@PathVariable Integer id) {
        return tuVungService.getTuVungById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TuVung createTuVung(@RequestBody TuVung tuVung) {
        return tuVungService.createTuVung(tuVung);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TuVung> updateTuVung(@PathVariable Integer id, @RequestBody TuVung tuVung) {
        return ResponseEntity.ok(tuVungService.updateTuVung(id, tuVung));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTuVung(@PathVariable Integer id) {
        tuVungService.deleteTuVung(id);
        return ResponseEntity.noContent().build();
    }
}
