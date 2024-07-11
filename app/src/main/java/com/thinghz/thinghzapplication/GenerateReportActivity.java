package com.thinghz.thinghzapplication;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.thinghz.thinghzapplication.UserAuth.UserAuthModel;
import com.thinghz.thinghzapplication.Utils.JWTUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.deviceDataModel.DeviceDataDownload;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.thinghz.thinghzapplication.retrofitInterface.GetAllDevices;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GenerateReportActivity extends AppCompatActivity {
    private final String TAG = GenerateReportActivity.this.getClass().getSimpleName();
    private RadioButton radio2Days, radio1Week, radio1Month, radioCustom;
    private TextView radioLable, pickDeviceLable, tvPickStartTime,tvPickEndTime,tvPickStartDate,tvPickEndDate, progressInfo, progressPercentage;
    private ImageView startDatePick, endDatePick, startTimePick, endTimePick;
    private RadioGroup radioGroup;
    private Button btnDownload;
    private Retrofit retrofit;
    private Spinner spinner;
    private ProgressBar progressBarLinear;
    private LinearLayout layoutProgress, layoutCustom;
    private DynamoDBMapper dynamoDBMapper;
    private String spinnerSelect = null;
    private int endYear,endMonth,endDay,endHour,endMinute = -1;
    private int startYear,startMonth,startDay,startHour,startMinute = -1;
    UserAuth userAuth;
    GetAllDevices getdevice;
    List<DataItem> deviceList = new ArrayList<>();
    private int radioPosition;
    private ProgressBar progressBar;
    private static final String COGNITO_POOL_ID = "us-east-1:3cf54d60-82e7-4cfc-a742-646dafc2e565";
    private static final Regions MY_REGION = Regions.US_EAST_1;
    CognitoCachingCredentialsProvider credentialsProvider;
    int progress = 0;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    String userName, emailId, location;

    enum Download_State{
        QUERY,
        WORKBOOK,
        DOWNLOAD,
        DONE,
        ERROR_QUERY,
        ERROR_EXCEL,
        ERROR_DATE,
        ERROR_DOWNLOADING
    }

    Download_State state = Download_State.QUERY;
    UserAuthModel userAuthModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_generate_report);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(GenerateReportActivity.this);
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        String jsonString;
        try {
            long issuedAt,expireAt;
            jsonString = decodeAuthToken(userAuth.getAuthToken());
            Log.i(TAG,jsonString);
            JSONObject root = new JSONObject(jsonString);
            userName = root.getJSONObject("user").getString("userName");
            emailId = root.getJSONObject("user").getString("email_id");
            location = root.getJSONObject("user").getString("location");
            issuedAt = root.getLong("iat");
            expireAt = root.getLong("exp");
            userAuthModel = new UserAuthModel(userName,emailId,location,issuedAt,expireAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        radio2Days = findViewById(R.id.rb_2_days);
        radio1Week = findViewById(R.id.rb_1_week);
        radio1Month = findViewById(R.id.rb_1_month);
        radioCustom = findViewById(R.id.rb_custom);
        pickDeviceLable = findViewById(R.id.tv_pick_device_label);
        radioLable = findViewById(R.id.tv_pick_interval_lable);
        btnDownload = findViewById(R.id.bt_download_report);
        layoutProgress = findViewById(R.id.layout_progress);
        layoutCustom = findViewById(R.id.ll_custom_time);
        progressBarLinear = findViewById(R.id.progress_horizontal);
        progressPercentage = findViewById(R.id.tv_percent_value);
        progressInfo = findViewById(R.id.tv_progress_info);
        radioGroup = findViewById(R.id.rg_pick_interval);
        progressBar = findViewById(R.id.pb_getDevices);
        spinner = findViewById(R.id.dd_device_select);
        startDatePick = findViewById(R.id.iv_pick_date_start);
        endDatePick = findViewById(R.id.iv_pick_date_end);
        startTimePick = findViewById(R.id.iv_pick_time_start);
        endTimePick = findViewById(R.id.iv_pick_time_end);
        tvPickStartTime = findViewById(R.id.tv_pick_time_start);
        tvPickEndTime = findViewById(R.id.tv_pick_time_end);
        tvPickStartDate = findViewById(R.id.tv_pick_date_start);
        tvPickEndDate = findViewById(R.id.tv_pick_date_end);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getdevice = retrofit.create(GetAllDevices.class);
        spinner.setVisibility(GONE);
        layoutProgress.setVisibility(GONE);
        radioGroup.setVisibility(GONE);
        btnDownload.setVisibility(GONE);
        pickDeviceLable.setVisibility(GONE);
        radioLable.setVisibility(GONE);
        layoutCustom.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        retrofitUpdateData(userAuth.getAuthToken());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelect = parent.getItemAtPosition(position).toString();
                Log.i(TAG, spinnerSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Initialize the AWS Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // context
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );

        AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentialsProvider);
        dynamoDBMapper= new DynamoDBMapper(client);

        if (radioGroup.getCheckedRadioButtonId() == -1 && spinnerSelect != null){
            Log.i(TAG, String.valueOf(radioGroup.getCheckedRadioButtonId()) + spinnerSelect);
            layoutProgress.setVisibility(GONE);
            btnDownload.setVisibility(GONE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_2_days:
                        radioPosition = 0;
                        layoutCustom.setVisibility(GONE);
                        break;
                    case R.id.rb_1_week:
                        radioPosition = 1;
                        layoutCustom.setVisibility(GONE);
                        break;
                    case R.id.rb_1_month:
                        radioPosition = 2;
                        layoutCustom.setVisibility(GONE);
                        break;
                    case R.id.rb_custom:
                        layoutCustom.setVisibility(View.VISIBLE);
                        radioPosition = 3;
                }
            }
        });

        startTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentTime = Calendar.getInstance();
                final int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog_start_time;
                mTimePickerDialog_start_time = new TimePickerDialog(GenerateReportActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int mStartHour, int mStartMinute) {
                        startHour = mStartHour;
                        startMinute = mStartMinute;
                        String hhmmTime = String.valueOf(mStartHour) + ":" + String.valueOf(mStartMinute);
                        tvPickStartTime.setText(hhmmTime);
                    }
                },hour,minute,true);
                mTimePickerDialog_start_time.setTitle("Pick Start Time");
                mTimePickerDialog_start_time.show();
            }
        });

        startDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentDate = Calendar.getInstance();
                final int year = mCurrentDate.get(Calendar.YEAR);
                final int  month = mCurrentDate.get(Calendar.MONTH);
                final int dayOfMonth = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePickerDialog_start_Date;
                mDatePickerDialog_start_Date = new DatePickerDialog(GenerateReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startYear = year;
                        startMonth = month+1;
                        startDay = dayOfMonth;
                        String ddmmyyyydate = String.valueOf(dayOfMonth) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year);
                        tvPickStartDate.setText(ddmmyyyydate);
                    }
                },year,month, dayOfMonth);
                mDatePickerDialog_start_Date.setTitle("Pick Start Date");
                mDatePickerDialog_start_Date.show();
            }
        });

        endTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentTime = Calendar.getInstance();
                final int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog_end_time;
                mTimePickerDialog_end_time = new TimePickerDialog(GenerateReportActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int mStartHour, int mStartMinute) {
                        endHour = mStartHour;
                        endMinute = mStartMinute;
                        String hhmmTime = String.valueOf(mStartHour) + ":" + String.valueOf(mStartMinute);
                        tvPickEndTime.setText(hhmmTime);
                    }
                },hour,minute,true);
                mTimePickerDialog_end_time.setTitle("Pick End Time");
                mTimePickerDialog_end_time.show();
            }
        });

        endDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentDate = Calendar.getInstance();
                final int year = mCurrentDate.get(Calendar.YEAR);
                final int  month = mCurrentDate.get(Calendar.MONTH);
                final int dayOfMonth = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePickerDialog_end_Date;
                mDatePickerDialog_end_Date = new DatePickerDialog(GenerateReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endYear = year;
                        endMonth = month+1;
                        endDay = dayOfMonth;
                        String ddmmyyyydate = String.valueOf(dayOfMonth) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year);
                        tvPickEndDate.setText(ddmmyyyydate);
                    }
                },year,month, dayOfMonth);
                mDatePickerDialog_end_Date.setTitle("Pick Start Date");
                mDatePickerDialog_end_Date.show();
            }
        });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUIEnabled(false);
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Update progress on UI
                                downloadData();
                            } catch (IOException | ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        }


    private void retrofitUpdateData(String authToken) {
        Log.i(TAG, authToken);
        Call<GetDeviceResponseModel> deviceResponseModelCall = getdevice.getResponse(authToken);
        deviceResponseModelCall.enqueue(new Callback<GetDeviceResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GetDeviceResponseModel> call, @NonNull Response<GetDeviceResponseModel> response) {

                if (response.code() != 200) {
                    Log.e(TAG,"response is not 200");
                }else if (response.body() != null) {
                    if (!response.body().isSuccess()) {
                        Log.e(TAG, "response body is not available");
                    }else if (response.body().getData().size() == 0){
                        Log.e(TAG, "no response available");
                        progressBar.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.VISIBLE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        radioLable.setVisibility(View.VISIBLE);
                        pickDeviceLable.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        btnDownload.setVisibility(View.VISIBLE);
                        deviceList = response.body().getData();
                        Log.i(TAG,deviceList.get(0).getDeviceId() + ":" + deviceList.get(0).getDeviceName());
                        String[] stringArray = new String[deviceList.size()];
                        Log.i(TAG, String.valueOf(deviceList.size()));
                            for(int i = 0; i<= deviceList.size()-1; i++) {
                                stringArray[i] = deviceList.get(i).getDeviceId();
                            }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(GenerateReportActivity.this, android.R.layout.simple_spinner_dropdown_item, stringArray);
                        spinner.setAdapter(adapter);
                        Log.i(TAG, "DeviceId: " + deviceList.get(0).getDeviceId());
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(@NonNull Call<GetDeviceResponseModel> call, @NonNull Throwable t) {
                Log.e(TAG, "could not get response");
            }
        });
    }

    private void updateProgress(Download_State progressState){
        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                layoutProgress.setVisibility(View.VISIBLE);
                switch (progressState){
                    case QUERY:
                        Log.i(TAG,"Running Query");
                        progressBarLinear.setProgress(20);
                        progressPercentage.setText("20");
                        progressInfo.setText("Fetching data from Server");
                        break;
                    case WORKBOOK:
                        Log.i(TAG,"generating Excel");
                        progressBarLinear.setProgress(60);
                        progressPercentage.setText("60");
                        progressInfo.setText("Preparing Excel");
                        break;
                    case DOWNLOAD:
                        Log.i(TAG,"downloading excel");
                        progressBarLinear.setProgress(80);
                        Log.i(TAG,"Preparing Excel");
                        progressPercentage.setText("80");
                        progressInfo.setText("Starting Download");
                        break;
                    case DONE:
                        Log.i(TAG,"Download Finished");
                        progressBarLinear.setProgress(100);
                        progressPercentage.setText("100");
                        progressInfo.setText("Download Finished");
                        break;
                    case ERROR_QUERY:
                        Log.i(TAG,"Error Processing Query");
                        progressBarLinear.setProgress(0);
                        progressPercentage.setText("0");
                        progressInfo.setTextColor(getColor(R.color.design_default_color_error));
                        progressInfo.setText("No Data available for this time range");
                        break;
                    case ERROR_EXCEL:
                        Log.i(TAG,"Error generating Excel");
                        progressBarLinear.setProgress(0);
                        progressPercentage.setText("0");
                        progressInfo.setTextColor(getColor(R.color.design_default_color_error));
                        progressInfo.setText("Error Creating Excel Try Again!");
                    case ERROR_DOWNLOADING:
                        Log.i(TAG, "Error downloading");
                        progressBarLinear.setProgress(0);
                        progressPercentage.setText("0");
                        progressInfo.setTextColor(getColor(R.color.design_default_color_error));
                        progressInfo.setText("Error Downloading Excel");
                        break;
                    case ERROR_DATE:
                        Log.i(TAG, "more than 15 days");
                        progressBarLinear.setProgress(0);
                        progressPercentage.setText("0");
                        progressInfo.setTextColor(getColor(R.color.design_default_color_error));
                        progressInfo.setText("Can not select interval more than 15 days");
                        break;
                    default:
                        Log.i(TAG,"Processing");
                        break;

                }
            }
        });
    }

    private void downloadData() throws IOException, ParseException {
        // Simulate data downloading process

        // Perform DynamoDB query
        ArrayList<DeviceDataDownload> devices = performDynamoDBQuery();
        if(devices.size() == 0){
            state = Download_State.ERROR_QUERY;
            updateProgress(state);
            return;
        }
        // Process query result
        Workbook workbook = generateExcel(devices);
        if(workbook == null){
            state = Download_State.ERROR_EXCEL;
            updateProgress(state);
            return;
        }
        String fileName = "/" + devices.get(0).getDeviceId() + ".xlsx";
        Log.i(TAG,"FileName: " + fileName);
        boolean success = saveExcelFile(workbook, fileName);
        if(!success){
            state = Download_State.ERROR_DOWNLOADING;
            updateProgress(state);
            return;
        }
        // Show result on UI
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Hide progress bar
                layoutProgress.setVisibility(GONE);
                // Enable UI elements
                setUIEnabled(true);
                if (success) {
                    // Show success message
                    Toast.makeText(GenerateReportActivity.this, "Data downloaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Show failure message
                    Toast.makeText(GenerateReportActivity.this, "Failed to download data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private HSSFWorkbook generateExcel(ArrayList<DeviceDataDownload> deviceData) throws IOException {
        state = Download_State.WORKBOOK;
        DeviceDataDownload data = new DeviceDataDownload();
        String deviceName = null;
        updateProgress(state);
        for (DataItem item : deviceList){
            if(Objects.equals(item.getDeviceId(), deviceData.get(0).getDeviceId())){
                deviceName = item.getDeviceName();
                break;
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight((short) 8);
        font.setItalic(true);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);
        Log.i(TAG,"Generating Excel for : " + deviceData.get(0).getDeviceId());
        Sheet sheet = workbook.createSheet("DeviceData");
        Row userRow = sheet.createRow(0);
        userRow.createCell(0).setCellValue("User Name");
        userRow.createCell(1).setCellValue(userAuthModel.getUserName());
        userRow.getCell(0).setCellStyle(style);

        Row emailRow = sheet.createRow(1);
        emailRow.createCell(0).setCellValue("Email");
        emailRow.createCell(1).setCellValue(userAuthModel.getEmail_id());
        emailRow.getCell(0).setCellStyle(style);

        Row locationRow = sheet.createRow(2);
        locationRow.createCell(0).setCellValue("Location");
        locationRow.createCell(1).setCellValue(userAuthModel.getLocation());
        locationRow.getCell(0).setCellStyle(style);

        Row startRow = sheet.createRow(3);
        startRow.createCell(0).setCellValue("Start Date");
        startRow.createCell(1).setCellValue(data.getTime(deviceData.get(0).getTimestamp()));
        startRow.getCell(0).setCellStyle(style);

        Row endRow = sheet.createRow(4);
        endRow.createCell(0).setCellValue("End Date");
        endRow.createCell(1).setCellValue(data.getTime(deviceData.get(deviceData.size()-1).getTimestamp()));
        endRow.getCell(0).setCellStyle(style);

        Row deviceNameRow = sheet.createRow(5);
        deviceNameRow.createCell(0).setCellValue("Device Name");
        deviceNameRow.createCell(1).setCellValue(deviceName);
        deviceNameRow.getCell(0).setCellStyle(style);

        Row deviceIdRow = sheet.createRow(6);
        deviceIdRow.createCell(0).setCellValue("Device Id");
        deviceIdRow.createCell(1).setCellValue(deviceData.get(0).getDeviceId());
        deviceIdRow.getCell(0).setCellStyle(style);

        Row headerRow = sheet.createRow(8);
        headerRow.createCell(0).setCellValue("Device ID");
        headerRow.createCell(1).setCellValue("Timestamp");
        headerRow.createCell(2).setCellValue("Time");
        headerRow.createCell(3).setCellValue("Battery");
        headerRow.createCell(4).setCellValue("Humidity");
        headerRow.createCell(5).setCellValue("Light State 1");
        headerRow.createCell(6).setCellValue("Light State 2");
        headerRow.createCell(7).setCellValue("Light State 3");
        headerRow.createCell(8).setCellValue("Light State 4");
        headerRow.createCell(9).setCellValue("Lux");
        headerRow.createCell(10).setCellValue("Sensor Profile");
        headerRow.createCell(11).setCellValue("Temperature");
        for (int i=0; i<=11; i++){
            headerRow.getCell(i).setCellStyle(style);
        }
        int rowIndex = 9;
        for (DeviceDataDownload device : deviceData) {
            Row row = sheet.createRow(rowIndex);
            Log.i(TAG, "sensor profile: " + device.getSensorProfile());
            row.createCell(0).setCellValue(device.getDeviceId());
            row.createCell(1).setCellValue(device.getTimestamp());
            row.createCell(2).setCellValue(device.getTime(device.getTimestamp()));
            row.createCell(3).setCellValue(device.getBattery());
            row.createCell(4).setCellValue(device.getHumid());
            row.createCell(5).setCellValue(device.getLightState1());
            row.createCell(6).setCellValue(device.getLightState2());
            row.createCell(7).setCellValue(device.getLightState3());
            row.createCell(8).setCellValue(device.getLightState4());
            row.createCell(9).setCellValue(device.getLux());
            row.createCell(10).setCellValue(device.getSensorProfile());
            row.createCell(11).setCellValue(device.getTemp());
            rowIndex++;
        }
        return workbook;
    }

    private boolean saveExcelFile(Workbook workbook, String fileName) {

        try {
            state = Download_State.DOWNLOAD;
            updateProgress(state);
            File downloadsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + fileName);
            FileOutputStream outputStream = new FileOutputStream(downloadsDirectory);
            workbook.write(outputStream);
            outputStream.close();
            return true;
        } catch (IOException e) {
            state = Download_State.ERROR_DOWNLOADING;
            updateProgress(state);
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<DeviceDataDownload> performDynamoDBQuery() throws ParseException {
        DeviceDataDownload deviceDataDownload = new DeviceDataDownload();
        state = Download_State.QUERY;
        updateProgress(state);
        // Prepare query conditions
        Long currentTime = getCurrentTimeEpoch();
        Log.i(TAG, String.valueOf(currentTime));
        Log.i(TAG,spinnerSelect);
        Long elapsedTime = null;
        switch (radioPosition){
            case 0:
                Log.i(TAG,"2 days chosen");
                elapsedTime = getEpochTimeHoursAgo(48);
                Log.i(TAG, String.valueOf(elapsedTime));
                break;
            case 1:
                Log.i(TAG,"1 week chosen");
                elapsedTime = getEpochTimeHoursAgo(168);
                Log.i(TAG, String.valueOf(elapsedTime));
                break;
            case 2:
                Log.i(TAG, "15 days Chosen");
                elapsedTime = getEpochTimeHoursAgo(360);
                Log.i(TAG, String.valueOf(elapsedTime));
                break;
            case 3:
                Log.i(TAG, "custom chosen");
                String dateTimeStart = startYear + "-" + startMonth + "-" + startDay + " " + startHour + ":" + startMinute + ":00";
                String dateTimeEnd = endYear + "-" + endMonth + "-" + endDay + " " + endHour + ":" + endMinute + ":00";
                Log.i(TAG, dateTimeStart);
                Log.i(TAG, dateTimeEnd);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getDefault());
                Date dateStart = df.parse(dateTimeStart);
                Date dateEnd = df.parse(dateTimeEnd);
                assert dateStart != null;
                assert dateEnd != null;
                elapsedTime = (dateStart.getTime())/1000L;
                Log.i(TAG,"startTime: "+ elapsedTime);
                currentTime = (dateEnd.getTime())/1000L;
                Log.i(TAG,"endTime: " + currentTime);
        }

        // Perform query
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#timestamp", "timestamp");

        Map<String, AttributeValue> expressionAttributesValues = new HashMap<>();
        expressionAttributesValues.put(":id", new AttributeValue().withS(spinnerSelect));
        expressionAttributesValues.put(":start_ts", new AttributeValue().withN(String.valueOf(elapsedTime)));
        expressionAttributesValues.put(":end_ts", new AttributeValue().withN(String.valueOf(currentTime)));
        deviceDataDownload.setDeviceId(spinnerSelect);
        long timeDiff = currentTime - elapsedTime;
        Log.i(TAG, String.valueOf(timeDiff));
        long daysInSecs =  361 * 3600;
        if(timeDiff >= daysInSecs){
            state  = Download_State.ERROR_DATE;
//            Toast.makeText(GenerateReportActivity.this, "Cant choose time range more than 15 days", Toast.LENGTH_LONG).show();
            updateProgress(state);
            return new ArrayList<>();
        }
        DynamoDBQueryExpression<DeviceDataDownload> queryExpression = new DynamoDBQueryExpression<DeviceDataDownload>()
                .withHashKeyValues(deviceDataDownload)
                .withRangeKeyCondition("timestamp", new Condition()
                        .withComparisonOperator(ComparisonOperator.BETWEEN)
                        .withAttributeValueList(new AttributeValue().withN(elapsedTime.toString()), new AttributeValue().withN(currentTime.toString())));

        PaginatedQueryList<DeviceDataDownload> result = dynamoDBMapper.query(DeviceDataDownload.class, queryExpression);
        if(result == null) {
            Log.i(TAG,"query error");
            state  = Download_State.ERROR_QUERY;
            updateProgress(state);
            Toast.makeText(GenerateReportActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, String.valueOf(result.size()));
        return new ArrayList<>(result);
    }

    public static long getCurrentTimeEpoch() {
        return Instant.now().getEpochSecond();
    }

    public static long getEpochTimeHoursAgo(int hours) {
        long currentTimeEpoch = getCurrentTimeEpoch();
        long hoursInSeconds = hours * 3600L; // Convert hours to seconds
        return currentTimeEpoch - hoursInSeconds;
    }


    private void setUIEnabled(boolean enabled) {
        radio2Days.setEnabled(enabled);
        radio1Week.setEnabled(enabled);
        radio1Month.setEnabled(enabled);
        radioCustom.setEnabled(enabled);
        btnDownload.setEnabled(enabled);
        endTimePick.setEnabled(enabled);
        endDatePick.setEnabled(enabled);
        startDatePick.setEnabled(enabled);
        endTimePick.setEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String decodeAuthToken(String authToken) throws Exception {
        return JWTUtils.decodeJWT(authToken);
    }


}