package com.example.englishlearningapp.Retrofit;
import com.example.englishlearningapp.DTO.Request.UserLoginRequest;
import com.example.englishlearningapp.DTO.Request.UserRegisterRequest;
import com.example.englishlearningapp.DTO.Response.AuthResponse;
import com.example.englishlearningapp.DTO.Response.BaiHocResponse;
import com.example.englishlearningapp.DTO.Response.BaiTap;
import com.example.englishlearningapp.DTO.Response.BaiTapResponse;
import com.example.englishlearningapp.DTO.Response.CauHoiResponse;
import com.example.englishlearningapp.DTO.Response.KhoaHocResponse;
import com.example.englishlearningapp.DTO.Response.NguoiDungRespone;
import com.example.englishlearningapp.DTO.Response.NhatKyHoatDong;
import com.example.englishlearningapp.DTO.Response.TuVungResponse;
import com.example.englishlearningapp.DTO.Response.UserSummaryDto;
import com.example.englishlearningapp.DTO.Response.KyNangResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("user/login")
    @Headers("No-Authentication: true")
    Call<AuthResponse> login(@Body UserLoginRequest user);

    @POST("user/register")
    @Headers("No-Authentication: true")
    Call<NguoiDungRespone> register(@Body UserRegisterRequest user);

    @GET("nhatkyhoatdong/my-activity-log")
    Call<List<NhatKyHoatDong>> getUserActivityLog();

    @POST("/nhatkyhoatdong")
    Call<ApiResponse<Object>> createTodayActivityLog();

    @GET("user/summary")
    Call<UserSummaryDto> GetInfoUser();

    @GET("kynang")
    Call<List<KyNangResponse>> getAllKyNang();

    @GET("baitap/{loaiBT}")
    Call<List<BaiTapResponse>> getBaiTapByLoai(@Path("loaiBT") String loaiBaiTap);

    @GET("cauhoi/baitap/{id}")
    Call<List<CauHoiResponse>> getCauHoiByBaiTapId(@Path("id") int id);

    @GET("KhoaHoc")
    @Headers("No-Authentication: true")
    Call<List<KhoaHocResponse>> getAllKhoaHoc();

    @GET("baihoc/khoahoc/{idKhoaHoc}")
    @Headers("No-Authentication: true")
    Call<List<BaiHocResponse>> getBaiHocByKhoaHoc(@Path("idKhoaHoc")int id);

    @GET("baitap/baihoc/{idbaihoc}")
    @Headers("No-Authentication: true")
    Call<List<BaiTap>> getBaiTapByBaiHocId(@Path("idbaihoc")int id);

    @GET("tuvung/baihoc/{idbaihoc}")
    @Headers("No-Authentication: true")
    Call<List<TuVungResponse>> getTuVungByBaihocID(@Path("idbaihoc") int id);
}
