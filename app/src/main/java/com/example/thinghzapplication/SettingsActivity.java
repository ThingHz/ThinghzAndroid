package com.example.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinghzapplication.Utils.SharedPreferanceHelper;
import com.example.thinghzapplication.deviceModel.DataItem;
import com.example.thinghzapplication.deviceModel.UpdateDeviceRequest;
import com.example.thinghzapplication.loginModel.LoginResponseModel;
import com.example.thinghzapplication.loginModel.UserAuth;
import com.example.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.example.thinghzapplication.retrofitInterface.AddDeviceInterface;
import com.example.thinghzapplication.retrofitInterface.LoginInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinner_max_temp,spinner_min_Temp,spinner_max_humidity,spinner_min_humidity,spinner_max_carbon,spinner_min_carbon;
    ImageView imgEdit;
    TextInputEditText edit_text_device_name,edit_text_location;
    TextView txt_sensor_profile;
    Button button_update_device;
    String max_temp,min_temp,max_humid,min_humid,max_carbon,min_carbon;
    private DataItem dataItem;
    AddDeviceInterface addDeviceInterface;
    UserAuth userAuth;
    ProgressBar progress_bar;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        addDeviceInterface = retrofit.create(AddDeviceInterface.class);
        dataItem = new Gson().fromJson(getIntent().getStringExtra("DeviceData"),DataItem.class);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(this);
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        spinner_max_temp = findViewById(R.id.spinner_max_temp);
        spinner_min_Temp = findViewById(R.id.spinner_min_temp);
        spinner_max_humidity = findViewById(R.id.spinner_max_humidity);
        spinner_min_humidity = findViewById(R.id.spinner_min_humidity);
        spinner_max_carbon = findViewById(R.id.spinner_max_carbon);
        spinner_min_carbon = findViewById(R.id.spinner_min_carbon);
        spinner_min_carbon = findViewById(R.id.spinner_min_carbon);
        imgEdit = findViewById(R.id.imgEdit);
        edit_text_device_name = findViewById(R.id.edit_text_device_name);
        txt_sensor_profile = findViewById(R.id.txt_sensor_profile);
        button_update_device = findViewById(R.id.button_update_device);
        edit_text_location = findViewById(R.id.edit_text_location);
        progress_bar = findViewById(R.id.progress_bar);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_text_device_name.getTag().toString().equalsIgnoreCase("disable")){
                    Log.i("Onclick", "onClick:"+edit_text_device_name.getTag());
                    edit_text_device_name.setTag("enable");
                    edit_text_device_name.setEnabled(true);
                    edit_text_device_name.requestFocus();
                }
                else {
                    edit_text_device_name.setTag("disable");
                    edit_text_device_name.setEnabled(false);
                }
            }
        });





        ArrayAdapter adapterMaxTemp = ArrayAdapter.createFromResource(this, R.array.max_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max_temp.setAdapter(adapterMaxTemp);

        spinner_max_temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("OnItemSelected", "onItemSelected:"+adapterView.getSelectedItem().toString());
                max_temp = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapterminTemp = ArrayAdapter.createFromResource(this, R.array.min_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterminTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_Temp.setAdapter(adapterminTemp);

        spinner_min_Temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                min_temp = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapterMaxHumidity = ArrayAdapter.createFromResource(this, R.array.max_Humidity_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max_humidity.setAdapter(adapterMaxHumidity);

        spinner_max_humidity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("OnItemSelected", "onItemSelected:"+adapterView.getSelectedItem().toString());
                max_humid = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapterMinHumidity = ArrayAdapter.createFromResource(this, R.array.min_Humidity_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_humidity.setAdapter(adapterMinHumidity);

        spinner_min_humidity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                min_humid = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapterMaxCarbon = ArrayAdapter.createFromResource(this, R.array.max_carbon_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max_carbon.setAdapter(adapterMaxCarbon);

        spinner_max_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                max_carbon = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapterMinCarbon = ArrayAdapter.createFromResource(this, R.array.min_carbon_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_carbon.setAdapter(adapterMinCarbon);

        spinner_min_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                min_carbon = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (getIntent().getStringExtra("DeviceData") != null){
            edit_text_device_name.setText(dataItem.getDeviceName());
            txt_sensor_profile.setText(String.valueOf(dataItem.getSensorProfile()));
        }

        button_update_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               updateDevice();
            }
        });

    }

    private void updateDevice() {
        progress_bar.setVisibility(View.VISIBLE);
        UpdateDeviceRequest updateDeviceRequest = new UpdateDeviceRequest();
        updateDeviceRequest.setDeviceName(edit_text_device_name.getText().toString());
        updateDeviceRequest.setLocation(edit_text_location.getText().toString());
        UpdateDeviceRequest.Range range = new UpdateDeviceRequest.Range();
        range.setMaxTemp(max_temp);
        range.setMinTemp(min_temp);
        range.setMaxHumid(max_humid);
        range.setMinHumid(min_humid);
        range.setMaxGas(max_carbon);
        range.setMinGas(min_carbon);

        updateDeviceRequest.setRange(range);

        Log.i("UpdateDevice", "updateDevice:"+new Gson().toJson(updateDeviceRequest));

        Call<LoginResponseModel> updateResponseModelCall = addDeviceInterface.updateDevice(userAuth.getAuthToken(),dataItem.getDeviceId(),updateDeviceRequest);
        updateResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                progress_bar.setVisibility(View.GONE);
                if (response.code() == 200){
                    Toast.makeText(SettingsActivity.this, "Device Updated", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();

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
}