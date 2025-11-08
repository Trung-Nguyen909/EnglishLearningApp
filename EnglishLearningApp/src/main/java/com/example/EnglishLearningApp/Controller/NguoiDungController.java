package com.example.EnglishLearningApp.Controller;


import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Service.NguoiDungService;
import com.example.EnglishLearningApp.dto.request.ChangePasswordRequest;
import com.example.EnglishLearningApp.dto.request.UserLoginRequest;
import com.example.EnglishLearningApp.dto.request.UserRegisterRequest;
import com.example.EnglishLearningApp.dto.response.ApiResponse;
import com.example.EnglishLearningApp.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
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
}
