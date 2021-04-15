package com.example.thinghzapplication.addDeviceModel;

import com.google.gson.annotations.SerializedName;

public class AddDeviceResponseModel{

	@SerializedName("data")
	private Data data;

	@SerializedName("Success")
	private boolean success;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}