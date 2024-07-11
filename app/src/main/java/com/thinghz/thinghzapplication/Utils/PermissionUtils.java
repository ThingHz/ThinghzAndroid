package com.thinghz.thinghzapplication.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionUtils {
    public static boolean requestPermission(Activity activity, int requestCode , String... permissions){
        boolean granted = true;
        final AlertDialog.Builder alertDialogue;
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
        }else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                alertDialogue = new AlertDialog.Builder(activity);
                alertDialogue.setMessage("You need camera permission to scan QR code to add device. To provide this permission Go to Settings->Apps->permissions")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            AlertDialog alert = alertDialogue.create();
            alert.setTitle("Why do you need this permission");
            alert.show();
        }else{
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    requestCode);
            return false;
        }
        return true;
    }


    public static boolean permissionGranted(int requestCode , int permissionCode, int[] grantResult){
        if (requestCode==permissionCode){
            if (grantResult.length >=0 && grantResult[0]==PackageManager.PERMISSION_GRANTED){
                return true;
            }else {
                return false;
            }
        }return false;
    }
}
