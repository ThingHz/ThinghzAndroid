package com.example.thinghzapplication.retrofitInterface;

import com.example.thinghzapplication.deleteDeviceModel.DeleteDeviceModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeleteDeviceInterface {
    @DELETE("v1/devices/{device_id}")
    Call<DeleteDeviceModel> getResponse(@Header("Authorization") String auth,
                                        @Query("escalation") Integer escalation,
                                        @Path("device_id") String deviceId);
}
