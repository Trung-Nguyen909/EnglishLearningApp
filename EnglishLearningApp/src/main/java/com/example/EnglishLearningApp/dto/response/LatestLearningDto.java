package com.example.EnglishLearningApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestLearningDto {
    private String tenKhoaHoc;
    private String tenBaiHoc;
    private Integer idBaiHoc;
    private Double phanTram; // % hoàn thành khóa học
}