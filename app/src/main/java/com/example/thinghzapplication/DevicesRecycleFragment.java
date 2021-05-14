package com.example.thinghzapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thinghzapplication.Utils.KeysUtils;
import com.example.thinghzapplication.Utils.PermissionUtils;
import com.example.thinghzapplication.Utils.SharedPreferanceHelper;
import com.example.thinghzapplication.deviceModel.DataItem;
import com.example.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.example.thinghzapplication.loginModel.UserAuth;
import com.example.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.example.thinghzapplication.retrofitInterface.GetdeviceData;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DevicesRecycleFragment extends Fragment {
    private View view;
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

    public DevicesRecycleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_devices_recycle, container, false);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getActivity());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
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
        deviceListAdapter = new DeviceListAdapter(getActivity(),deviceList);
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                retrofitUpdateData(userAuth.getAuthToken(),deviceStatus,escalation,sensor_profile);
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                    intent.putParcelableArrayListExtra("deviceResponse", (ArrayList<? extends Parcelable>) deviceList);
                    startActivity(intent);
            }
        });
        return view;
    }


    private void retrofitUpdateData(String authToken,String status, Integer escalate, Integer profile) {
        Log.i(TAG,"Device Status call: "+status+"Escalation Call: "+escalate+"Sensor profile Call: "+profile);
        Call<GetDeviceResponseModel> deviceResponseModelCall = getdeviceData.getResponse(authToken,status,escalate,profile);
        deviceResponseModelCall.enqueue(new Callback<GetDeviceResponseModel>() {
            @Override
            public void onResponse(Call<GetDeviceResponseModel> call, Response<GetDeviceResponseModel> response) {
                Log.i(TAG,"size of Data: "+(response.body().getData().size()));
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }else if (!response.body().isSuccess()) {
                    Toast.makeText(getActivity(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }else if (response.body().getData().size() == 0){
                        recyclerView.setVisibility(View.GONE);
                        noFilterData.setVisibility(View.VISIBLE);
                }else{
                    noFilterData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    deviceList = response.body().getData();
                    deviceListAdapter.setDeviceList(deviceList);
                    Toast.makeText(getActivity(), "DeviceId: " + deviceList.get(0).getDeviceId(), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBarRecycler.setVisibility(View.GONE);
                deviceStatus = null;
                sensor_profile = null;
                escalation = null;
            }
            @Override
            public void onFailure(Call<GetDeviceResponseModel> call, Throwable t) {
            }
        });
    }
}