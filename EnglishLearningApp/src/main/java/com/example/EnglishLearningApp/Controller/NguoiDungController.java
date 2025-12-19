package com.example.EnglishLearningApp.Controller;


import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Service.NguoiDungService;
import com.example.EnglishLearningApp.dto.request.ChangePasswordRequest;
import com.example.EnglishLearningApp.dto.request.UserLoginRequest;
import com.example.EnglishLearningApp.dto.request.UserRegisterRequest;
import com.example.EnglishLearningApp.dto.response.ApiResponse;
import com.example.EnglishLearningApp.dto.response.AuthResponse;
import com.example.EnglishLearningApp.dto.response.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class NguoiDungController {
    private final NguoiDungService nguoiDungService;

    @GetMapping("/admin/all")
    public List<NguoiDung> getAllUser(){
        return nguoiDungService.getAllNguoiDung();
    }
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getUserById(@PathVariable Integer id){
        return nguoiDungService.findNguoiDungById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping("/register")
    public NguoiDung createUser(@RequestBody UserRegisterRequest nguoiDung){
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
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest user) {
        return ResponseEntity.ok(nguoiDungService.Login(user));
    }
    @PostMapping("/changePass")
    public ResponseEntity<ApiResponse<?>> updatePass(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication)
    {
        String email = authentication.getName();
        nguoiDungService.updateMatkhau(request.getOldPass(), request.getNewPass(), email);
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(1000)
                        .message("update thành công")
                        .build());
    }
    @GetMapping("/summary")
    public ResponseEntity<UserSummaryDto> getInforUser(Authentication authentication) {
        System.out.println(authentication.getName());
        UserSummaryDto dto = nguoiDungService.getInfoUser(Integer.parseInt(authentication.getName()));
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/upAva")
    public ResponseEntity<ApiResponse<String>> updateAvatar(
            @RequestParam("avatar") MultipartFile file,
            Authentication authentication) {
        if (file.isEmpty()) return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .code(1001)
                .message("ảnh không hợp lệ")
                .build());
        String email = authentication.getName();
        try {
            String link_ava = nguoiDungService.updateAvatar(email, file);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .message("Cập nhật thành công avatar")
                    .result(link_ava)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<String>builder()
                            .code(500)
                            .message("Lỗi khi cập nhật avatar: " + e.getMessage())
                            .build());
        }
    }
}
