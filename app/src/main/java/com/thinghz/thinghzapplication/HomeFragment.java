package com.thinghz.thinghzapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.thinghz.thinghzapplication.UserAuth.UserAuthModel;
import com.thinghz.thinghzapplication.Utils.JWTUtils;
import com.thinghz.thinghzapplication.Utils.NetworkUtils;
import com.thinghz.thinghzapplication.Utils.SharedPreferanceHelper;
import com.thinghz.thinghzapplication.loginModel.UserAuth;

import org.json.JSONObject;


public class HomeFragment extends Fragment {

    View view;
    private static String TAG = "HomeFragment";
    UserAuthModel userAuthModel;
    ImageView logo_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if (!NetworkUtils.isNetworkAvailable(getActivity())){
            Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
        }
        logo_view = view.findViewById(R.id.fragment_home_id);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Check for Auth Token
                if (!NetworkUtils.isNetworkAvailable(getActivity())){
                    Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    handler.postDelayed(this,1500);
                }else{
                    Log.d("Handler", "Running Handler");
                    navigateUser();
                }
            }
        }, 3000);

        return view;
    }

    private void navigateUser() {
        SharedPreferanceHelper sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getActivity());
        UserAuth userAuth = sharedPreferanceHelper.getUserSavedValue();
        boolean isFirstUser = userAuth.getIsFirstUser();
        String authToken = userAuth.getAuthToken();
        Log.i(TAG,"isFirstUser:"+ isFirstUser);
        Log.i(TAG,"authToken: "+ authToken);
        if(!sharedPreferanceHelper.isFirstUserExists()){
            userAuth.setIsFirstUser(false);
            sharedPreferanceHelper.saveUserInfo(userAuth);
            Log.i(TAG,"is First User going to Login Activity");
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
        }else if (!sharedPreferanceHelper.isAuthTokenExists()){
            Log.i(TAG,"auth token does not exists go to Login");
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
        }else{
            try {
                String jsonString,userName,emailId,location;
                long issuedAt,expireAt;
                jsonString = decodeAuthToken(userAuth.getAuthToken());
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
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }
    }


    private String decodeAuthToken(String authToken) throws Exception {
        return JWTUtils.decodeJWT(authToken);
    }

}