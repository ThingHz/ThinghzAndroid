package com.example.thinghzapplication.Utils;

public class KeysUtils {
    public static String getMyPreferences() {
        return MY_PREFERENCES;
    }

    public static String getShareKeyIsfirst() {
        return SHARE_KEY_ISFIRST;
    }

    public static String getShareKeyToken() {
        return SHARE_KEY_TOKEN;
    }

    public static String getDeviceNameKey() {
        return DEVICE_NAME_KEY;
    }

    public static String getDeviceIdKey() {
        return DEVICE_ID_KEY;
    }

    public static String getPATTERN() {
        return PATTERN;
    }

    public static String getPATTERN_GRAPH() {
        return PATTERN_GRAPH;
    }

    public static String getMap_device_name() { return map_device_name; }

    public static String getMap_min_temp() { return map_min_temp; }

    public static String getMap_max_temp() { return map_max_temp; }

    public static String getUser_fragment_bundle_userName() { return user_fragment_bundle_userName; }

    public static String getUser_fragment_bundle_location() { return user_fragment_bundle_location; }

    public static String getUser_fragment_bundle_email() { return user_fragment_bundle_email; }

    public static String getFilter_device_status() {
        return filter_device_status;
    }

    public static String getFilter_escalation() {
        return filter_escalation;
    }

    public static String getFilter_sensor_profile() {
        return filter_sensor_profile;
    }

    private static final String MY_PREFERENCES = "preferances";
    private static final String SHARE_KEY_ISFIRST = "isFirstUser";
    private static final String SHARE_KEY_TOKEN = "authToken";
    private static final String DEVICE_NAME_KEY = "device_name";
    private static final String DEVICE_ID_KEY = "device_id";
    private static final String PATTERN = "dd/MM/yyyy HH:mm";
    private static final String PATTERN_GRAPH = "dd/MM HH:mm";
    private static final String map_device_name = "device_name";
    private static final String map_min_temp = "min_temp";
    private static final String map_max_temp = "max_temp";
    private static final String user_fragment_bundle_userName = "user_name";
    private static final String user_fragment_bundle_location = "location";
    private static final String user_fragment_bundle_email = "email_id";
    private static final String filter_device_status = "filter_status";
    private static final String filter_escalation = "filter_escalation";
    private static final String filter_sensor_profile = "filter_profile";

}
