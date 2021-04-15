package com.example.thinghzapplication.deviceModel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("sensor_profile")
	private int sensorProfile;

	@SerializedName("device_status")
	private String deviceStatus;

	@SerializedName("temp")
	private String temp;

	@SerializedName("device_name")
	private String deviceName;

	@SerializedName("cap")
	private String cap;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("escalation")
	private int escalation;

	@SerializedName("humid")
	private String humid;

	@SerializedName("userName")
	private String userName;

	@SerializedName("battery")
	private String battery;

	@SerializedName("timestamp")
	private int timestamp;

	public int getSensorProfile(){
		return sensorProfile;
	}

	public String getDeviceStatus(){
		return deviceStatus;
	}

	public String getTemp(){
		return temp;
	}

	public String getDeviceName(){
		return deviceName;
	}

	public String getCap(){
		return cap;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public int getEscalation(){
		return escalation;
	}

	public String getHumid(){
		return humid;
	}

	public String getUserName(){
		return userName;
	}

	public String getBattery(){
		return battery;
	}

	public int getTimestamp(){
		return timestamp;
	}
}