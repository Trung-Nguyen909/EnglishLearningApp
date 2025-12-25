package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.ChiTietBaiLam;
import com.example.EnglishLearningApp.Repository.ChiTietLichSuLamBaiRepository;
import com.example.EnglishLearningApp.dto.request.SubmitBaiLamRequest;
import com.example.EnglishLearningApp.dto.response.ChiTietBaiLamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietBaiLamService {
    private final ChiTietLichSuLamBaiRepository chiTietRepo;

    public List<ChiTietBaiLamDTO> getChiTietBaiLam(Integer lichSuId) {
        return chiTietRepo.findChiTietByLichSuId(lichSuId);
    }

    public void saveChiTietBaiLam(Integer idLichSuBaiLam, List<SubmitBaiLamRequest.CauTraLoiRequest> cauTraLoiList) {
        for (SubmitBaiLamRequest.CauTraLoiRequest cauTraLoi : cauTraLoiList) {
            boolean isCorrect = cauTraLoi.getUserAns() != null &&
                              cauTraLoi.getUserAns().equalsIgnoreCase(cauTraLoi.getCorrectAns());

            ChiTietBaiLam chiTiet = ChiTietBaiLam.builder()
                    .idLichSuBaiLam(idLichSuBaiLam)
                    .idCauHoi(cauTraLoi.getIdCauHoi())
                    .userAns(cauTraLoi.getUserAns())
                    .isCorrect(isCorrect)
                    .build();

            chiTietRepo.save(chiTiet);
        }
    }
}
