package com.example.thinghzapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.thinghzapplication.Utils.NetworkUtils;
import com.example.thinghzapplication.Utils.SharedPreferanceHelper;
import com.example.thinghzapplication.loginModel.LoginBodyModel;
import com.example.thinghzapplication.loginModel.LoginResponseModel;
import com.example.thinghzapplication.loginModel.UserAuth;
import com.example.thinghzapplication.retrofitBuilder.RetrofitApiBuilder;
import com.example.thinghzapplication.retrofitInterface.LoginInterface;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

   View view;
   ProgressBar progressBar;
   String userName;
   String password;
   Animation anim_login_Button,anim_user,anim_pass;
   EditText edit_user,edit_Pass;
   TextInputLayout user_layout,pass_layout;
   TextView forgot_password;
   private Retrofit retrofit;
   private static String TAG = "LoginFragment";
   LoginInterface loginInterface;
   SharedPreferanceHelper sharedPreferanceHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        sharedPreferanceHelper = SharedPreferanceHelper.getInstance(getActivity());
        anim_login_Button = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_login_btn);
        anim_user = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_login_user);
        anim_pass = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_login_pass);
        Button button = view.findViewById(R.id.loginButton);
        button.setAnimation(anim_login_Button);
        edit_user = view.findViewById(R.id.edit_text_email_cred);
        edit_Pass = view.findViewById(R.id.edit_text_pass_cred);
        progressBar = view.findViewById(R.id.progress_bar_login);
        forgot_password = view.findViewById(R.id.forgot_password);
        user_layout = view.findViewById(R.id.edit_text_email_cred_layout);
        pass_layout = view.findViewById(R.id.edit_text_pass_cred_layout);
        user_layout.setAnimation(anim_user);
        pass_layout.setAnimation(anim_pass);
        edit_user.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edit_Pass.setImeOptions(EditorInfo.IME_ACTION_DONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = edit_user.getText().toString();
                password = edit_Pass.getText().toString();
                if (NetworkUtils.validateUserData(userName,password,getActivity())){
                    if(NetworkUtils.isNetworkAvailable(getActivity())){
                        progressBar.setVisibility(View.VISIBLE);
                        retrofotLoginApi(userName,password);
                    }else{
                        Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForgotPasswordFragment();
            }
        });
        return view;
    }

    private void goToForgotPasswordFragment() {
        Toast.makeText(getActivity(), "Go to forgot password fragment", Toast.LENGTH_SHORT).show();
    }

    private void retrofotLoginApi(String userId, String password){
        RetrofitApiBuilder retrofitApiBuilder = new RetrofitApiBuilder(retrofit);
        retrofit = retrofitApiBuilder.getRetrofitClient();
        loginInterface = retrofit.create(LoginInterface.class);
        final LoginBodyModel login = new LoginBodyModel(userId, password);
        Call<LoginResponseModel> loginResponseModelCall = loginInterface.createUser(login);
        loginResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.code() != 200){
                    Toast.makeText(getActivity(),"Error:"+response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body().isSuccess()){
                    String authToken = response.body().getToken();
                    UserAuth userAuth = new UserAuth(authToken,false);
                    sharedPreferanceHelper.saveUserInfo(userAuth);
                    Toast.makeText(getActivity(),"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }else{
                    forgot_password.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),response.body().getError(),Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });
    }

}