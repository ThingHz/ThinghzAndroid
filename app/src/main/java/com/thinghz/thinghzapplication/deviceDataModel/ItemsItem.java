package com.thinghz.thinghzapplication.deviceDataModel;

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

	@SerializedName("gas")
	private String gas;

	@SerializedName("battery")
	private String battery;

	@SerializedName("timestamp")
	private int timestamp;


	@SerializedName("lux")
	private String lux;


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

	public String getGas(){
		return gas;
	}

	public String getBattery(){
		return battery;
	}

	public int getTimestamp(){
		return timestamp;
	}

	public String getLux() {
		return lux;
	}
}