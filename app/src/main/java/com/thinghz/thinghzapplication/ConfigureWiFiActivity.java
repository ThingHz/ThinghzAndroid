package com.thinghz.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thinghz.thinghzapplication.Utils.NetworkUtils;
import com.thinghz.thinghzapplication.Utils.PermissionUtils;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfigureWiFiActivity extends AppCompatActivity {
    ProgressBar progressBar,progressBarCred;
    TextView waiting, wifi_device_id,wifi_manager;
    EditText wifi_ssid, wifi_pass;
    Button wifi_button;
    TextInputLayout wifiSSIDLayout, wifiPassLayout;
    private static final int FINE_LOCATION_REQUEST = 1006;

    boolean isSuccess = false;
    private static final int TIMEOUT = 10000;
    private static final int CONN_TIMEOUT = 10000;
    private final String TAG = ConfigureWiFiActivity.this.getClass().getSimpleName();
    private final String SSID = "\"ThingHz-\"";
    private final String protocol = "http";
    private final String baseUrl = "192.168.4.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_wi_fi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Configure");
        progressBar = findViewById(R.id.progress_bar_wifi);
        waiting = findViewById(R.id.waiting);
        wifi_manager = findViewById(R.id.text_wifi_manager);
        wifi_pass = findViewById(R.id.edit_text_wifi_pass);
        wifi_device_id = findViewById(R.id.tv_wifi_device_id);
        wifi_ssid = findViewById(R.id.edit_text_wifi_ssid);
        wifi_button = findViewById(R.id.button_wif_configure);
        wifiSSIDLayout = findViewById(R.id.edit_text_wifi_ssid_layout);
        wifiPassLayout = findViewById(R.id.edit_text_wifi_pass_layout);
        progressBar.setVisibility(View.VISIBLE);
        progressBarCred = findViewById(R.id.progress_bar_cred);
        progressBarCred.setVisibility(View.GONE);
        PermissionUtils.requestPermission(ConfigureWiFiActivity.this,
                FINE_LOCATION_REQUEST,
                Manifest.permission.ACCESS_FINE_LOCATION);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Check for Auth Token
                if (!NetworkUtils.isWiFiNetworkAvailable(ConfigureWiFiActivity.this)) {
                    waiting.setText(ConfigureWiFiActivity.this.getResources().getString(R.string.wifi_not_enabled));
                    Log.i(TAG, "Wifi Not Enabled");
                    handler.postDelayed(this, 1000);
                } else if (NetworkUtils.isWiFiNetworkAvailable(ConfigureWiFiActivity.this) && !isConnectedTo(SSID)) {
                    waiting.setText(ConfigureWiFiActivity.this.getResources().getString(R.string.connect_to_thing));
                    Log.i(TAG, "Wifi Enabled but not connected to ThingHz-");
                    handler.postDelayed(this, 1000);
                } else if (NetworkUtils.isWiFiNetworkAvailable(ConfigureWiFiActivity.this) && isConnectedTo(SSID) && !isSuccess) {
                    waiting.setText(ConfigureWiFiActivity.this.getResources().getString(R.string.waiting));
                    Log.i(TAG, "Wifi Enabled and connected to ThingHz-, Waiting for connection");
                    new AsyncWiFiCheck().execute();
                    handler.postDelayed(this, 1000);
                } else if (NetworkUtils.isWiFiNetworkAvailable(ConfigureWiFiActivity.this) && isConnectedTo(SSID) && isSuccess) {
                    Log.i(TAG, "Everythings good remove callbacks");
                    handler.removeCallbacks(this);
                }
            }
        }, 3000);
        if (!NetworkUtils.isWiFiNetworkAvailable(this)) {
            waiting.setText(this.getResources().getString(R.string.wifi_not_enabled));
        }
        if (NetworkUtils.isWiFiNetworkAvailable(this) && !isConnectedTo(SSID)) {
            waiting.setText(this.getResources().getString(R.string.connect_to_thing));
        }

        wifi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ConfigureWiFiActivity.this,"ssid: "+wifi_ssid.getText().toString()+" pass: "+wifi_pass.getText().toString(),Toast.LENGTH_SHORT).show();
                new AsyncWiFiCred().execute();
            }
        });
    }

    private class AsyncWiFiCred extends AsyncTask<Void, Boolean, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarCred.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpURLConnection connection;
            URL url;
            try {
                String file = "/cred?ssid="+wifi_ssid.getText().toString()+"&pass="+wifi_pass.getText().toString();
                url = new URL(protocol,baseUrl,file);
                Log.d(TAG, url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "Malformed URL");
                return false;
            }
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(CONN_TIMEOUT);
                connection.setRequestMethod("GET");
                String request_meathod = connection.getRequestMethod();
                Log.d(TAG, "request:" + request_meathod);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int response_code = 0;
            try {
                response_code = connection.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder jResult = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        jResult.append(line);
                    }
                    return true;
                } else {
                    Log.d(TAG, "no http Connection");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                progressBarCred.setVisibility(View.GONE);
            }else{
                progressBarCred.setVisibility(View.GONE);
                Toast.makeText(ConfigureWiFiActivity.this,"Error saving cred",Toast.LENGTH_SHORT);
            }
        }
    }

    private class AsyncWiFiCheck extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection connection;
            URL url;
            progressBar.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.VISIBLE);
            try {
                url = new URL(protocol,baseUrl,"/check");
                Log.d(TAG, url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "Malformed URL");
                return e.toString();
            }
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(CONN_TIMEOUT);
                connection.setRequestMethod("GET");
                String request_meathod = connection.getRequestMethod();
                Log.d(TAG, "request:" + request_meathod);
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
            int response_code = 0;
            try {
                response_code = connection.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    //InputStream errorStream = connection.getErrorStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    // BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder jResult = new StringBuilder();
                    StringBuilder jError = new StringBuilder();
                    String line;
                    String error;

                    while ((line = bufferedReader.readLine()) != null) {
                        jResult.append(line);
                    }
                    return (jResult.toString());
                } else {
                    Log.d(TAG, "no http Connection");
                    return ("{\"Success\":\"false\"}");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.i(TAG,"JsonString"+s);
                    if (jsonObject.getString("Success").equals("false")) {
                        Toast.makeText(ConfigureWiFiActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                        isSuccess = false;
                    } else if (jsonObject.getString("Success").equals("true")) {
                        wifi_device_id.setVisibility(View.VISIBLE);
                        Log.i(TAG,"DeviceId:"+jsonObject.getString("DeviceId"));
                        wifi_device_id.setText(jsonObject.getString("DeviceId"));
                        progressBar.setVisibility(View.GONE);
                        waiting.setVisibility(View.GONE);
                        wifi_manager.setVisibility(View.VISIBLE);
                        wifi_button.setVisibility(View.VISIBLE);
                        wifiSSIDLayout.setVisibility(View.VISIBLE);
                        wifiPassLayout.setVisibility(View.VISIBLE);
                        wifi_pass.setVisibility(View.VISIBLE);
                        wifi_ssid.setVisibility(View.VISIBLE);
                        waiting.setVisibility(View.GONE);
                        isSuccess = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean isConnectedTo(String ssid) {
        boolean retVal = false;
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if (wifiInfo != null) {
            String currentConnectedSSID = wifiInfo.getSSID();
            Log.i(TAG, "Connected to:" + currentConnectedSSID);
            Log.i(TAG, "supposed ssid:" + ssid);
            if (currentConnectedSSID != null && ssid.equals(currentConnectedSSID)) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_REQUEST) {
            if (PermissionUtils.permissionGranted(requestCode, FINE_LOCATION_REQUEST, grantResults)) {
                Toast.makeText(ConfigureWiFiActivity.this, "Permission granted to fine location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

