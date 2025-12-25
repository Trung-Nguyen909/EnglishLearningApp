package com.example.englishlearningapp.DTO.Response;

import com.example.englishlearningapp.DTO.UserDetail;

public class AuthResponse {
    private String token;
    private UserDetail user;

    public String getToken() {
        return token;
    }

    public UserDetail getUser() {
        return user;
    }
}