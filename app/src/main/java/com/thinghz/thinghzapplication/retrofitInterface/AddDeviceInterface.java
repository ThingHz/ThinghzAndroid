package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceRequestModel;
import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceResponseModel;
import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceRequest;
import com.thinghz.thinghzapplication.loginModel.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddDeviceInterface {
    @POST("v1/device")
    Call<AddDeviceResponseModel> addDevice(@Body AddDeviceRequestModel addDevice, @Header("Authorization") String auth);

}
