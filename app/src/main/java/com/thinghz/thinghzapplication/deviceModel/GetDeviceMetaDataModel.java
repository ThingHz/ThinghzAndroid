package com.thinghz.thinghzapplication.deviceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDeviceMetaDataModel {
    @SerializedName("data")
    private List<MetaData> data;

    @SerializedName("Success")
    private boolean success;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private boolean message;

    public boolean isError() { return error; }

    public boolean isMessage() { return message; }

    public List<MetaData> getData(){
        return data;
    }

    public boolean isSuccess(){
        return success;
    }

    public class MetaData {
        @SerializedName("ex_1")
        @Expose
        private String ex1;
        @SerializedName("ex_2")
        @Expose
        private String ex2;
        @SerializedName("ex_3")
        @Expose
        private String ex3;
        @SerializedName("ex_4")
        @Expose
        private String ex4;
        @SerializedName("obj_1")
        @Expose
        private String obj1;
        @SerializedName("obj_2")
        @Expose
        private String obj2;
        @SerializedName("obj_3")
        @Expose
        private String obj3;
        @SerializedName("obj_4")
        @Expose
        private String obj4;
        @SerializedName("userName")
        @Expose
        private String user_name;

        public String getEx1() {
            return ex1;
        }

        public String getEx2() {
            return ex2;
        }

        public String getEx3() {
            return ex3;
        }

        public String getEx4() {
            return ex4;
        }

        public String getObj1() {
            return obj1;
        }

        public String getObj2() {
            return obj2;
        }

        public String getObj3() {
            return obj3;
        }

        public String getObj4() {
            return obj4;
        }

        @SerializedName("device_id")
        @Expose
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }
    }
}
