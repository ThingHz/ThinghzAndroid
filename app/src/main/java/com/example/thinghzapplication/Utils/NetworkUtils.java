package com.example.thinghzapplication.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.thinghzapplication.NavigationActivity;

public class NetworkUtils {
    public  static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true;
                 }
                return false;
            }
        }else{
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }catch (Exception e){
                Log.i("update_statut", "" + e.getMessage());
            }
        }
        return false;
    }

    public  static boolean isWiFiNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                }
                return false;
            }
        }else{
            try {
                NetworkInfo isWifiConnected = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
                if(isWifiConnected.isConnected()){
                    return true;
                }
                return false;
            }catch (Exception e){
                Log.i("update_statut", "" + e.getMessage());
            }
        }
        return false;
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
