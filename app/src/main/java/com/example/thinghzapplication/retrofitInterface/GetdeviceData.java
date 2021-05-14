package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.deviceModel.DataItem;
import com.example.thinghzapplication.deviceModel.GetDeviceResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetdeviceData {
    @GET("v1/devices")
    Call<GetDeviceResponseModel> getResponse(@Header("Authorization") String auth,
                                             @Query("status") String status,
                                             @Query("escalation") Integer escalation,
                                             @Query("profile") Integer profile);
    Call<DataItem> getDataItem(@Header("Authorization") String auth);

}
