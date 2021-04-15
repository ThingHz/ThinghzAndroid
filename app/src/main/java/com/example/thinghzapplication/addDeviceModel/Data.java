package com.example.thinghzapplication.addDeviceModel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("sensor_profile")
	private String sensorProfile;

	@SerializedName("item")
	private Item item;

	public String getSensorProfile(){
		return sensorProfile;
	}

	public Item getItem(){
		return item;
	}
}