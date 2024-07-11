package com.thinghz.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceRequestModel;
import com.thinghz.thinghzapplication.addDeviceModel.AddDeviceResponseModel;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.thinghz.thinghzapplication.retrofitInterface.AddDeviceInterface;
import com.thinghz.thinghzapplication.retrofitInterface.GetdeviceData;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDeviceActivity extends AppCompatActivity implements AddDeviceBottomSheetFragment.AddButtomClickListner {
    private CodeScanner codeScanner;
    private RecyclerView recyclerView;
    String device_id = "THING00021";
    int sensor_profile = 0;
    private final String TAG = AddDeviceActivity.this.getClass().getSimpleName();
    private Retrofit retrofit;
    AlertDialog.Builder alertbuilder;
    UserAuth userAuth;
    List<DataItem> deviceList = new ArrayList<>();
    AddedDeviceListAdapter deviceListAdapter;
    GetdeviceData getdeviceData;
    AddDeviceInterface addDeviceInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Device");
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(AddDeviceActivity.this);
        userAuth = sharedPreferanceHelper.getUserSavedValue();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
            codeScanner = new CodeScanner(this, scannerView);
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(AddDeviceActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject root = new JSONObject(result.getText());
                                device_id = root.getString("device_id");
                                sensor_profile = root.getInt("sensor_profile");
                                Log.i(TAG, "device_id: " + device_id);
                                Log.i(TAG, "sensor_prof: " + sensor_profile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            showBottomSheet();
                        }
                    });
                }
            });
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getdeviceData = retrofit.create(GetdeviceData.class);
        addDeviceInterface = retrofit.create(AddDeviceInterface.class);
        recyclerView = findViewById(R.id.id_recycler_add);
        //retrofitUpdateData(userAuth.getAuthToken());
        deviceList = this.getIntent().getParcelableArrayListExtra("deviceResponse");
        deviceListAdapter = new AddedDeviceListAdapter(deviceList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(deviceListAdapter);
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });

    }

    private void showBottomSheet() {
        AddDeviceBottomSheetFragment addDeviceBottomSheetFragment = AddDeviceBottomSheetFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("sensor_profile", sensor_profile);
        addDeviceBottomSheetFragment.setArguments(bundle);
        addDeviceBottomSheetFragment.show(getSupportFragmentManager(), "AddDeviceBottomSheetFragment");
    }


    @Override
    protected void onPause() {
        super.onPause();
        codeScanner.releaseResources();

    }



    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
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


    @Override
    public void onAddButtonClick(HashMap<String, String> map) {
        Log.i(TAG,"map device name: "+map.get(KeysUtils.getMap_device_name()));
        Log.i(TAG,"map maxTemp: "+map.get(KeysUtils.getMap_max_temp()));
        Log.i(TAG,"map minTemp: "+map.get(KeysUtils.getMap_min_temp()));
        if(!map.containsKey(KeysUtils.getMap_device_name())){
            Toast.makeText(this,"Please enter Device Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!map.containsKey(KeysUtils.getMap_min_temp()) || !map.containsKey(KeysUtils.getMap_max_temp())){
            Toast.makeText(this,"Please enter max/min temp",Toast.LENGTH_SHORT).show();
            return;
        }
        final AddDeviceRequestModel addDeviceRequestModel = new AddDeviceRequestModel(sensor_profile,map.get(KeysUtils.getMap_device_name()),device_id,0,map.get(KeysUtils.getMap_max_temp()),map.get(KeysUtils.getMap_min_temp()));
        addDevice(addDeviceRequestModel,userAuth.getAuthToken());
    }

    private void addDevice(AddDeviceRequestModel addDeviceRequestModel, String authToken) {
        Call<AddDeviceResponseModel> addDeviceResponseModelCall = addDeviceInterface.addDevice(addDeviceRequestModel,authToken);
        addDeviceResponseModelCall.enqueue(new Callback<AddDeviceResponseModel>() {
            @Override
            public void onResponse(Call<AddDeviceResponseModel> call, Response<AddDeviceResponseModel> response) {
                if (response.code() != 200) {
                    Toast.makeText(AddDeviceActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }if (!response.body().isSuccess()) {
                    Toast.makeText(AddDeviceActivity.this, "Error: " + response.body().getError(), Toast.LENGTH_SHORT).show();
                }else{
                    setupAlertDialog();
                }
            }

            @Override
            public void onFailure(Call<AddDeviceResponseModel> call, Throwable t) {

            }

        });
    }

    private void setupAlertDialog() {
        alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setMessage("Do you want to setup WiFi configuration for this device?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),ConfigureWiFiActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertbuilder.create();
        alert.setTitle("Logout");
        alert.show();
    }
}