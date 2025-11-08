package com.example.EnglishLearningApp.Controller;

import com.example.EnglishLearningApp.Model.NguoiDung_BinhLuan;
import com.example.EnglishLearningApp.Service.NguoiDungBinhLuanService;
import com.example.EnglishLearningApp.dto.response.ApiResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/UserComment")
@RequiredArgsConstructor
public class NguoiDungBinhLuanController {
    private final NguoiDungBinhLuanService nguoiDungBinhLuanService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NguoiDung_BinhLuan>>> getAllNGUoiDUng()
    {
        return ResponseEntity.ok(ApiResponse.<List<NguoiDung_BinhLuan>>builder()
                        .code(1000)
                        .message("Thành Công")
                        .result(nguoiDungBinhLuanService.getAllNguoiDngbl())
                        .build());
    }
    @GetMapping("/get")
    public ResponseEntity<ApiResponse<?>> getNguoiDung(@PathVariable int id)
    {
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(1000)
                        .result(nguoiDungBinhLuanService.getNguoiDung(id))
                        .build());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> DeleteNguoiDung(@PathVariable int id)
    {
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(1000)
                        .result(nguoiDungBinhLuanService.DeleteNguoiDung(id))
                        .build());
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateNguoiDung(@PathVariable int id, @RequestBody NguoiDung_BinhLuan nguoiDungBinhLuan)
    {
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(1000)
                        .result(nguoiDungBinhLuanService.updatenguoiDUng(nguoiDungBinhLuan,id))
                        .build());
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> AddNguoiDung(@RequestBody NguoiDung_BinhLuan nguoiDungBinhLuan)
    {
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(1000)
                        .result(nguoiDungBinhLuanService.tao1NguoiDung(nguoiDungBinhLuan))
                        .build());
    }
}
