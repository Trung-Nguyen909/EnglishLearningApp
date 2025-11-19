package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Controller.GmailSender;
import com.example.EnglishLearningApp.Exception.AppException;
import com.example.EnglishLearningApp.Exception.ErrorCode;
import com.example.EnglishLearningApp.Mapper.NguoiDungMapper;
import com.example.EnglishLearningApp.Mapper.UserResponseMapper;
import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Repository.NguoiDungRepository;
import com.example.EnglishLearningApp.dto.request.UserLoginRequest;
import com.example.EnglishLearningApp.dto.request.UserRegisterRequest;
import com.example.EnglishLearningApp.dto.response.AuthResponse;
import com.example.EnglishLearningApp.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository ;
    private final NguoiDungMapper nguoiDungMapper;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GmailSender gmailSender;

    public List<NguoiDung> getAllNguoiDung(){
        return nguoiDungRepository.findAll();
    }

    public Optional<NguoiDung> findNguoiDungById(Integer id){
        return nguoiDungRepository.findById(id);
    }

    public NguoiDung createUser(UserRegisterRequest nguoiDung){

        NguoiDung nguoiDung1 =  nguoiDungMapper.toNguoiDung(nguoiDung);
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        return nguoiDungRepository.save(nguoiDung1);
    }

    public AuthResponse Login(UserLoginRequest user)
    {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(user.getEmail());
        if (nguoiDung == null) throw new AppException(ErrorCode.Not_Found);
        if (!passwordEncoder.matches(user.getMatkhau(), nguoiDung.getMatKhau())) throw new AppException(ErrorCode.Wrong_Password);
        String token = jwtService.createToken(nguoiDung);
        UserResponse userResponse = userResponseMapper.toUserResponse(nguoiDung);
        String userEmail = nguoiDung.getEmail();
        GmailSender.sendEmail(userEmail,
                "Thông báo đăng nhập",
                "Bạn vừa đăng nhập thành công vào hệ thống!");
        return AuthResponse .builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public NguoiDung capNhatNguoiDung(Integer id,NguoiDung nguoiDungChiTiet){
        NguoiDung nguoiDung = nguoiDungRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.Not_Found));
        nguoiDungMapper.updateUser(nguoiDung, nguoiDungChiTiet);
        return nguoiDungRepository.save(nguoiDung);
    }
    public void updateMatkhau(String oldPass, String newPass, String email)
    {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email);
        if (nguoiDung == null) throw new AppException(ErrorCode.Not_Found);
        if (!passwordEncoder.matches(oldPass,nguoiDung.getMatKhau())) throw new AppException(ErrorCode.Wrong_Password);
        nguoiDung.setMatKhau(passwordEncoder.encode(newPass));
        nguoiDungRepository.save(nguoiDung);
    }

    public void xoaNguoiDung(Integer id){
        nguoiDungRepository.deleteById(id);
    }

    public String updateAvatar(String email, MultipartFile file) throws IOException {
        NguoiDung tokenUser = nguoiDungRepository.findByEmail(email);
        if (tokenUser.getEmail() == null) {
            throw new RuntimeException("Bạn không có quyền cập nhật thông tin của người dùng khác");
        }
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        List<String> allowed = Arrays.asList(".png", ".jpg", ".jpeg", ".webp");
        if (!allowed.contains(extension)) throw new RuntimeException("File type not allowed");

        String fileName = tokenUser.getID() + "_ava" + extension;
        Path path = Paths.get("img_user/user_avatar", fileName);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Avatar saved to: " + path.toAbsolutePath());
        String avatarUrl = "/img_user/user_avatar/" + fileName;
        tokenUser.setAnhDaiDien(avatarUrl);
        nguoiDungRepository.save(tokenUser);
        return avatarUrl;
    }
}
