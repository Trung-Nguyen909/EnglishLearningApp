package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.KhoaHoc;
import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Repository.NguoiDungRepository;
import com.example.EnglishLearningApp.Service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class NguoiDungController {
    private final NguoiDungService nguoiDungService;

    @GetMapping
    public List<NguoiDung> getAllUser(){
        return nguoiDungService.getAllNguoiDung();
    }
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getUserById(@PathVariable Integer id){
        return nguoiDungService.findNguoiDungById(id).map(ResponseEntity::ok).orElseThrow();
    }
    @PostMapping
    public NguoiDung createUser(@RequestBody NguoiDung nguoiDung){
        return nguoiDungService.createUser(nguoiDung);
    }
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateUser(@PathVariable Integer id, @RequestBody NguoiDung nguoiDung){
        return ResponseEntity.ok(nguoiDungService.capNhatNguoiDung(id,nguoiDung));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        nguoiDungService.xoaNguoiDung(id);
        return ResponseEntity.notFound().build();
    }
}
