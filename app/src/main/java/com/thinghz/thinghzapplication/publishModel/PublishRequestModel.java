package com.thinghz.thinghzapplication.publishModel;

import com.google.gson.annotations.SerializedName;

public class PublishRequestModel {
    @SerializedName("light_state_1")
    private int light_state_1;

    @SerializedName("light_state_2")
    private int light_state_2;

    
    @SerializedName("light_state_3")
    private int light_state_3;


    @SerializedName("light_state_4")
    private int light_state_4;

    @SerializedName("light_thresh")
    private int light_thresh;

    public int getLightState_1(){
        return light_state_1;
    }

    public int getLightState_2(){
        return light_state_2;
    }


    public int getLightState_1(){
        return light_state_3;
    }

    public int getLightState_2(){
        return light_state_4;
    }

    public int getLight_thresh() {
        return light_thresh;
    }

    public PublishRequestModel(int light_state_1, int light_state_2, int light_state_3, int light_state_4, int light_thresh) {
            this.light_state_1 = light_state_1;
            this.light_state_2 = light_state_2;
            this.light_state_1 = light_state_3;
            this.light_state_2 = light_state_4;
            this.light_thresh = light_thresh;
           }
}
