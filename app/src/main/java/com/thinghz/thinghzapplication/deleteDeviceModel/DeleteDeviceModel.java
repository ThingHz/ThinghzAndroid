package com.thinghz.thinghzapplication.deleteDeviceModel;

import com.google.gson.annotations.SerializedName;

public class DeleteDeviceModel{

	@SerializedName("message")
	private String message;

	@SerializedName("Success")
	private boolean success;

	@SerializedName("error")
	private String error;

	public String getMessage(){
		return message;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getError() {
		return error;
	}
}