package com.example.englishlearningapp.Retrofit;
import com.example.englishlearningapp.DTO.Request.UserLoginRequest;
import com.example.englishlearningapp.DTO.Request.UserRegisterRequest;
import com.example.englishlearningapp.DTO.Response.AuthResponse;
import com.example.englishlearningapp.DTO.Response.NguoiDungRespone;
import com.example.englishlearningapp.DTO.Response.NhatKyHoatDong;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("user/login")
    Call<AuthResponse> login(@Body UserLoginRequest user);

    @POST("user/register")
    Call<NguoiDungRespone> register(@Body UserRegisterRequest user);

    @GET("nhatkyhoatdong/my-activity-log")
    Call<List<NhatKyHoatDong>> getUserActivityLog(@Header("Authorization") String token);
}
