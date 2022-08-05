package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.addDeviceModel.AddDeviceRequestModel;
import com.example.thinghzapplication.addDeviceModel.AddDeviceResponseModel;
import com.example.thinghzapplication.deviceModel.UpdateDeviceRequest;
import com.example.thinghzapplication.loginModel.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddDeviceInterface {
    @POST("v1/device")
    Call<AddDeviceResponseModel> addDevice(@Body AddDeviceRequestModel addDevice, @Header("Authorization") String auth);

    @PUT("v1/device/{device_id}")
    Call<LoginResponseModel> updateDevice(@Header("Authorization") String auth,
                                          @Path("device_id")String device_id,
                                          @Body UpdateDeviceRequest updateDeviceRequest);

}
