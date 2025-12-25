package com.example.englishlearningapp.DTO.Response;

import java.util.List;

public class UserSummaryDto {
    private int soThanhTuu;
    private int soBaiHoc;
    private List<SkillDto> kyNang;
    public int getSoThanhTuu() { return soThanhTuu; }
    public int getSoBaiHoc() { return soBaiHoc; }
    public List<SkillDto> getKyNang() { return kyNang; }
}
