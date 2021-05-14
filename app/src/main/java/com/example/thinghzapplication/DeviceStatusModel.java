package com.example.thinghzapplication;

import java.io.Serializable;

public class DeviceStatusModel implements Serializable {
    private String deviceStatus;

    public DeviceStatusModel(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

}
