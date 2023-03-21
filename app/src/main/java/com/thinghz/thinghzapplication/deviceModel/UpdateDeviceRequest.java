package com.thinghz.thinghzapplication.deviceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDeviceRequest {
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @SerializedName("sensor_profile")
    @Expose
    private Integer sensorProfile;
    @SerializedName("range")
    @Expose
    private Range range;
    @SerializedName("location")
    @Expose
    private String location;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getSensorProfile() {
        return sensorProfile;
    }

    public void setSensorProfile(Integer sensorProfile) {
        this.sensorProfile = sensorProfile;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static class Range {

        @SerializedName("maxTemp")
        @Expose
        private String maxTemp;
        @SerializedName("minTemp")
        @Expose
        private String minTemp;
        @SerializedName("maxHumid")
        @Expose
        private String maxHumid;
        @SerializedName("minHumid")
        @Expose
        private String minHumid;
        @SerializedName("maxGas")
        @Expose
        private String maxGas;
        @SerializedName("minGas")
        @Expose
        private String minGas;

        public String getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
        }

        public String getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(String minTemp) {
            this.minTemp = minTemp;
        }

        public String getMaxHumid() {
            return maxHumid;
        }

        public void setMaxHumid(String maxHumid) {
            this.maxHumid = maxHumid;
        }

        public String getMinHumid() {
            return minHumid;
        }

        public void setMinHumid(String minHumid) {
            this.minHumid = minHumid;
        }

        public String getMaxGas() {
            return maxGas;
        }

        public void setMaxGas(String maxGas) {
            this.maxGas = maxGas;
        }

        public String getMinGas() {
            return minGas;
        }

        public void setMinGas(String minGas) {
            this.minGas = minGas;
        }
    }
}
