package com.example.thinghzapplication.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.thinghzapplication.NavigationActivity;

public class NetworkUtils {
    public  static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean validateUserData(String userName , String password, Context context) {
        Log.i("LoginFragment", "userId is -> " + userName);
        Log.i("LoginFragment", "password is -> " + password);

        if (userName.trim().equals("")) {
            Toast.makeText(context,"UserName cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.trim().equals("")) {
            Toast.makeText(context,"Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
