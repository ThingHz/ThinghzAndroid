package com.thinghz.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.gson.Gson;
import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.deviceDataModel.Data;
import com.thinghz.thinghzapplication.deviceDataModel.DeviceDataRoot;
import com.thinghz.thinghzapplication.deviceDataModel.ItemsItem;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.thinghz.thinghzapplication.retrofitInterface.GetDeviceDataForGraph;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AnalysisActivity extends AppCompatActivity  {
    private static final String TAG = "AnalysisActivity";
    private static String deviceName;
    private static String deviceId;
    private static int sensor_profile;
    private DataItem dataItem;
    ProgressBar progressBar;
    List<ItemsItem> deviceDataList = new ArrayList<>();
    List<DataEntry> seriesData = new ArrayList<>();
    Data deviceDataObject;
    AlertDialog.Builder alertbuilder;
    private Retrofit retrofit;
    private Toolbar toolbar;
    private boolean is2hSelected = false;
    private boolean is12hSelected = false;
    private boolean is24hSelected = false;
    Set set;
    GetDeviceDataForGraph getDeviceDataForGraph;
    boolean isDataUpdated = false;
    UserAuth userAuth;
    SharedPreferanceHelper sharedPreferanceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        progressBar = findViewById(R.id.progress_bar_analysis);
        dataItem = new Gson().fromJson(getIntent().getStringExtra("DeviceData"), DataItem.class);
        deviceName = dataItem.getDeviceName();
        deviceId = dataItem.getDeviceId();
        sensor_profile = dataItem.getSensorProfile();
        Log.i(TAG,"deviceName: "+deviceName);
        Log.i(TAG,"deviceId: "+deviceId);
        setTitle(deviceName);
        setUpToolbar();
        sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getApplicationContext());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        alertbuilder = new AlertDialog.Builder(this);
        getDeviceDataForGraph = retrofit.create(GetDeviceDataForGraph.class);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        if(!isDataUpdated){
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG,"update Data again");
            retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected,sensor_profile);
        }
        setDefaultValues(sensor_profile);
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.xScroller(true);
        cartesian.xScroller().maxHeight(35);
        cartesian.xScroller().minHeight(5);
        cartesian.xAxis(0).labels().padding(4d, 4d, 4d, 4d);
        set = Set.instantiate();
        set.data(seriesData);
        setmapping(sensor_profile,cartesian);
        anyChartView.setChart(cartesian);
    }

    private void setmapping(int sensor_profile, Cartesian cartesian) {
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        switch (sensor_profile){
            case 0:
                cartesian.title("Temperature and Humidity graph");
                cartesian.yAxis(0).title("temperature and humidity");
                Line series1 = cartesian.line(series1Mapping);
                series1.name("Temperature");
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);

                Line series2 = cartesian.line(series2Mapping);
                series2.name("Humidity");
                series2.hovered().markers().enabled(true);
                series2.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                break;
            case 1:
                cartesian.title("Temperature and Humidity graph");
                cartesian.yAxis(0).title("temperature and humidity");
                Line series1_1 = cartesian.line(series1Mapping);
                series1_1.name("Temperature");
                series1_1.hovered().markers().enabled(true);
                series1_1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1_1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);

                Line series2_1 = cartesian.line(series2Mapping);
                series2_1.name("Humidity");
                series2_1.hovered().markers().enabled(true);
                series2_1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2_1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                break;
            case 4:
                cartesian.title("Temperature, Humidity and CO2 graph");
                cartesian.yAxis(0).title("temperature, humidity and CO2");
                Line series1_4 = cartesian.line(series1Mapping);
                series1_4.name("Temperature");
                series1_4.hovered().markers().enabled(true);
                series1_4.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1_4.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);

                Line series2_4 = cartesian.line(series2Mapping);
                series2_4.name("Humidity");
                series2_4.hovered().markers().enabled(true);
                series2_4.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2_4.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                Line series3_4 = cartesian.line(series3Mapping);
                series3_4.name("CO2");
                series3_4.hovered().markers().enabled(true);
                series3_4.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series3_4.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                break;
            case 5:
                cartesian.title("Temperature, Humidity and LUX graph");
                cartesian.yAxis(0).title("temperature, humidity and lux");
                Line series1_5 = cartesian.line(series1Mapping);
                series1_5.name("Temperature");
                series1_5.hovered().markers().enabled(true);
                series1_5.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1_5.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);

                Line series2_5 = cartesian.line(series2Mapping);
                series2_5.name("Humidity");
                series2_5.hovered().markers().enabled(true);
                series2_5.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2_5.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                Line series3_5 = cartesian.line(series3Mapping);
                series3_5.name("Lux");
                series3_5.hovered().markers().enabled(true);
                series3_5.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series3_5.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                break;
            default:
                Line series1_d = cartesian.line(series1Mapping);
                series1_d.name("Temperature");
                series1_d.hovered().markers().enabled(true);
                series1_d.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1_d.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);

                Line series2_d = cartesian.line(series2Mapping);
                series2_d.name("Humidity");
                series2_d.hovered().markers().enabled(true);
                series2_d.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2_d.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(4d)
                        .offsetY(4d);
                break;


        }

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(14d);
        cartesian.legend().padding(5d, 5d, 5d, 5d);
        cartesian.legend().background().enabled(true);
        cartesian.legend().background().fill("#96a6a6 0.3").stroke("#96a6a6").corners(5).cornerType("round");
    }

    private void setDefaultValues(int sensor_profile) {
        switch (sensor_profile){
            case 0:
                seriesData.add(new CustomDataEntry("", 0.0, 99.0));
                break;
            case 4:
                seriesData.add(new CustomDataEntry("", 0.0, 99.0, 400));
                break;
            case 5:
                seriesData.add(new CustomDataEntry("", 0.0, 99.0, 100));
                break;
            default:
                seriesData.add(new CustomDataEntry("", 0.0, 99.0));
                break;
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_analysis_layout,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getApplicationContext());
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_2_hour:
                is2hSelected = true;
                is12hSelected = false;
                is24hSelected = false;
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected,sensor_profile);
                return true;
            case R.id.item_12_hour:
                is12hSelected = true;
                is2hSelected = false;
                is24hSelected = false;
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected,sensor_profile);
                return true;
            case R.id.item_24_hour:
                is24hSelected = true;
                is2hSelected = false;
                is12hSelected = false;
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected,sensor_profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

        CustomDataEntry(String x, Number value, Number value2) {
            super(x,value);
            setValue("value2",value2);
        }
    }

    private String convertUnixTime(int timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        Date date = Date.from( instant );
        SimpleDateFormat sdf = new SimpleDateFormat(KeysUtils.getPATTERN_GRAPH());
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(date);
    }

    private void retrofitUpdateData(String device_id,String authToken,boolean is2hSelected, boolean is12hSelected, boolean is24hSelected, int sensor_profile) {
        Call<DeviceDataRoot> deviceDataRootCall = null;
        if(is2hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,24);
             seriesData.clear();
             set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(is12hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,144);
            seriesData.clear();
            set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(is24hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,288);
            seriesData.clear();
            set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,24);
        }
        deviceDataRootCall.enqueue(new Callback<DeviceDataRoot>() {
            @Override
            public void onResponse(Call<DeviceDataRoot> call, Response<DeviceDataRoot> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }if (!response.body().isSuccess()) {
                    Toast.makeText(getApplicationContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }else {

                        deviceDataObject = response.body().getData();
                        deviceDataList = deviceDataObject.getItems();
                        switch (sensor_profile) {
                            case 0:
                                for (int i = deviceDataObject.getCount() - 1; i >= 0; i--) {
                                    if (deviceDataList.get(i).getHumid() != null && deviceDataList.get(i).getTemp() != null) {
                                        Log.i(TAG, "TimeStamp: " + convertUnixTime(deviceDataList.get(i).getTimestamp()));
                                        Log.i(TAG, "Temp: " + Float.parseFloat(deviceDataList.get(i).getTemp()));
                                        Log.i(TAG, "Humid: " + Float.parseFloat(deviceDataList.get(i).getHumid()));

                                        if (seriesData.add(new CustomDataEntry(convertUnixTime(deviceDataList.get(i).getTimestamp()), Float.parseFloat(deviceDataList.get(i).getTemp()), Float.parseFloat(deviceDataList.get(i).getHumid())))) {
                                            Log.i(TAG, "adding Serial data");
                                        }
                                        set.data(seriesData);
                                    }
                                }
                                break;
                            case 4:
                                for (int i = deviceDataObject.getCount() - 1; i >= 0; i--) {
                                    if (deviceDataList.get(i).getGas() != null && deviceDataList.get(i).getHumid() != null && deviceDataList.get(i).getTemp() != null) {
                                        Log.i(TAG, "TimeStamp: " + convertUnixTime(deviceDataList.get(i).getTimestamp()));
                                        Log.i(TAG, "Temp: " + Float.parseFloat(deviceDataList.get(i).getTemp()));
                                        Log.i(TAG, "Humid: " + Float.parseFloat(deviceDataList.get(i).getHumid()));
                                        Log.i(TAG, "Gas: " + Float.parseFloat(deviceDataList.get(i).getGas()));

                                    if (seriesData.add(new CustomDataEntry(convertUnixTime(deviceDataList.get(i).getTimestamp()), Float.parseFloat(deviceDataList.get(i).getTemp()), Float.parseFloat(deviceDataList.get(i).getHumid()), Float.parseFloat(deviceDataList.get(i).getGas())))) {
                                        Log.i(TAG, "adding Serial data");
                                    }
                                    set.data(seriesData);
                                    }
                                }
                                break;
                            case 5:
                                for (int i = deviceDataObject.getCount() - 1; i >= 0; i--) {
                                    if (deviceDataList.get(i).getLux() != null && deviceDataList.get(i).getHumid() != null && deviceDataList.get(i).getTemp() != null) {
                                        Log.i(TAG, "TimeStamp: " + convertUnixTime(deviceDataList.get(i).getTimestamp()));
                                        Log.i(TAG, "Temp: " + Float.parseFloat(deviceDataList.get(i).getTemp()));
                                        Log.i(TAG, "Humid: " + Float.parseFloat(deviceDataList.get(i).getHumid()));
                                        Log.i(TAG, "Lux: " + Float.parseFloat(deviceDataList.get(i).getLux()));

                                        if (seriesData.add(new CustomDataEntry(convertUnixTime(deviceDataList.get(i).getTimestamp()), Float.parseFloat(deviceDataList.get(i).getTemp()), Float.parseFloat(deviceDataList.get(i).getHumid()), Float.parseFloat(deviceDataList.get(i).getLux())))) {
                                            Log.i(TAG, "adding Serial data");
                                        }
                                        set.data(seriesData);
                                    }
                                }
                                break;
                            default:
                                for (int i = deviceDataObject.getCount() - 1; i >= 0; i--) {
                                    if (deviceDataList.get(i).getHumid() != null && deviceDataList.get(i).getTemp() != null) {
                                        Log.i(TAG, "TimeStamp: " + convertUnixTime(deviceDataList.get(i).getTimestamp()));
                                        Log.i(TAG, "Temp: " + Float.parseFloat(deviceDataList.get(i).getTemp()));
                                        Log.i(TAG, "Humid: " + Float.parseFloat(deviceDataList.get(i).getHumid()));

                                        if (seriesData.add(new CustomDataEntry(convertUnixTime(deviceDataList.get(i).getTimestamp()), Float.parseFloat(deviceDataList.get(i).getTemp()), Float.parseFloat(deviceDataList.get(i).getHumid())))) {
                                            Log.i(TAG, "adding Serial data");
                                        }
                                        set.data(seriesData);
                                    }
                                }
                                break;
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<DeviceDataRoot> call, Throwable t) {

            }

        });
    }


}