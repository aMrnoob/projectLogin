package com.example.loginaccount.api;

import com.example.loginaccount.model.ApiResponse;
import com.example.loginaccount.model.User;
import com.example.loginaccount.model.UserLoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/spring-boot/api/auth/register")
    Call<ApiResponse> register(@Body User user);

    @POST("/spring-boot/api/auth/login")
    Call<ApiResponse> login(@Body UserLoginRequest request);
}
