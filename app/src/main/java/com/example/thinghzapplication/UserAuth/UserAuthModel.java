package com.example.thinghzapplication.UserAuth;

import com.google.gson.annotations.SerializedName;

public class UserAuthModel {
    private String userName;

    private String email_id;

    private String location;

    private long issuedAt;

    private long expireAt;

    public UserAuthModel(String userName, String email_id, String location, long issuedAt, long expireAt) {
        this.userName = userName;
        this.email_id = email_id;
        this.location = location;
        this.issuedAt = issuedAt;
        this.expireAt = expireAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getLocation() {
        return location;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public long getExpireAt() {
        return expireAt;
    }
}
