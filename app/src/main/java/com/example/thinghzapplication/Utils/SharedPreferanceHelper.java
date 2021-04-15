package com.example.thinghzapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Debug;
import android.util.Log;

import com.example.thinghzapplication.loginModel.UserAuth;

public class SharedPreferanceHelper {
    private static SharedPreferanceHelper instance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static String MY_PREFERENCES = "preferances";
    private static String SHARE_KEY_ISFIRST = "isFirstUser";
    private static String SHARE_KEY_TOKEN = "authToken";
    private static String authToken;
    private static boolean isFirstUser;


    public SharedPreferanceHelper() {

    }

    public static SharedPreferanceHelper getInstance(Context context) {

        if (instance == null){
            instance = new SharedPreferanceHelper();
            sharedPreferences = context.getSharedPreferences(MY_PREFERENCES,context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return instance;
    }

    public void saveUserInfo(UserAuth userAuth){
        editor.putBoolean(SHARE_KEY_ISFIRST, userAuth.getIsFirstUser());
        editor.putString(SHARE_KEY_TOKEN, userAuth.getAuthToken());
        if(!editor.commit()){
            Log.i("Shared Preferance","Error while saving it to shared preferance");
        }
    }

    public UserAuth getUserSavedValue(){
        UserAuth userAuth = null;
        try{
            String authToken = sharedPreferences.getString(SHARE_KEY_TOKEN,"EmptyToken");
            boolean isFirstUser = sharedPreferences.getBoolean(SHARE_KEY_ISFIRST,true);
            userAuth = new UserAuth(authToken,isFirstUser);

        }catch(NullPointerException e) {
            Log.i("Shared","Exception"+e);
        }
        //param1: Key param2: value to return if this value does not exists
        return userAuth;
    }


}
