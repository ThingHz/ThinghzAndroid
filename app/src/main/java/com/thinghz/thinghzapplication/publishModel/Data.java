package com.thinghz.thinghzapplication.publishModel;

import com.google.gson.annotations.SerializedName;
import com.thinghz.thinghzapplication.addDeviceModel.Item;

public class Data {
    @SerializedName("topic")
    private String topic;

    @SerializedName("qos")
    private int qos;


    public String getTopic() {
        return topic;
    }

    public int getQos() {
        return qos;
    }
}
