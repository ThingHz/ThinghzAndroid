package com.thinghz.thinghzapplication.deviceDataModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("Items")
	private List<ItemsItem> items;

	@SerializedName("Count")
	private int count;

	@SerializedName("ScannedCount")
	private int scannedCount;

	@SerializedName("LastEvaluatedKey")
	private LastEvaluatedKey lastEvaluatedKey;

	public List<ItemsItem> getItems(){
		return items;
	}

	public int getCount(){
		return count;
	}

	public int getScannedCount(){
		return scannedCount;
	}

	public LastEvaluatedKey getLastEvaluatedKey(){
		return lastEvaluatedKey;
	}
}