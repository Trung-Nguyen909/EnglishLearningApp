package com.example.EnglishLearningApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private int soThanhTuu;
    private int soBaiHoc;
    private List<SkillDto> kyNang;
}
