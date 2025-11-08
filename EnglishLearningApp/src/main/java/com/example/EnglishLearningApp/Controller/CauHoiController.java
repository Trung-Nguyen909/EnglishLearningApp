package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.CauHoi;
import com.example.EnglishLearningApp.Service.CauHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cauhoi")
@RequiredArgsConstructor
public class CauHoiController {

    private final CauHoiService cauHoiService;

    @GetMapping
    public List<CauHoi> getAllCauHoi() {
        return cauHoiService.getAllCauHoi();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CauHoi> getCauHoiById(@PathVariable Integer id) {
        return ResponseEntity.of(cauHoiService.getCauHoiById(id));
    }

    @PostMapping
    public CauHoi createCauHoi(@RequestBody CauHoi cauHoi) {
        return cauHoiService.createCauHoi(cauHoi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CauHoi> updateCauHoi(@PathVariable Integer id, @RequestBody CauHoi cauHoi) {
        return ResponseEntity.ok(cauHoiService.updateCauHoi(id,cauHoi));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCauHoi(@PathVariable Integer id) {
        cauHoiService.deleteCauHoi(id);
        return ResponseEntity.noContent().build();
    }
}
