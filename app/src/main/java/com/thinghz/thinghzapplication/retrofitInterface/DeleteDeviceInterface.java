package com.thinghz.thinghzapplication.retrofitInterface;

import com.thinghz.thinghzapplication.deleteDeviceModel.DeleteDeviceModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeleteDeviceInterface {
    @DELETE("v1/device/{device_id}")
    Call<DeleteDeviceModel> getResponse(@Header("Authorization") String auth,
                                        @Path("device_id") String deviceId,
                                        @Query("escalation") Integer escalation);
}
