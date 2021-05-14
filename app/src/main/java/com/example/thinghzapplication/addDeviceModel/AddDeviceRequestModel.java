package com.example.thinghzapplication.addDeviceModel;

import com.google.gson.annotations.SerializedName;

public class AddDeviceRequestModel{

	@SerializedName("sensor_profile")
	private int sensorProfile;

	@SerializedName("device_name")
	private String deviceName;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("escalation")
	private int escalation;

	@SerializedName("maxTemp")
	private String maxTemp;

	@SerializedName("minTemp")
	private String minTemp;

	public int getSensorProfile(){
		return sensorProfile;
	}

	public String getMinTemp() { return minTemp; }

	public AddDeviceRequestModel(int sensorProfile, String deviceName, String deviceId, int escalation, String maxTemp, String minTemp) {
		this.sensorProfile = sensorProfile;
		this.deviceName = deviceName;
		this.deviceId = deviceId;
		this.escalation = escalation;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
	}

	public String getDeviceName(){
		return deviceName;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public int getEscalation(){
		return escalation;
	}

	public String getMaxTemp(){
		return maxTemp;
	}

}