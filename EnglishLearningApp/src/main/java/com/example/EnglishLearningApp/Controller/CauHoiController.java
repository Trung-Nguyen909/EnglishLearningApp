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

    // 1. Lấy tất cả
    @GetMapping
    public List<CauHoi> getAllCauHoi() {
        return cauHoiService.getAllCauHoi();
    }

    // 2. Lấy chi tiết 1 câu
    @GetMapping("/{id}")
    public ResponseEntity<CauHoi> getCauHoiById(@PathVariable Integer id) {
        return ResponseEntity.of(cauHoiService.getCauHoiById(id));
    }

    // 3. Tạo mới
    @PostMapping
    public CauHoi createCauHoi(@RequestBody CauHoi cauHoi) {
        return cauHoiService.createCauHoi(cauHoi);
    }

    // 4. Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<CauHoi> updateCauHoi(@PathVariable Integer id, @RequestBody CauHoi cauHoi) {
        return ResponseEntity.ok(cauHoiService.updateCauHoi(id,cauHoi));
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCauHoi(@PathVariable Integer id) {
        cauHoiService.deleteCauHoi(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================================================
    // 6. API QUAN TRỌNG: Lấy danh sách câu hỏi theo ID Bài Tập (Cho Android)
    // URL: http://localhost:8080/cauhoi/baitap/1
    // ==================================================================
    @GetMapping("/baitap/{idBaiTap}")
    public ResponseEntity<List<CauHoi>> getCauHoiByBaiTap(@PathVariable Integer idBaiTap) {
        List<CauHoi> list = cauHoiService.getCauHoiByBaiTapId(idBaiTap);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    // 7. API cũ (Lấy theo bài tập và cấp độ)
    @GetMapping("/baitap/{idBaiTap}/capdo/{idCapDo}")
    public ResponseEntity<List<CauHoi>> getCauHoiTheoBaiTapVaCapDo(
            @PathVariable int idBaiTap,
            @PathVariable int idCapDo
    ) {
        List<CauHoi> list = cauHoiService.GetCauHoiByBaiHocAndCapDo(idBaiTap, idCapDo);
        return ResponseEntity.ok(list);
    }
}