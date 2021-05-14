package com.example.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    List<ItemsItem> deviceDataList = new ArrayList<>();
    List<DataEntry> seriesData = new ArrayList<>();
    Data deviceDataObject;
    private Retrofit retrofit;
    Set set;
    GetDeviceDataForGraph getDeviceDataForGraph;
    boolean isDataUpdated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        deviceName = getIntent().getExtras().getString(KeysUtils.getDeviceNameKey());
        deviceId = getIntent().getExtras().getString(KeysUtils.getDeviceIdKey());
        Log.i(TAG,"deviceName: "+deviceName);
        Log.i(TAG,"deviceId: "+deviceId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(deviceName);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getApplicationContext());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getDeviceDataForGraph = retrofit.create(GetDeviceDataForGraph.class);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        if(!isDataUpdated){
            Log.i(TAG,"update Data again");
            retrofitUpdateData(deviceId,userAuth.getAuthToken());
        }
        seriesData.add(new CustomDataEntry("", 0.0, 99.0));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Temperature and Humidity Graph");

        cartesian.yAxis(0).title("temperature and humidity");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Line series1 = cartesian.line(series1Mapping);
        series1.name("Temperature");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Humidity");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
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

        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }

    }

    private String convertUnixTime(int timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        Date date = Date.from( instant );
        SimpleDateFormat sdf = new SimpleDateFormat(KeysUtils.getPATTERN_GRAPH());
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(date);
    }

    private void retrofitUpdateData(String device_id,String authToken) {
        Call<DeviceDataRoot> deviceDataRootCall = getDeviceDataForGraph.getResponse(device_id,authToken);
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
                        Log.i(TAG,"TimeStamp: "+ convertUnixTime(deviceDataList.get(i).getTimestamp()));
                        Log.i(TAG,"Temp: "+ Float.parseFloat(deviceDataList.get(i).getTemp()));
                        Log.i(TAG,"Humid: "+ Float.parseFloat(deviceDataList.get(i).getHumid()));
                        if(seriesData.add(new CustomDataEntry(convertUnixTime(deviceDataList.get(i).getTimestamp()), Float.parseFloat(deviceDataList.get(i).getTemp()), Float.parseFloat(deviceDataList.get(i).getHumid())))){
                            Log.i(TAG,"adding Serial data");
                        }
                        set.data(seriesData);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceDataRoot> call, Throwable t) {

            }

        });
    }


}