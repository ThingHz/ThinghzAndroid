package com.example.thinghzapplication;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.thinghzapplication.Utils.NetworkUtils;
import com.example.thinghzapplication.Utils.SharedPreferanceHelper;
import com.example.thinghzapplication.deviceModel.DataItem;
import com.example.thinghzapplication.deviceModel.GetDeviceResponseModel;
import com.example.thinghzapplication.loginModel.UserAuth;
import com.example.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.example.thinghzapplication.retrofitInterface.GetdeviceData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    View view;
    private Retrofit retrofit;
    private List<DataItem> dataItemList;
    private static String TAG = "HomeFragment";
    GetdeviceData getdeviceData;
    String jwtAuthToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getActivity());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        Log.i(TAG,"isFirstUser:"+ UserAuth.getIsFirstUser());
        Log.i(TAG,"authToken: "+ UserAuth.getAuthToken());
        if(userAuth.getIsFirstUser()){
            boolean isFirstUser = false;
            userAuth.setIsFirstUser(isFirstUser);
            sharedPreferanceHelper.saveUserInfo(userAuth);
            Log.i(TAG,"is First User going to Login Activity");
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
        }

        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        getdeviceData = retrofit.create(GetdeviceData.class);
        jwtAuthToken = userAuth.getAuthToken();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                checkRetrofitApi(jwtAuthToken);
                Log.i(TAG,"Checking for API");
            }
        };
        if (jwtAuthToken == null){
            jwtAuthToken = "eyJhbGciOiJIUzI1NiIsInR5";
        }
        if (NetworkUtils.isNetworkAvailable(getActivity())){
            checkRetrofitApi(jwtAuthToken);
        }else{
            Log.i(TAG,"No Network available");
            Toast.makeText(getActivity(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
            handler.postDelayed(r,10000);
        }
        return view;
    }

    private void checkRetrofitApi(String authToken){
        Call<GetDeviceResponseModel> deviceResponseModelCall = getdeviceData.getResponse(authToken);
        deviceResponseModelCall.enqueue(new Callback<GetDeviceResponseModel>() {
            @Override
            public void onResponse(Call<GetDeviceResponseModel> call, Response<GetDeviceResponseModel> response) {
                try {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
                    }
                    if (!response.body().isSuccess()) {
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
                    }else{
                        dataItemList = response.body().getData();
                        DataItem dataItem = dataItemList.get(0);
                        Toast.makeText(getActivity(), "deviceId" + dataItem.getDeviceId().toString() + "batteryValue" + dataItem.getBattery().toString(), Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
                    }
                }catch(NullPointerException exception){
                    Log.i(TAG,"Null Exception"+exception);
                }
            }

            @Override
            public void onFailure(Call<GetDeviceResponseModel> call, Throwable t) {

            }
        });
    }
}