package com.example.thinghzapplication.deviceDataModel;

import com.google.gson.annotations.SerializedName;

public class ItemsItem{

	@SerializedName("sensor_profile")
	private int sensorProfile;

	@SerializedName("temp")
	private String temp;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("humid")
	private String humid;

	@SerializedName("battery")
	private String battery;

	@SerializedName("timestamp")
	private int timestamp;

	public int getSensorProfile(){
		return sensorProfile;
	}

	public String getTemp(){
		return temp;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public String getHumid(){
		return humid;
	}

	public String getBattery(){
		return battery;
	}

	public int getTimestamp(){
		return timestamp;
	}
}