package com.example.EnglishLearningApp.dto.request;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitBaiLamRequest {
    private Integer idTest;          // Null nếu là bài tập
    private Integer idBaiTap;        // Null nếu là test
    private String tenBai;           // Tên bài (Test hoặc BaiTap)
    private String loaiBai;          // 'TEST' hoặc 'BAITAP'
    private Integer tgianLam;        // Thời gian làm (seconds)
    private List<CauTraLoiRequest> cauTraLoi; // Danh sách câu trả lời

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CauTraLoiRequest {
        private Integer idCauHoi;
        private String userAns;      // Câu trả lời của user
        private String correctAns;   // Câu trả lời đúng (để so sánh)
    }
}

