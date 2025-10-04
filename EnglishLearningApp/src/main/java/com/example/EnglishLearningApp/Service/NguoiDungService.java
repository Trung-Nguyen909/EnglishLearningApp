package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.Repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    private final NguoiDungRepository nguoiDungRepository ;

    public List<NguoiDung> getAllNguoiDung(){
        return nguoiDungRepository.findAll();
    }

    public Optional<NguoiDung> findNguoiDungById(Integer id){
        return nguoiDungRepository.findById(id);
    }

    public NguoiDung createUser(NguoiDung nguoiDung){
        return nguoiDungRepository.save(nguoiDung);
    }

    public NguoiDung capNhatNguoiDung(Integer id,NguoiDung nguoiDungChiTiet){
        return nguoiDungRepository.findById(id).map(nguoiDung -> {
            nguoiDung.setTenDangNhap(nguoiDungChiTiet.getTenDangNhap());
            nguoiDung.setMatKhau(nguoiDungChiTiet.getMatKhau());
            nguoiDung.setHoTen(nguoiDungChiTiet.getHoTen());
            nguoiDung.setEmail(nguoiDungChiTiet.getEmail());
            nguoiDung.setAnhDaiDien(nguoiDungChiTiet.getAnhDaiDien());
            nguoiDung.setLastLogin(nguoiDungChiTiet.getLastLogin());
            nguoiDung.setStreak(nguoiDungChiTiet.getStreak());
            return nguoiDungRepository.save(nguoiDung);
        }).orElseThrow();
    }

    public void xoaNguoiDung(Integer id){
        nguoiDungRepository.deleteById(id);
    }
}
