package com.thinghz.thinghzapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thinghz.thinghzapplication.UserAuth.UserAuthModel;
import com.thinghz.thinghzapplication.Utils.JWTUtils;
import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.thinghz.thinghzapplication.Utils.PermissionUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.loginModel.UserAuth;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements EscalationListAdapter.RadioEscalationClickListener,
        DeviceStatusListAdapter.RadioDeviceStatusClickListener,
        SensorProfileListAdapter.RadioSensorProfileClickListener {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Fragment fragment;
    private static final int CAMERA_PERMISSION_REQUEST = 1003;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private FragmentManager fragmentManager;
    AlertDialog.Builder alertbuilder;
    UserAuthModel userAuthModel;
    private ArrayList<DeviceStatusModel> deviceStatusModels = new ArrayList<>();
    private ArrayList<EscalationModel> escalationModels = new ArrayList<>();
    private ArrayList<SensorProfileModel> sensorProfileModels = new ArrayList<>();
    private DeviceStatusListAdapter deviceStatusListAdapter;
    private EscalationListAdapter escalationListAdapter;
    private SensorProfileListAdapter sensorProfileListAdapter;
    RecyclerView deviceStatusRecycler,sensorProfileRecycler,escalationRecycler;
    String device_Status = null;
    Integer escalation = null;
    Integer sensor_profile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonFilter,buttonReset;
        TextView all_devices,dashboard,user, report;
        ImageView iv_device_status,iv_sensor_profile,iv_escalation;
        drawerLayout =findViewById(R.id.drawer_layout);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(this);
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        deviceStatusRecycler = findViewById(R.id.rv_device_status);
        sensorProfileRecycler = findViewById(R.id.rv_sensor_profile);
        escalationRecycler = findViewById(R.id.rv_escalation);

        setUpToolbar();
        setUpDrawer();
        PermissionUtils.requestPermission(MainActivity.this,
                CAMERA_PERMISSION_REQUEST,
                Manifest.permission.CAMERA);

        alertbuilder = new AlertDialog.Builder(this);
        all_devices = findViewById(R.id.tv_all_devices);
        dashboard = findViewById(R.id.tv_dashboard);
        report = findViewById(R.id.tv_report);
        iv_device_status = findViewById(R.id.iv_drop_down_device_status);
        iv_sensor_profile = findViewById(R.id.iv_drop_down_sensor_profile);
        iv_escalation = findViewById(R.id.iv_drop_down_escalation);
        fragmentManager = getSupportFragmentManager();
        user = findViewById(R.id.tv_users);
        buttonFilter = findViewById(R.id.button_Filter);
        buttonReset = findViewById(R.id.button_reset);
        String jsonString;
        String userName, emailId, location;
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

        dashboard.setVisibility(View.GONE);

        all_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new DevicesRecycleFragment();
                Bundle arguments = new Bundle();
                arguments.putString(KeysUtils.getFilter_device_status(),null);
                arguments.putInt(KeysUtils.getFilter_escalation(),-1);
                arguments.putInt(KeysUtils.getFilter_sensor_profile(),-1);
                fragmentTransaction(fragment,arguments);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                String token = task.getResult();
                                Log.d(TAG, token);
                                Toast.makeText(MainActivity.this, "FCM token:" + token, Toast.LENGTH_SHORT).show();
                            }
                        });
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "downloading report", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),GenerateReportActivity.class);
                startActivity(intent);
            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new UserFragment();
                Bundle arguments = new Bundle();
                arguments.putString(KeysUtils.getUser_fragment_bundle_userName(), userAuthModel.getUserName());
                arguments.putString(KeysUtils.getUser_fragment_bundle_email(), userAuthModel.getEmail_id());
                arguments.putString(KeysUtils.getUser_fragment_bundle_location(),userAuthModel.getLocation());
                Log.i(TAG,"arguments: "+arguments);
                fragmentTransaction(fragment,arguments);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        iv_device_status.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;
            @Override
            public void onClick(View view) {
                isOpen =!isOpen;
                deviceStatusRecycler.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });

        iv_sensor_profile.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;
            @Override
            public void onClick(View view) {
                isOpen =!isOpen;
                sensorProfileRecycler.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });

        iv_escalation.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;
            @Override
            public void onClick(View view) {
                isOpen =!isOpen;
                escalationRecycler.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                Log.i(TAG,"is clicked:"+isOpen);
            }
        });


        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new DevicesRecycleFragment();
                Bundle arguments = new Bundle();
                arguments.putString(KeysUtils.getFilter_device_status(),device_Status);
                if(escalation == null){
                    arguments.putInt(KeysUtils.getFilter_escalation(),-1);
                }else{
                    arguments.putInt(KeysUtils.getFilter_escalation(),escalation);
                }
                if(sensor_profile == null){
                    arguments.putInt(KeysUtils.getFilter_sensor_profile(),-1);
                }else{
                    arguments.putInt(KeysUtils.getFilter_sensor_profile(),sensor_profile);
                }
                fragmentTransaction(fragment,arguments);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRecyclerView();
                escalation = null;
                sensor_profile = null;
                device_Status = null;
            }
        });
        createRecyclerView();
        createDeviceStatusList();
        createEscalationList();
        createSensorProfileList();
    }


    public void slideUp(View view){
        TranslateAnimation animate = new TranslateAnimation(
                -500,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                -500,                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(0);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void createRecyclerView() {

        deviceStatusRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        deviceStatusListAdapter = new DeviceStatusListAdapter(deviceStatusModels,this, this);
        deviceStatusRecycler.setAdapter(deviceStatusListAdapter);
        sensorProfileRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        sensorProfileListAdapter = new SensorProfileListAdapter(sensorProfileModels,this, this);
        sensorProfileRecycler.setAdapter(sensorProfileListAdapter);
        escalationRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        escalationListAdapter = new EscalationListAdapter(escalationModels,this,this);
        escalationRecycler.setAdapter(escalationListAdapter);

    }

    private void createDeviceStatusList() {
        deviceStatusModels = new ArrayList<>();
        deviceStatusModels.add(new DeviceStatusModel("online"));
        deviceStatusModels.add(new DeviceStatusModel("offline"));
        deviceStatusListAdapter.setDeviceStatusList(deviceStatusModels);
    }
    private void createEscalationList() {
        escalationModels = new ArrayList<>();
        escalationModels.add(new EscalationModel("0"));
        escalationModels.add(new EscalationModel(">0"));
        escalationModels.add(new EscalationModel("5"));
        escalationListAdapter.setEscalation(escalationModels);
    }
    private void createSensorProfileList() {
        sensorProfileModels = new ArrayList<>();
        sensorProfileModels.add(new SensorProfileModel("Temperature"));
        sensorProfileModels.add(new SensorProfileModel("Temperature and Humidity"));
        sensorProfileListAdapter.setSensorProfile(sensorProfileModels);
    }

    private void fragmentTransaction(Fragment fragment, Bundle arguments) {
        //Start the fragment transaction
        fragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replace the content of frame
        fragmentTransaction.replace(R.id.nav_drawer_fragment, fragment);
        //commit the transaction
        fragmentTransaction.commit();
    }


    private String decodeAuthToken(String authToken) throws Exception {
        return JWTUtils.decodeJWT(authToken);
    }

    private void setUpDrawer() {
        drawerLayout =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSION_REQUEST, grantResults)) {
                Toast.makeText(MainActivity.this, "Permission granted for Camera", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_logout,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.item_logout:
                alertbuilder.setMessage("Do you want to logout from application?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferanceHelper.getInstance(MainActivity.this).removeToken(true);
                                Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
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
                break;

            case R.id.item_user:{

                Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                intent.putExtra(KeysUtils.getUser_fragment_bundle_userName(),userAuthModel.getUserName());
                intent.putExtra(KeysUtils.getUser_fragment_bundle_email(),userAuthModel.getEmail_id());
                intent.putExtra(KeysUtils.getUser_fragment_bundle_location(),userAuthModel.getLocation());
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "back button clicked");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onRadioDeviceStatusClick(String selected) {
        this.device_Status = selected;
    }

    @Override
    public void onRadioEscalationClick(String selected) {
        switch (selected){
            case "0":
                this.escalation = 0;
                break;
            case ">0":
                this.escalation = 1;
                break;
            case "5":
                this.escalation = 5;
                break;
            default:
                this.escalation = 0;
        }
    }

    @Override
    public void onRadioSensorProfileClick(String selected) {
        switch (selected) {
            case "Temperature":
                this.sensor_profile = 2;
                break;
            case "Temperature and Humidity":
                this.sensor_profile = 3;
                break;
            case "Gas":
                this.sensor_profile = 4;
                break;
            case "Gyro and Accel":
                this.sensor_profile = 5;
                break;
            case "Moisture":
                this.sensor_profile = 6;
                break;
            case "Control":
                this.sensor_profile = 7;
                break;
            case "Capacitance":
                this.sensor_profile = 8;
                break;
            default:
                this.sensor_profile = 3; 
        }
    }


}