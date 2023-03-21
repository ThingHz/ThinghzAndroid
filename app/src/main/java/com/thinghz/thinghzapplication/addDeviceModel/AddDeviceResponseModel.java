package com.thinghz.thinghzapplication.addDeviceModel;

import com.google.gson.annotations.SerializedName;

public class AddDeviceResponseModel{

	@SerializedName("data")
	private Data data;

	@SerializedName("Success")
	private boolean success;

	@SerializedName("error")
	private String error;


	public Data getData(){
		return data;
	}

	public String getError() { return error; }

	public boolean isSuccess(){
		return success;
	}
}