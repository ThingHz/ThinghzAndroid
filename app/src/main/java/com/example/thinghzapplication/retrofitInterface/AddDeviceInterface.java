package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.addDeviceModel.AddDeviceRequestModel;
import com.example.thinghzapplication.addDeviceModel.AddDeviceResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddDeviceInterface {
    @POST("v1/device")
    Call<AddDeviceResponseModel> addDevice(@Body AddDeviceRequestModel addDevice, @Header("Authorization") String auth);
}
