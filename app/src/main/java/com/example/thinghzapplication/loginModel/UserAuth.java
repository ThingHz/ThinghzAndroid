package com.example.thinghzapplication.loginModel;


public class UserAuth {
    private static String authToken;
    private static boolean isFirstUser;

    public UserAuth(String authToken,boolean isFirstUser) {
        this.authToken = authToken;
        this. isFirstUser = isFirstUser;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static boolean getIsFirstUser() {
        return isFirstUser;
    }

    public static void setAuthToken(String authToken) {
        UserAuth.authToken = authToken;
    }

    public static void setIsFirstUser(boolean isFirstUser) {
        UserAuth.isFirstUser = isFirstUser;
    }
}
