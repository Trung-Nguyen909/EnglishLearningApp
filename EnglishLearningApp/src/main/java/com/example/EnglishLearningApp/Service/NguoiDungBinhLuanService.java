package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Exception.AppException;
import com.example.EnglishLearningApp.Exception.ErrorCode;
import com.example.EnglishLearningApp.Model.NguoiDung_BinhLuan;
import com.example.EnglishLearningApp.Repository.NguoiDungBinhLuanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NguoiDungBinhLuanService {
    private final NguoiDungBinhLuanRepository nguoiDungBinhLuanRepository;

    public List<NguoiDung_BinhLuan> getAllNguoiDngbl()
    {
        return nguoiDungBinhLuanRepository.findAll();
    }
    public NguoiDung_BinhLuan getNguoiDung(int id) {
        return nguoiDungBinhLuanRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.Not_Found));
    }
    public NguoiDung_BinhLuan tao1NguoiDung(NguoiDung_BinhLuan nguoiDung_binhLuan)
    {
        return nguoiDungBinhLuanRepository.save(nguoiDung_binhLuan);
    }
    public NguoiDung_BinhLuan updatenguoiDUng(NguoiDung_BinhLuan nguoiDungBinhLuan, int id)
    {
        NguoiDung_BinhLuan nguoiDungBinhLuan1 = getNguoiDung(id);
        nguoiDungBinhLuan1.setType(nguoiDungBinhLuan.getType());
        nguoiDungBinhLuan1.setNgayTao(nguoiDungBinhLuan.getNgayTao());
        return nguoiDungBinhLuanRepository.save(nguoiDungBinhLuan1);
    }
    public String DeleteNguoiDung(int id)
    {
        if (getNguoiDung(id) != null) {
            nguoiDungBinhLuanRepository.deleteById(id);
            return "Xóa thành công";
        }
        return "Xóa không thành công";
    }

}
