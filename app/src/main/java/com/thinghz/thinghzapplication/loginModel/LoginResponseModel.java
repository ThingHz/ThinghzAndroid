package com.thinghz.thinghzapplication.loginModel;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {


	@SerializedName("Success")
	private boolean success;

	@SerializedName("token")
	private String token;

	@SerializedName("error")
	private String error;


	public boolean isSuccess(){
		return success;
	}

	public String getToken(){ return token; }

	public String getError() { return error; }

}