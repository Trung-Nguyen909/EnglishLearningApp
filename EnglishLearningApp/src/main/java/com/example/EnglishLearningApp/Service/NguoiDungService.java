package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Controller.GmailSender;
import com.example.EnglishLearningApp.Exception.AppException;
import com.example.EnglishLearningApp.Exception.ErrorCode;
import com.example.EnglishLearningApp.Mapper.NguoiDungMapper;
import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Repository.NguoiDungRepository;
import com.example.EnglishLearningApp.dto.request.UserLoginRequest;
import com.example.EnglishLearningApp.dto.request.UserRegisterRequest;
import com.example.EnglishLearningApp.dto.response.AuthResponse;
import com.example.EnglishLearningApp.dto.response.SkillDto;
import com.example.EnglishLearningApp.dto.response.UserResponse;
import com.example.EnglishLearningApp.dto.response.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository ;
    private final NguoiDungMapper nguoiDungMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GmailSender gmailSender;
    private final JdbcTemplate jdbc;

    public List<NguoiDung> getAllNguoiDung(){
        return nguoiDungRepository.findAll();
    }

    public Optional<NguoiDung> findNguoiDungById(Integer id){
        return nguoiDungRepository.findById(id);
    }

    public NguoiDung createUser(UserRegisterRequest nguoiDung){

        NguoiDung nguoiDung1 =  nguoiDungMapper.toNguoiDung(nguoiDung);
        nguoiDung1.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        return nguoiDungRepository.save(nguoiDung1);
    }

    public AuthResponse Login(UserLoginRequest user)
    {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(user.getEmail());
        if (nguoiDung == null) throw new AppException(ErrorCode.Not_Found);
        if (!passwordEncoder.matches(user.getMatkhau(), nguoiDung.getMatKhau())) throw new AppException(ErrorCode.Wrong_Password);
        String token = jwtService.createToken(nguoiDung);
        NguoiDung newNguoiDung = updateStreak(nguoiDung);
        System.out.println(newNguoiDung.getId());
        UserResponse userResponse = nguoiDungMapper.toUserResponse(newNguoiDung);
        String userEmail = nguoiDung.getEmail();
        gmailSender.sendEmail(userEmail,
                "Thông báo đăng nhập",
                "Bạn vừa đăng nhập thành công vào hệ thống!");
        userResponse.setAnhDaiDien("http://10.0.2.2:8080/img_user/user_avatar/" + newNguoiDung.getAnhDaiDien());
        userResponse.setStreak(newNguoiDung.getStreak());
        userResponse.setLastLogin(newNguoiDung.getLastLogin());
        System.out.println(userResponse.getId());
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

        String fileName = tokenUser.getId() + "_ava" + extension;
        Path path = Paths.get("img_user/user_avatar", fileName);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Avatar saved to: " + path.toAbsolutePath());
        tokenUser.setAnhDaiDien(fileName);
        nguoiDungRepository.save(tokenUser);
        return fileName;
    }

    public NguoiDung updateStreak(NguoiDung nguoiDung) {
        LocalDateTime lastLoginDateTime = nguoiDung.getLastLogin();
        LocalDate today = LocalDate.now();
        if (lastLoginDateTime == null) {
            nguoiDung.setStreak(1);
        } else {
            LocalDate lastLoginDate = lastLoginDateTime.toLocalDate();

            if (lastLoginDate.equals(today)) {
            } else if (lastLoginDate.plusDays(1).equals(today)) {
                nguoiDung.setStreak(nguoiDung.getStreak() + 1);
            } else {
                nguoiDung.setStreak(1);
            }
        }
        nguoiDung.setLastLogin(LocalDateTime.now());
        return nguoiDungRepository.save(nguoiDung);
    }


    public NguoiDung findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    public UserSummaryDto getInfoUser(int userId) {
        String sqlThanhTuu = "select count(*) from nguoiDung n join NguoiDung_ThanhTich nd on n.ID = nd.IDNguoiDung where n.ID = ?";
        Integer soThanhTuu = jdbc.queryForObject(sqlThanhTuu, Integer.class, userId);

        String sqlKyNang = "select k.TenKyNang, dg.diem " +
                "from DanhGiaKyNang dg join KyNang k on k.ID = dg.IDKyNang " +
                "where dg.IDNguoiDung = ?";
        List<SkillDto> kyNang = jdbc.query(sqlKyNang, new Object[]{userId}, (rs, rn) ->
                new SkillDto(rs.getString("TenKyNang"), rs.getInt("diem"))
        );

        String sqlBaiHoc = "select count(*) from TienTrinhKhoaHoc t where t.IdUser = ? and (t.trangthai = N'Đang học' or t.trangthai = N'Đã học')";
        Integer soBaiHoc = jdbc.queryForObject(sqlBaiHoc, Integer.class, userId);

        return new UserSummaryDto(
                soThanhTuu == null ? 0 : soThanhTuu,
                soBaiHoc == null ? 0 : soBaiHoc,
                kyNang
        );
    }
}
