package com.thinghz.thinghzapplication.deviceDataModel;

import com.google.gson.annotations.SerializedName;

public class DeviceDataRoot{

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