package com.thinghz.thinghzapplication.publishModel;

import com.google.gson.annotations.SerializedName;
import com.thinghz.thinghzapplication.publishModel.Data;

public class PublishResponseModel {
    @SerializedName("data")
    private  Data data;

    @SerializedName("Success")
    private boolean success;

    @SerializedName("error")
    private String error;


    public Data getData(){
        return data;
    }

    public String getError() { return error; }

    public boolean isSuccess(){
        return success;
    }
}
