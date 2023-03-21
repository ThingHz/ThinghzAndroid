package com.thinghz.thinghzapplication.deviceDataModel;

import com.google.gson.annotations.SerializedName;

public class LastEvaluatedKey{

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("timestamp")
	private int timestamp;

	public String getDeviceId(){
		return deviceId;
	}

	public int getTimestamp(){
		return timestamp;
	}
}