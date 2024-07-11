package com.thinghz.thinghzapplication.deviceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDeviceMetaRequest {
    @SerializedName("sensor_at")
    @Expose
    private Integer sensorAt;
    @SerializedName("lux_1")
    @Expose
    private String lux1;
    @SerializedName("lux_2")
    @Expose
    private String lux2;
    @SerializedName("lux_3")
    @Expose
    private String lux3;
    @SerializedName("lux_4")
    @Expose
    private String lux4;
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

    public Integer getSensorAt() {
        return sensorAt;
    }

    public void setSensorAt(Integer sensorAt) {
        this.sensorAt = sensorAt;
    }

    public String getLux1() {
        return lux1;
    }

    public void setLux1(String lux1) {
        this.lux1 = lux1;
    }

    public String getLux2() {
        return lux2;
    }

    public void setLux2(String lux2) {
        this.lux2 = lux2;
    }

    public String getLux3() {
        return lux3;
    }

    public void setLux3(String lux3) {
        this.lux3 = lux3;
    }

    public String getLux4() {
        return lux4;
    }

    public void setLux4(String lux4) {
        this.lux4 = lux4;
    }

    public String getEx1() {
        return ex1;
    }

    public void setEx1(String ex1) {
        this.ex1 = ex1;
    }

    public String getEx2() {
        return ex2;
    }

    public void setEx2(String ex2) {
        this.ex2 = ex2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }

    public String getEx4() {
        return ex4;
    }

    public void setEx4(String ex4) {
        this.ex4 = ex4;
    }

    public String getObj1() {
        return obj1;
    }

    public void setObj1(String obj1) {
        this.obj1 = obj1;
    }

    public String getObj2() {
        return obj2;
    }

    public void setObj2(String obj2) {
        this.obj2 = obj2;
    }

    public String getObj3() {
        return obj3;
    }

    public void setObj3(String obj3) {
        this.obj3 = obj3;
    }

    public String getObj4() {
        return obj4;
    }

    public void setObj4(String obj4) {
        this.obj4 = obj4;
    }
}
