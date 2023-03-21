package com.thinghz.thinghzapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.thinghz.thinghzapplication.loginModel.UserAuth;

public class SharedPreferanceHelper {
    private static SharedPreferanceHelper instance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    UserAuth userAuth;

    private static String authToken;
    private static boolean isFirstUser;


    public SharedPreferanceHelper() {

    }

    public static SharedPreferanceHelper getInstance(Context context) {

        if (instance == null){
            instance = new SharedPreferanceHelper();
            sharedPreferences = context.getSharedPreferences(KeysUtils.getMyPreferences(),context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return instance;
    }

    public void saveUserInfo(UserAuth userAuth){
        editor.putBoolean(KeysUtils.getShareKeyIsfirst(), userAuth.getIsFirstUser());
        editor.putString(KeysUtils.getShareKeyToken(), userAuth.getAuthToken());
        if(!editor.commit()){
            Log.i("Shared Preferance","Error while saving it to shared preferance");
        }
    }

    public void removeToken(boolean delete){
        if(delete){
            editor.remove(KeysUtils.getShareKeyIsfirst());
            editor.remove(KeysUtils.getShareKeyToken());
            editor.apply();
        }
    }
    public UserAuth getUserSavedValue(){
        try{

            String authToken = sharedPreferences.getString(KeysUtils.getShareKeyToken(),null);
            boolean isFirstUser = sharedPreferences.getBoolean(KeysUtils.getShareKeyIsfirst(),true);
            Log.i("Shared Preferance","Auth Value: "+authToken);
            userAuth = new UserAuth(authToken,isFirstUser);

        }catch(NullPointerException e) {
            Log.i("Shared","Exception"+e);
        }
        //param1: Key param2: value to return if this value does not exists
        return userAuth;
    }

    public boolean isFirstUserExists(){
        return sharedPreferences.contains(KeysUtils.getShareKeyIsfirst());
    }

    public boolean isAuthTokenExists(){
        return sharedPreferences.contains(KeysUtils.getShareKeyToken());
    }

}
