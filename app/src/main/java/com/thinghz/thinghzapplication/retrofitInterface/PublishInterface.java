package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceRequestModel;
import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceResponseModel;
import com.thinghz.thinghzapplication.publishModel.PublishRequestModel;
import com.thinghz.thinghzapplication.publishModel.PublishResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PublishInterface {
    @POST("v1/publish")
    Call<PublishResponseModel> publishData(@Body PublishRequestModel publish, @Header("Authorization") String auth);
}
