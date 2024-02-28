package com.thinghz.thinghzapplication;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitInterface.PublishInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Retrofit;

public class LightActivity extends AppCompatActivity {

    private static final String TAG = "Light Activity";
    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a26dm966t9g0lv-ats";
    private static final String COGNITO_POOL_ID = "us-east-1:3cf54d60-82e7-4cfc-a742-646dafc2e565";
    private static final Regions MY_REGION = Regions.US_EAST_1;
    private static final String AWS_IOT_POLICY_NAME = "arn:aws:iot:us-east-1:307339013420:policy/thing_test-Policy";
    private static final String KEYSTORE_NAME = "thinghz.bks";
    private static final String light_topic  = "aws/thing/thinghz/";
    // Password for the private key in the KeyStore
    private static final String KEYSTORE_PASSWORD = "thinghz";
    // Certificate and key aliases in the KeyStore
    private static final String CERTIFICATE_ID = "thinghz";
    SeekBar light_seekbar1,light_seekbar2, light_seekbar3,light_seekbar4;
    Button mqtt_connect;
    SwitchMaterial light_switch1,light_switch2, light_switch3, light_switch4;
    CheckBox light_update_checkbox,light_delete_checkbox;
    Button light_update_settings;
    LinearLayout layout_time_picker;
    ImageView light_pick_on_time, light_pick_off_time;
    TextView light_pick_on_label, light_pick_off_label, tvStatus;
    ProgressBar progress_bar;
    private Retrofit retrofit;
    PublishInterface publishInterface;
    private DataItem dataItem;
    private static String deviceName,deviceId;
    UserAuth userAuth;
    AWSIotClient mIotAndroidClient;
    AWSIotMqttManager mqttManager;
    boolean isAckReceived = false;

    String clientId;
    String keystorePath;
    String keystoreName;
    String keystorePassword;

    KeyStore clientKeyStore = null;
    String certificateId;

    CognitoCachingCredentialsProvider credentialsProvider;

    int light_state_1 = 0;
    int light_state_2 = 0;
    int light_state_3 = 0;
    int light_state_4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataItem = new Gson().fromJson(getIntent().getStringExtra("DeviceData"), DataItem.class);
        deviceName = dataItem.getDeviceName();
        light_state_1 = dataItem.getLightState1();
        light_state_2 = dataItem.getLightState2();
        Log.i(TAG,"Light_State1:"+ light_state_1);
        Log.i(TAG,"Light_State2:"+ light_state_2);
        light_state_3 = dataItem.getLightState3();
        light_state_4 = dataItem.getLightState4();
        Log.i(TAG,"Light_State1:"+ light_state_3);
        Log.i(TAG,"Light_State2:"+ light_state_4);
        setTitle(deviceName);
        Toast.makeText(LightActivity.this,"light State:"+ light_state_1,Toast.LENGTH_LONG).show();
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(LightActivity.this);
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        light_seekbar1 = findViewById(R.id.sb_light1_int);
        light_seekbar2 = findViewById(R.id.sb_light2_int);
        light_seekbar3 = findViewById(R.id.sb_light3_int);
        light_seekbar4 = findViewById(R.id.sb_light4_int);
        light_switch1 = findViewById(R.id.sw_light1_state);
        light_switch2 = findViewById(R.id.sw_light2_state);
        light_switch3 = findViewById(R.id.sw_light3_state);
        light_switch4 = findViewById(R.id.sw_light4_state);
        light_update_checkbox = findViewById(R.id.cb_update_light_event);
        light_delete_checkbox = findViewById(R.id.cb_delete_light_event);
        light_update_settings = findViewById(R.id.bt_light_update);
        layout_time_picker = findViewById(R.id.ll_time_pick);
        light_pick_on_time = findViewById(R.id.iv_pick_time_on);
        light_pick_off_time = findViewById(R.id.iv_pick_time_off);
        light_pick_on_label = findViewById(R.id.tv_time_event_on_value);
        light_pick_off_label = findViewById(R.id.tv_time_event_off_value);
        progress_bar = findViewById(R.id.light_progress_bar);
        mqtt_connect = findViewById(R.id.bt_mqtt_connect);
        tvStatus = findViewById(R.id.tv_mqtt_connect_status);
        progress_bar.setVisibility(View.GONE);
        layout_time_picker.setVisibility(View.GONE);
        light_update_settings.setVisibility(View.GONE);
        light_switch1.setChecked(light_state_1 != 0);
        light_switch2.setChecked(light_state_2 != 0);
        light_switch3.setChecked(light_state_3 != 0);
        light_switch4.setChecked(light_state_4 != 0);

        // Initialize the AWS Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // context
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );

        Region region = Region.getRegion(MY_REGION);
        keystorePath = getFilesDir().getPath();
        keystoreName = KEYSTORE_NAME;
        keystorePassword = KEYSTORE_PASSWORD;
        certificateId = CERTIFICATE_ID;
        clientId = UUID.randomUUID().toString();
        // MQTT Client
        mqttManager = new AWSIotMqttManager(clientId, region, CUSTOMER_SPECIFIC_ENDPOINT);
        mqttManager.setKeepAlive(10);

        // IoT Client (for creation of certificate if needed)
        mIotAndroidClient = new AWSIotClient(credentialsProvider);
        mIotAndroidClient.setRegion(region);


        try {
            if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, keystoreName)) {
                if (AWSIotKeystoreHelper.keystoreContainsAlias(certificateId, keystorePath,
                        keystoreName, keystorePassword)) {
                    Log.i(TAG, "Certificate " + certificateId
                            + " found in keystore - using for MQTT.");
                    // load keystore from file into memory to pass on connection
                    clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                            keystorePath, keystoreName, keystorePassword);
                    mqtt_connect.setEnabled(true);
                } else {
                    Log.i(TAG, "Key/cert " + certificateId + " not found in keystore.");
                }
            } else {
                Log.i(TAG, "Keystore " + keystorePath + "/" + keystoreName + " not found.");
            }
        } catch (Exception e) {
            Log.e(TAG, "An error occurred retrieving cert/key from keystore.", e);
        }

        if (clientKeyStore == null) {
            Log.i(TAG, "Cert/key was not found in keystore - creating new key and certificate.");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create a new private key and certificate. This call
                        // creates both on the server and returns them to the
                        // device.
                        CreateKeysAndCertificateRequest createKeysAndCertificateRequest = new CreateKeysAndCertificateRequest();
                        createKeysAndCertificateRequest.setSetAsActive(true);
                        final CreateKeysAndCertificateResult createKeysAndCertificateResult = mIotAndroidClient
                                .createKeysAndCertificate(createKeysAndCertificateRequest);
                        Log.i(TAG,
                                "Cert ID: " + createKeysAndCertificateResult.getCertificateId()
                                        + " created.");

                        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(certificateId,
                                createKeysAndCertificateResult.getCertificatePem(),
                                createKeysAndCertificateResult.getKeyPair().getPrivateKey(),
                                keystorePath, keystoreName, keystorePassword);

                        // load keystore from file into memory to pass on
                        // connection
                        clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                                keystorePath, keystoreName, keystorePassword);

                        // Attach a policy to the newly created certificate.
                        // This flow assumes the policy was already created in
                        // AWS IoT and we are now just attaching it to the
                        // certificate.
                        AttachPrincipalPolicyRequest policyAttachRequest = new AttachPrincipalPolicyRequest();
                        policyAttachRequest.setPolicyName(AWS_IOT_POLICY_NAME);
                        policyAttachRequest.setPrincipal(createKeysAndCertificateResult
                                .getCertificateArn());
                        mIotAndroidClient.attachPrincipalPolicy(policyAttachRequest);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mqtt_connect.setEnabled(true);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred when generating new private key and certificate.",
                                e);
                    }
                }
            }).start();
        }

        mqtt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clientId = " + clientId);
                try {
                    if(clientKeyStore != null) {
                        progress_bar.setVisibility(View.VISIBLE);
                        mqttManager.connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
                            @Override
                            public void onStatusChanged(final AWSIotMqttClientStatus status,
                                                        final Throwable throwable) {
                                Log.d(TAG, "Status = " + status);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (status == AWSIotMqttClientStatus.Connecting) {
                                            tvStatus.setText("Connecting...");

                                        } else if (status == AWSIotMqttClientStatus.Connected) {
                                            tvStatus.setText("Connected to AWS IoT Endpoint " + CUSTOMER_SPECIFIC_ENDPOINT);
                                            try {
                                                String subscribe_topic =  makeSubscribeTopic();
                                                Log.i(TAG,"Subscribing To Topic"+subscribe_topic);
                                                mqttManager.subscribeToTopic(subscribe_topic, AWSIotMqttQos.QOS0,
                                                        new AWSIotMqttNewMessageCallback() {
                                                            @Override
                                                            public void onMessageArrived(final String topic, final byte[] data) {
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Log.i(TAG,"data arrived from topic"+topic);
                                                                        try {
                                                                            String message = new String(data, "UTF-8");
                                                                            final JSONObject subObject = new JSONObject(message);
                                                                            String status = subObject.getString("Status");
                                                                            Toast.makeText(LightActivity.this,"Light turned "+ status,Toast.LENGTH_LONG).show();
                                                                            progress_bar.setVisibility(View.GONE);
                                                                        } catch (UnsupportedEncodingException | JSONException e) {
                                                                            Toast.makeText(LightActivity.this,"Invalid JSON",Toast.LENGTH_LONG).show();
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                });
                                                            }
                                                        });
                                            } catch (Exception e) {
                                                Log.e(TAG, "Subscription error.", e);
                                            }
                                            light_update_settings.setVisibility(View.VISIBLE);
                                            progress_bar.setVisibility(View.GONE);
                                        } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                                            progress_bar.setVisibility(View.VISIBLE);
                                            if (throwable != null) {
                                                Log.e(TAG, "Connection error.", throwable);
                                            }
                                            tvStatus.setText(R.string.mqtt_reconnection_status);
                                        } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                                            progress_bar.setVisibility(View.VISIBLE);
                                            if (throwable != null) {
                                                Log.e(TAG, "Connection error.", throwable);
                                            }
                                            light_update_settings.setVisibility(View.GONE);
                                            tvStatus.setText(R.string.mqtt_disconnected_status);
                                        } else {
                                            tvStatus.setText(R.string.mqtt_disconnected_status);
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Log.d(TAG, "client key store is empty");
                    }
                } catch (final Exception e) {
                    Log.e(TAG, "Connection error.", e);
                    tvStatus.setText("Error! " + e.getMessage());
                }
            }
        });

        light_update_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    layout_time_picker.setVisibility(View.VISIBLE);
                }else layout_time_picker.setVisibility(View.GONE);
            }
        });
        light_pick_off_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog_light1_off;
                mTimePickerDialog_light1_off = new TimePickerDialog(LightActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.i(TAG, "Light1 off time = "+ selectedHour + ":" + selectedMinute);
                        light_pick_off_label.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour,minute,true);
                mTimePickerDialog_light1_off.setTitle("Pick Light 1 Off Time");
                mTimePickerDialog_light1_off.show();
            }
        });
        light_pick_on_time.setOnClickListener(new View.OnClickListener() {
            final Calendar mCurrentTime = Calendar.getInstance();
            final int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mCurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePickerDialog_light1_on;
            @Override
            public void onClick(View view) {
                mTimePickerDialog_light1_on = new TimePickerDialog(LightActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.i(TAG, "Light1 on time = " + selectedHour + ":" + selectedMinute);
                        light_pick_on_label.setText(selectedHour + ":" + selectedMinute);
                    }
                },hour,minute,true);
                mTimePickerDialog_light1_on.setTitle("Pick Light 1 Off Time");
                mTimePickerDialog_light1_on.show();
            }
        });

        light_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    light_state_1 = 0;
                }else  light_state_1 = 1;
            }
        });

        light_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    light_state_2 = 0;
                }else  light_state_2 = 1;

            }
        });

        light_switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    light_state_3 = 0;
                }else  light_state_3 = 1;

            }
        });

        light_switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    light_state_4 = 0;
                }else  light_state_4 = 1;

            }
        });
        
        

        light_update_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    progress_bar.setVisibility(View.VISIBLE);
                    JSONObject publishObject = new JSONObject();
                    publishObject.put("light_state_1",light_state_1);
                    publishObject.put("light_state_2",light_state_2);
                    publishObject.put("light_state_3",light_state_3);
                    publishObject.put("light_state_4",light_state_4);
                    publishObject.put("light_thresh",800);
                    String payloadToPublish = publishObject.toString();
                    String publish_topic = makePublishTopic();
                    Log.i(TAG,"publishing to topic" + publish_topic);
                    mqttManager.publishString(payloadToPublish, publish_topic , AWSIotMqttQos.QOS0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String makeSubscribeTopic(){
        deviceId = dataItem.getDeviceId();
        return LightActivity.light_topic +deviceId+"/ack";
    }

    String makePublishTopic(){
        deviceId = dataItem.getDeviceId();
        return LightActivity.light_topic +deviceId+"/light";
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