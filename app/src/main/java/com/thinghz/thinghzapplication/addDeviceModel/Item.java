package com.thinghz.thinghzapplication.addDeviceModel;

import com.google.gson.annotations.SerializedName;

public class Item{

	@SerializedName("maxCap")
	private String maxCap;

	@SerializedName("device_status")
	private String deviceStatus;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("maxTemp")
	private String maxTemp;

	@SerializedName("userName")
	private String userName;

	@SerializedName("minTemp")
	private String minTemp;

	@SerializedName("sensor_profile")
	private int sensorProfile;

	@SerializedName("device_name")
	private String deviceName;

	@SerializedName("escalation")
	private int escalation;

	@SerializedName("maxHumid")
	private String maxHumid;

	@SerializedName("minHumid")
	private String minHumid;

	@SerializedName("minCap")
	private String minCap;

	@SerializedName("timestamp")
	private int timestamp;

	public String getMaxCap(){
		return maxCap;
	}

	public String getDeviceStatus(){
		return deviceStatus;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public String getMaxTemp(){
		return maxTemp;
	}

	public String getUserName(){
		return userName;
	}

	public String getMinTemp(){
		return minTemp;
	}

	public int getSensorProfile(){
		return sensorProfile;
	}

	public String getDeviceName(){
		return deviceName;
	}

	public int getEscalation(){
		return escalation;
	}

	public String getMaxHumid(){
		return maxHumid;
	}

	public String getMinHumid(){
		return minHumid;
	}

	public String getMinCap(){
		return minCap;
	}

	public int getTimestamp(){
		return timestamp;
	}
}