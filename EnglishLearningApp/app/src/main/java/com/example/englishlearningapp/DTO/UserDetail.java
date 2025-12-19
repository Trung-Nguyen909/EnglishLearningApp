package com.example.englishlearningapp.DTO;

import java.time.LocalDateTime;

public class UserDetail {
    private String tenDangNhap;
    private String email;
    private String anhDaiDien;
    private int id;
    private int streak;
    private String lastLogin;
    private int tongThoiGianHoatDong;
    public String getTenDangNhap() { return tenDangNhap; }
    public String getEmail() { return email; }
    public int getId() { return id; }
    public String getAnhDaiDien() {return anhDaiDien;}
    public int getStreak() {return streak;}
    public String getLastLogin() {return lastLogin;}
    public  int getTongThoiGianHoatDong() {return tongThoiGianHoatDong;}

}