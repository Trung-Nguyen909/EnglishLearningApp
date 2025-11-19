package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Mapper.CauhoiMapper;
import com.example.EnglishLearningApp.Model.CauHoi;
import com.example.EnglishLearningApp.Repository.CauHoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CauHoiService {

    @Autowired
    private CauHoiRepository cauHoiRepository;
    private CauhoiMapper cauhoiMapper;

    public List<CauHoi> getAllCauHoi() {
        return cauHoiRepository.findAll();
    }

    public Optional<CauHoi> getCauHoiById(Integer id) {
        return cauHoiRepository.findById(id);
    }

    public CauHoi createCauHoi(CauHoi cauHoi) {
        return cauHoiRepository.save(cauHoi);
    }

    public CauHoi updateCauHoi(Integer id, CauHoi cauHoi) {
        return cauHoiRepository.findById(id).map(ch -> {
            cauhoiMapper.toCauHoi(cauHoi);
            return cauHoiRepository.save(ch);
        }).orElseThrow(() -> new RuntimeException("Câu hỏi không tồn tại"));
    }

    public void deleteCauHoi(Integer id) {
        cauHoiRepository.deleteById(id);
    }
}
