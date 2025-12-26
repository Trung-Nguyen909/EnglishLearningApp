package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.BaiTap;
import com.example.EnglishLearningApp.Repository.BaiTapRepository;
import com.example.EnglishLearningApp.Service.BaiHocService;
import com.example.EnglishLearningApp.Service.BaiTapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/baitap")
public class BaiTapController {
    private final BaiTapService baiTapService;

    @GetMapping("/baihoc/{idbaihoc}")
    public List<BaiTap> getBaiTapByBaiHocId(@PathVariable Integer idbaihoc) {
        return baiTapService.getBaiTapTheoIdBaiHoc(idbaihoc);
    }
    @GetMapping("/all")
    public List<BaiTap> getAllBaiTap() {
        return baiTapService.GetAllBaiTap();
    }

    @GetMapping("/{loaiBT}")
    public List<BaiTap> GetBaiTapByLoaiBaitap(@PathVariable String loaiBT){
        return baiTapService.GetBaiTapByLoaiBaiTap(loaiBT);
    }

    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<BaiTap> updateBaiTap(@PathVariable Integer id, @RequestBody BaiTap baiTap) {
        return org.springframework.http.ResponseEntity.ok(baiTapService.updateBaiTap(id, baiTap));
    }

    @PostMapping
    public BaiTap createBaiTap(@RequestBody BaiTap baiTap) {
        return baiTapService.addBaiTap(baiTap);
    }

    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> deleteBaiTap(@PathVariable Integer id) {
        baiTapService.deleteBaiTap(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
