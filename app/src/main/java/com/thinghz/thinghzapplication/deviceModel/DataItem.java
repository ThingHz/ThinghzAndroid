package com.thinghz.thinghzapplication.deviceModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

	@SerializedName("sensor_profile")
	private int sensorProfile;

	@SerializedName("device_status")
	private String deviceStatus;

	@SerializedName("temp")
	private String temp;

	@SerializedName("device_name")
	private String deviceName;

	@SerializedName("cap")
	private String cap;

	@SerializedName("gas")
	private String gas;

	@SerializedName("lux")
	private String lux;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("escalation")
	private int escalation;

	@SerializedName("humid")
	private String humid;

	@SerializedName("userName")
	private String userName;

	@SerializedName("battery")
	private String battery;

	@SerializedName("timestamp")
	private int timestamp;

	@SerializedName("light_state_1")
	private int light_state_1;

	@SerializedName("light_state_2")
	private int light_state_2;

	protected DataItem(Parcel in) {
		sensorProfile = in.readInt();
		deviceStatus = in.readString();
		temp = in.readString();
		deviceName = in.readString();
		cap = in.readString();
		gas = in.readString();
		lux = in.readString();
		deviceId = in.readString();
		escalation = in.readInt();
		humid = in.readString();
		userName = in.readString();
		battery = in.readString();
		timestamp = in.readInt();
		light_state_1 = in.readInt();
		light_state_2 = in.readInt();
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

	public int getSensorProfile(){
		return sensorProfile;
	}

	public String getDeviceStatus(){
		return deviceStatus;
	}

	public String getTemp(){
		return temp;
	}

	public String getDeviceName(){
		return deviceName;
	}

	public String getCap(){
		return cap;
	}

	public String getGas(){
		return gas;
	}

	public String getLux(){
		return lux;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public int getEscalation(){
		return escalation;
	}

	public String getHumid(){
		return humid;
	}

	public String getUserName(){
		return userName;
	}

	public String getBattery(){
		return battery;
	}

	public int getTimestamp(){
		return timestamp;
	}

	public int getLightState1() { return light_state_1; }

	public int getLightState2() { return light_state_2; }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(sensorProfile);
		parcel.writeString(deviceStatus);
		parcel.writeString(temp);
		parcel.writeString(deviceName);
		parcel.writeString(cap);
		parcel.writeString(gas);
		parcel.writeString(lux);
		parcel.writeString(deviceId);
		parcel.writeInt(escalation);
		parcel.writeString(humid);
		parcel.writeString(userName);
		parcel.writeString(battery);
		parcel.writeInt(timestamp);
		parcel.writeInt(light_state_1);
		parcel.writeInt(light_state_2);
	}
}