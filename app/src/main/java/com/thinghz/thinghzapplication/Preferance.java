package com.thinghz.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceMetaDataModel;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceMetaRequest;
import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceRequest;
import com.thinghz.thinghzapplication.loginModel.LoginResponseModel;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.thinghz.thinghzapplication.retrofitInterface.AddDeviceInterface;
import com.thinghz.thinghzapplication.retrofitInterface.GetdeviceData;
import com.thinghz.thinghzapplication.retrofitInterface.UpdateDeviceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Preferance extends AppCompatActivity implements ExperimentListAdapter.UpdateMetaDataClickListener {

    private static final String TAG = "PreferanceActivity";
    private DataItem dataItem;
    ImageView img_update_device_Settings;
    TextView text_device_name, text_device_id, text_temp_value, text_lux_value, text_humid_value;
    Button button_device_experiment_update;
    private RecyclerView recyclerView;

    ProgressBar progress_experiment_update;

    private Retrofit retrofit;
    UpdateDeviceInterface updateDeviceInterface;
    GetdeviceData getDeviceData;
    UserAuth userAuth;
    ExperimentListAdapter experimentListAdapter;
    List<GetDeviceMetaDataModel.MetaData> metaDataList  = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferance);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Preferences");
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getDeviceData = retrofit.create(GetdeviceData.class);
        dataItem = new Gson().fromJson(getIntent().getStringExtra("DeviceData"),DataItem.class);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getApplicationContext());
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        img_update_device_Settings = findViewById(R.id.iv_update_device_Settings);
        text_device_name = findViewById(R.id.tv_device_name);
        text_lux_value = findViewById(R.id.tv_lux_value);
        text_humid_value = findViewById(R.id.tv_humid_value);
        text_temp_value = findViewById(R.id.tv_temp_value);
        text_device_id = findViewById(R.id.tv_device_id);
        recyclerView = findViewById(R.id.rv_experiment);
        progress_experiment_update = findViewById(R.id.pb_experiment_update);
        progress_experiment_update.setVisibility(View.VISIBLE);
        retrofitGetaMetaData(userAuth.getAuthToken(), dataItem.getDeviceId());
        updateDeviceInterface = retrofit.create(UpdateDeviceInterface.class);

        if (getIntent().getStringExtra("DeviceData") != null){
            Log.i(TAG, dataItem.getDeviceName());
            Log.i(TAG, dataItem.getDeviceId());
            text_device_name.setText((dataItem.getDeviceName()));
            text_device_id.setText(dataItem.getDeviceId());
            text_temp_value.setText(dataItem.getTemp());
            text_humid_value.setText(dataItem.getHumid());
            text_lux_value.setText(dataItem.getLux());
        }

        img_update_device_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Preferance.this, SettingsActivity.class);
                intent.putExtra("deviceName",dataItem.getDeviceName());
                intent.putExtra("sensorProfile",dataItem.getSensorProfile());
                intent.putExtra("deviceId",dataItem.getDeviceId());
                intent.putExtra("escalation", dataItem.getEscalation());
                startActivity(intent);
            }
        });

    }

    private void retrofitGetaMetaData(String authToken, String device_id) {

        Call<GetDeviceMetaDataModel> deviceMetaDataResponseCall = getDeviceData.getMetaData(authToken,device_id);
        deviceMetaDataResponseCall.enqueue(new Callback<GetDeviceMetaDataModel>() {
            @Override
            public void onResponse(Call<GetDeviceMetaDataModel> call, Response<GetDeviceMetaDataModel> response) {
                if (response.code() != 200) {
                    Log.e(TAG,"response is not 200");
                    recyclerView.setVisibility(View.GONE);
                }else if (response.body() != null) {
                    if (!response.body().isSuccess()) {
                        Log.e(TAG, "response body is not available");
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        progress_experiment_update.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        metaDataList = response.body().getData();
                        Log.i(TAG, "DeviceId: " + metaDataList.get(0).getDeviceId());
                        experimentListAdapter = new ExperimentListAdapter(Preferance.this, metaDataList, Preferance.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Preferance.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(experimentListAdapter);
                        experimentListAdapter.setMetaList(metaDataList);
                    }
                }
                progress_experiment_update.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<GetDeviceMetaDataModel> call, Throwable t) {

            }
        });
    }

    private void updateMetadata(UpdateDeviceMetaRequest updateDeviceMetaRequest) {
        progress_experiment_update.setVisibility(View.VISIBLE);

        Log.i("UpdateMetaData", "updateDeviceMetaData:"+new Gson().toJson(updateDeviceMetaRequest));

        Call<LoginResponseModel> updateResponseModelCall = updateDeviceInterface.updateDeviceMeta(userAuth.getAuthToken(),dataItem.getDeviceId(),updateDeviceMetaRequest);
        updateResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                progress_experiment_update.setVisibility(View.GONE);
                if (response.code() == 200){
                    Toast.makeText(Preferance.this, "Meta Data", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(Preferance.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });

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
    public void metaDataClick(UpdateDeviceMetaRequest updateDeviceMetaRequest) {
        updateMetadata(updateDeviceMetaRequest);
    }
}