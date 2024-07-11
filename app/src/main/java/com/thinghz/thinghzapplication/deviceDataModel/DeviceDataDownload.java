package com.thinghz.thinghzapplication.deviceDataModel;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@DynamoDBTable(tableName = "thinghz-backend-aws-Data-3")
public class DeviceDataDownload {
    private String deviceId;
    private Long timestamp;
    private String battery;
    private String gas;
    private String humid;
    private int lightState1;
    private int lightState2;
    private int lightState3;
    private int lightState4;
    private String lux;
    private int sensorProfile;
    private String temp;
    private String deviceName;

    @DynamoDBHashKey(attributeName = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public void setHumid(String humid) {
        this.humid = humid;
    }

    public void setLightState1(int lightState1) {
        this.lightState1 = lightState1;
    }

    public void setLightState2(int lightState2) {
        this.lightState2 = lightState2;
    }

    public void setLightState3(int lightState3) {
        this.lightState3 = lightState3;
    }

    public void setLightState4(int lightState4) {
        this.lightState4 = lightState4;
    }

    public void setLux(String lux) {
        this.lux = lux;
    }

    public void setSensorProfile(int sensorProfile) {
        this.sensorProfile = sensorProfile;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @DynamoDBRangeKey(attributeName = "timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public String getBattery() {
        return battery;
    }

    public String getHumid() {
        return humid;
    }

    public int getLightState1() {
        return lightState1;
    }

    public int getLightState2() {
        return lightState2;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getLightState3() {
        return lightState3;
    }

    public int getLightState4() {
        return lightState4;
    }

    public String getLux() {
        return lux;
    }

    public int getSensorProfile() {
        return sensorProfile;
    }

    public String getTemp() {
        return temp;
    }

    public String getTime(Long timestamp){
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String time = sdf.format(new Date(timestamp*1000L));
        return time;
    }
    // Add similar getter and setter methods for the other attributes
}