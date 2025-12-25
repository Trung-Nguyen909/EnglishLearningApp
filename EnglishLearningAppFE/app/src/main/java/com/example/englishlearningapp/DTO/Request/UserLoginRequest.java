package com.example.englishlearningapp.DTO.Request;

public class UserLoginRequest {
    private String email;
    private String matkhau;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.matkhau = password;
    }

}
