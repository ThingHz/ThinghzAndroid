package com.thinghz.thinghzapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.deleteDeviceModel.DeleteDeviceModel;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.thinghz.thinghzapplication.loginModel.UserAuth;
import com.thinghz.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.thinghz.thinghzapplication.retrofitInterface.DeleteDeviceInterface;
import com.thinghz.thinghzapplication.retrofitInterface.GetdeviceData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DevicesRecycleFragment extends Fragment implements DeviceListAdapter.DeleteClickListener{
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    List<DataItem> deviceList = new ArrayList<>();
    DeviceListAdapter deviceListAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    GetdeviceData getdeviceData;
    ProgressBar progressBarRecycler;
    TextView noFilterData;
    private final String TAG = DevicesRecycleFragment.this.getClass().getSimpleName();
    ExtendedFloatingActionButton mFloatingActionButton;
    String deviceStatus = null;
    Integer escalation = null;
    Integer sensor_profile = null;
    UserAuth userAuth;

    public DevicesRecycleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_devices_recycle, container, false);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getActivity());
        userAuth = sharedPreferanceHelper.getUserSavedValue();
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        Bundle arguments = getArguments();

        if(arguments != null){
            deviceStatus = arguments.getString(KeysUtils.getFilter_device_status());
            Log.i(TAG,"userName: "+ deviceStatus);
            escalation = arguments.getInt(KeysUtils.getFilter_escalation());
            Log.i(TAG,"escalation: "+escalation);
            if(escalation == -1){
                escalation = null;
            }
            sensor_profile = arguments.getInt(KeysUtils.getFilter_sensor_profile());
            Log.i(TAG,"sensor_profile: "+sensor_profile);
            if(sensor_profile == -1){
                sensor_profile = null;
            }
        }else{
            Log.i(TAG,"arguments are null");
        }
        noFilterData = view.findViewById(R.id.tv_no_fiter_data);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getdeviceData = retrofit.create(GetdeviceData.class);
        recyclerView = view.findViewById(R.id.id_recycler_devices);
        mFloatingActionButton = view.findViewById(R.id.fab_add_Device);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        progressBarRecycler = view.findViewById(R.id.progress_bar_recycler);
        retrofitUpdateData(userAuth.getAuthToken(),deviceStatus,escalation,sensor_profile);
        deviceListAdapter = new DeviceListAdapter(getActivity(),deviceList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(deviceListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });
        progressBarRecycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(()->{
            swipeRefreshLayout.setRefreshing(true);
            retrofitUpdateData(userAuth.getAuthToken(),deviceStatus,escalation,sensor_profile);

        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                    intent.putParcelableArrayListExtra("deviceResponse", (ArrayList<? extends Parcelable>) deviceList);
                    startActivity(intent);
            }
        });
        refreshCardData(userAuth.getAuthToken(),deviceStatus,escalation,sensor_profile);
        return view;
    }

    private void refreshCardData(String authToken, String deviceStatus, Integer escalation, Integer sensor_profile) {
        retrofitUpdateData(authToken,deviceStatus,escalation,sensor_profile);
        refreshHandler(300);
    }

    private void refreshHandler(int refreshSecs) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refreshCardData(userAuth.getAuthToken(),deviceStatus,escalation,sensor_profile);
            }
        };
        handler.postDelayed(runnable,refreshSecs*1000);
    }


    private void retrofitUpdateData(String authToken,String status, Integer escalate, Integer profile) {
        Log.i(TAG,"Device Status call: "+status+"Escalation Call: "+escalate+"Sensor profile Call: "+profile);
        Call<GetDeviceResponseModel> deviceResponseModelCall = getdeviceData.getResponse(authToken,status,escalate,profile);
        deviceResponseModelCall.enqueue(new Callback<GetDeviceResponseModel>() {
            @Override
            public void onResponse(Call<GetDeviceResponseModel> call, Response<GetDeviceResponseModel> response) {

                if (response.code() != 200) {
                     Log.e(TAG,"response is not 200");
                }else if (response.body() != null) {
                    if (!response.body().isSuccess()) {
                        Log.e(TAG, "response body is not available");
                    }else if (response.body().getData().size() == 0){
                        Log.e(TAG, "no response available");
                            recyclerView.setVisibility(View.GONE);
                            noFilterData.setVisibility(View.VISIBLE);
                    }else{
                        noFilterData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        deviceList = response.body().getData();
                        deviceListAdapter.setDeviceList(deviceList);
                        Log.i(TAG, "DeviceId: " + deviceList.get(0).getDeviceId());
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBarRecycler.setVisibility(View.GONE);
                deviceStatus = null;
                sensor_profile = null;
                escalation = null;
            }
            @Override
            public void onFailure(Call<GetDeviceResponseModel> call, Throwable t) {
                Log.e(TAG, "could not get response");
            }
        });
    }

    @Override
    public void onDeleteClick(String deviceId, Integer escalation) {
            retrofitDeleteData(userAuth.getAuthToken(),escalation,deviceId);
    }

    @Override
    public void onSettingClick(Integer position) {
        Intent intent = new Intent(getActivity(),SettingsActivity.class);
        intent.putExtra("DeviceData",new Gson().toJson(deviceList.get(position)));
        startActivity(intent);
    }

    @Override
    public void onLightClick(Integer position) {
        Intent intent = new Intent(getActivity(), LightActivity.class);
        intent.putExtra("DeviceData",new Gson().toJson(deviceList.get(position)));
        startActivity(intent);
    }

    private void retrofitDeleteData(String authToken, Integer escalation, String deviceId) {
        DeleteDeviceInterface deleteDeviceInterface = retrofit.create(DeleteDeviceInterface.class);
        Call<DeleteDeviceModel> deviceResponseModelCall = deleteDeviceInterface.getResponse(authToken,deviceId,escalation);
        deviceResponseModelCall.enqueue(new Callback<DeleteDeviceModel>() {
            @Override
            public void onResponse(Call<DeleteDeviceModel> call, Response<DeleteDeviceModel> response) {
                if (response.code() != 200) {
                    Log.e(TAG, "Error: " + response.code());
                }else if (!response.body().isSuccess()) {
                    Log.e(TAG, "Error: " + response.body().getMessage());
                }else{
                    Log.e(TAG, "Error: " + response.body().getMessage());
                }
            }
            @Override
            public void onFailure(Call<DeleteDeviceModel> call, Throwable t) {
            }
        });
    }
}