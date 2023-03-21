package com.thinghz.thinghzapplication.loginModel;


public class UserAuth {
    private  String authToken;
    private  boolean isFirstUser;

    public UserAuth(String authToken,boolean isFirstUser) {
        this.authToken = authToken;
        this. isFirstUser = isFirstUser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public boolean getIsFirstUser() {
        return isFirstUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setIsFirstUser(boolean isFirstUser) {
        this.isFirstUser = isFirstUser;
    }
}
