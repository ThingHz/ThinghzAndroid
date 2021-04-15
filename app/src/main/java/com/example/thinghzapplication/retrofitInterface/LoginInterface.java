package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.loginModel.LoginBodyModel;
import com.example.thinghzapplication.loginModel.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginInterface {
    @POST("v1/login")
   Call<LoginResponseModel> createUser(@Body LoginBodyModel login);
}
