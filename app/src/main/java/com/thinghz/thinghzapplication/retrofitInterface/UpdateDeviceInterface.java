package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceMetaRequest;
import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceRequest;
import com.thinghz.thinghzapplication.loginModel.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UpdateDeviceInterface {

    @PUT("v1/device/{device_id}")
    Call<LoginResponseModel> updateDevice(@Header("Authorization") String auth,
                                          @Path("device_id")String device_id,
                                          @Query("escalation") Integer escalation,
                                          @Body UpdateDeviceRequest updateDeviceRequest);
    @PUT("v1/device-meta/{device_id}")
    Call<LoginResponseModel> updateDeviceMeta(@Header("Authorization") String auth,
                                          @Path("device_id")String device_id,
                                          @Body UpdateDeviceMetaRequest updateDeviceMetaRequest);

}
