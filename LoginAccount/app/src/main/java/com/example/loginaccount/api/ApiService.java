package com.example.loginaccount.api;

import com.example.loginaccount.model.ApiResponse;
import com.example.loginaccount.model.User;
import com.example.loginaccount.model.UserLoginRequest;
import com.example.loginaccount.model.UserResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/spring-boot/api/auth/register")
    Call<ApiResponse> register(@Body User user);

    @POST("/spring-boot/api/auth/login")
    Call<ApiResponse> login(@Body UserLoginRequest request);

    @POST("/spring-boot/api/auth/request")
    Call<ApiResponse> requestOtp(@Body UserResetPasswordRequest userResetPasswordRequest);

    @POST("/spring-boot/api/auth/verify")
    Call<ApiResponse> verifyOtpAndResetPassword(@Body UserResetPasswordRequest userResetPasswordRequest);

    @POST("/spring-boot/api/auth/reset")
    Call<ApiResponse> passwordReset(@Body UserResetPasswordRequest userResetPasswordRequest);
}
