package com.example.thinghzapplication.loginModel;

public class LoginBodyModel {
    public String userName;
    public String password;

    public LoginBodyModel(String userName, String passKey) {
        this.userName = userName;
        this.password = passKey;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassKey() {
        return password;
    }
}
