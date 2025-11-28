package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.NhatKyHoatDong;
import com.example.EnglishLearningApp.Service.NhatKyHoatDongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nhatkyhoatdong")
@RequiredArgsConstructor
public class NhatKyHoatDongController {

    private final NhatKyHoatDongService nhatKyHoatDongService;

    @GetMapping
    public List<NhatKyHoatDong> getAllNhatKy() {
        return nhatKyHoatDongService.getAllNhatKy();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhatKyHoatDong> getNhatKyById(@PathVariable Integer id) {
        return nhatKyHoatDongService.getNhatKyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API lấy nhật ký theo User ID (Ví dụ: /nhatkyhoatdong/user/1)
    @GetMapping("/user/{userId}")
    public List<NhatKyHoatDong> getNhatKyByUserId(@PathVariable Integer userId) {
        return nhatKyHoatDongService.getNhatKyByUserId(userId);
    }

    @PostMapping
    public NhatKyHoatDong createNhatKy(@RequestBody NhatKyHoatDong nhatKy) {
        return nhatKyHoatDongService.createNhatKy(nhatKy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhatKyHoatDong> updateNhatKy(@PathVariable Integer id, @RequestBody NhatKyHoatDong nhatKy) {
        return ResponseEntity.ok(nhatKyHoatDongService.updateNhatKy(id, nhatKy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNhatKy(@PathVariable Integer id) {
        nhatKyHoatDongService.deleteNhatKy(id);
        return ResponseEntity.noContent().build();
    }
}