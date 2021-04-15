package com.example.thinghzapplication.deviceModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GetDeviceResponseModel {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("Success")
	private boolean success;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private boolean message;

	public boolean isError() { return error; }

	public boolean isMessage() { return message; }

	public List<DataItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}