package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.deviceDataModel.DeviceDataRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDeviceDataForGraph {
    @GET("v1/data/n/{id}")
    Call<DeviceDataRoot> getResponse(@Path("id") String deviceId,@Header("Authorization") String auth,@Query("limit") Integer limit);
}
