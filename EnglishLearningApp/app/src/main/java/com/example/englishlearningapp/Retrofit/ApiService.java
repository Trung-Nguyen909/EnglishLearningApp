package com.example.englishlearningapp.Retrofit;
import com.example.englishlearningapp.DTO.Request.UserLoginRequest;
import com.example.englishlearningapp.DTO.Request.UserRegisterRequest;
import com.example.englishlearningapp.DTO.Response.AuthResponse;
import com.example.englishlearningapp.DTO.Response.NguoiDungRespone;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/login")
    Call<AuthResponse> login(@Body UserLoginRequest user);

    @POST("user/register")
    Call<NguoiDungRespone> register(@Body UserRegisterRequest user);
}
