package com.thinghz.thinghzapplication.Utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class JWTUtils {
    public  static String decodeJWT(String encodeString) throws Exception
    {
        String[] splitString = new String[0];
        try{
            splitString = encodeString.split("\\.");
            Log.i("JWT_DECODED", "Header: " + getJson(splitString[0]));
            Log.i("JWT_DECODED", "Body: " + getJson(splitString[1]));
            Log.i("JWT_DECODED", "Signiture: " + getJson(splitString[2]));
        } catch (UnsupportedEncodingException e) {
            Log.i("JWT_DECODED", "Error Decodeinfg JSON");
        }

        return getJson(splitString[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
