package com.example.thinghzapplication.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionUtils {
    public static boolean requestPermission(Activity activity, int requestCode , String... permissions){
        boolean granted = true;
        ArrayList<String> permissionNeeded = new ArrayList<>();

        for (String s: permissions){
            int permissionCheck = activity.checkSelfPermission(s);
            boolean permissionGranted = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            granted &= permissionGranted;
            if (!permissionGranted){
                permissionNeeded.add(s);
            }
        }
        if (granted){
            return true;
        }else {
            if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                activity.requestPermissions(new String[permissionNeeded.size()],
                        requestCode);
            }
            ActivityCompat.requestPermissions(activity,
                    permissionNeeded.toArray(new String[permissionNeeded.size()]),
                    requestCode);
            return false;
        }
    }

    public static boolean permissionGranted(int requestCode , int permissionCode, int[] grantResult){
        if (requestCode==permissionCode){
            if (grantResult.length>=0 && grantResult[0]==PackageManager.PERMISSION_GRANTED){
                return true;
            }else {
                return false;
            }
        }return false;
    }
}
