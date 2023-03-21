package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetdeviceData {
    @GET("v1/devices")
    Call<GetDeviceResponseModel> getResponse(@Header("Authorization") String auth,
                                             @Query("status") String status,
                                             @Query("escalation") Integer escalation,
                                             @Query("profile") Integer profile);
    Call<DataItem> getDataItem(@Header("Authorization") String auth);

}
