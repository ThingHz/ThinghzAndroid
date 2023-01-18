package com.example.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.thinghzapplication.Utils.KeysUtils;
import com.example.thinghzapplication.Utils.SharedPreferanceHelper;
import com.example.thinghzapplication.deviceDataModel.Data;
import com.example.thinghzapplication.deviceDataModel.DeviceDataRoot;
import com.example.thinghzapplication.deviceDataModel.ItemsItem;
import com.example.thinghzapplication.deviceModel.DataItem;
import com.example.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.example.thinghzapplication.loginModel.UserAuth;
import com.example.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.example.thinghzapplication.retrofitInterface.GetDeviceDataForGraph;
import com.example.thinghzapplication.retrofitInterface.GetdeviceData;

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
        deviceName = getIntent().getExtras().getString(KeysUtils.getDeviceNameKey());
        deviceId = getIntent().getExtras().getString(KeysUtils.getDeviceIdKey());
        Log.i(TAG,"deviceName: "+deviceName);
        Log.i(TAG,"deviceId: "+deviceId);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(deviceName);
        setUpToolbar();
        sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getApplicationContext());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        alertbuilder = new AlertDialog.Builder(this);
        getDeviceDataForGraph = retrofit.create(GetDeviceDataForGraph.class);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        if(!isDataUpdated){
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG,"update Data again");
            retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected);
        }
        seriesData.add(new CustomDataEntry("", 0.0, 99.0, 400));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Temperature, Humidity and CO2 Graph");

        cartesian.yAxis(0).title("temperature, humidity and CO2");
        cartesian.xAxis(0).labels().padding(4d, 4d, 4d, 4d);
        set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
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


        Line series3 = cartesian.line(series3Mapping);
        series3.name("Gas");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(4d)
                .offsetY(4d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(4d);
        cartesian.legend().padding(0d, 0d, 5d, 0d);

        anyChartView.setChart(cartesian);
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
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected);
                return true;
            case R.id.item_12_hour:
                is12hSelected = true;
                is2hSelected = false;
                is24hSelected = false;
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected);
                return true;
            case R.id.item_24_hour:
                is24hSelected = true;
                is2hSelected = false;
                is12hSelected = false;
                retrofitUpdateData(deviceId,userAuth.getAuthToken(),is2hSelected,is12hSelected,is24hSelected);
                return true;
            case R.id.item_chart_select:
                alertbuilder.setCancelable(true).setTitle("Select Chart type");
                alertbuilder.setSingleChoiceItems(R.array.graph_entries, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                                Toast.makeText(AnalysisActivity.this,"temperature selected", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(AnalysisActivity.this,"humidity selected", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(AnalysisActivity.this,"CO2 selected", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(AnalysisActivity.this,"temperature and Humidity selected", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(AnalysisActivity.this,"Combined selected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                AlertDialog alert = alertbuilder.create();
                alert.show();

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

    }

    private String convertUnixTime(int timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        Date date = Date.from( instant );
        SimpleDateFormat sdf = new SimpleDateFormat(KeysUtils.getPATTERN_GRAPH());
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(date);
    }

    private void retrofitUpdateData(String device_id,String authToken,boolean is2hSelected, boolean is12hSelected, boolean is24hSelected) {
        Call<DeviceDataRoot> deviceDataRootCall = null;
        if(is2hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,8);
             seriesData.clear();
             set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(is12hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,48);
            seriesData.clear();
            set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(is24hSelected){
             deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,96);
            seriesData.clear();
            set.data(seriesData);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken,8);
        }
        deviceDataRootCall.enqueue(new Callback<DeviceDataRoot>() {
            @Override
            public void onResponse(Call<DeviceDataRoot> call, Response<DeviceDataRoot> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }if (!response.body().isSuccess()) {
                    Toast.makeText(getApplicationContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }else{
                    deviceDataObject = response.body().getData();
                    deviceDataList = deviceDataObject.getItems();
                    for (int i=deviceDataObject.getCount()-1 ;i>=0;i--){
                        if(deviceDataList.get(i).getGas()!=null && deviceDataList.get(i).getHumid()!=null && deviceDataList.get(i).getTemp()!=null) {
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
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<DeviceDataRoot> call, Throwable t) {

            }

        });
    }


}