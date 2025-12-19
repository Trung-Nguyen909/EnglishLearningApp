package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.NhatKyHoatDong;
import com.example.EnglishLearningApp.Service.NhatKyHoatDongService;
import com.example.EnglishLearningApp.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/my-activity-log")
    public List<NhatKyHoatDong> getMyActivityLog(Authentication authentication) {
        return nhatKyHoatDongService.getNhatKyByUserId(Integer.parseInt(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createNhatKy(Authentication authentication) {
        if(nhatKyHoatDongService.createNhatKy(Integer.parseInt(authentication.getName())))
        {
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(200)
                    .message("Created activity log for today")
                    .result(null)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .code(400)
                .message("Activity log for today already exists")
                .result(null)
                .build());
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