package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.loginModel.LoginBodyModel;
import com.thinghz.thinghzapplication.loginModel.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginInterface {
    @POST("v1/login")
   Call<LoginResponseModel> createUser(@Body LoginBodyModel login);
}
