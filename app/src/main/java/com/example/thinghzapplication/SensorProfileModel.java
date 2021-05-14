package com.example.thinghzapplication;

import java.io.Serializable;

public class SensorProfileModel implements Serializable {
    private String sensorProfile;

    public SensorProfileModel(String sensorProfile) {
        this.sensorProfile = sensorProfile;
    }

    public String getSensorProfile() {
        return sensorProfile;
    }

}
