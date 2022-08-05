package com.example.thinghzapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.internal.rtl.RtlTextView;
import com.example.thinghzapplication.Utils.KeysUtils;

public class UserFragment extends Fragment {

    View view;
    TextView tv_user_name,tv_location,tv_email_id;
    ImageView iv_user_profile;
    private static final String TAG = "userFragment";

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle arguments = getArguments();
        String userName = arguments.getString(KeysUtils.getUser_fragment_bundle_userName());
        Log.i(TAG,"userName: "+ userName);
        String location = arguments.getString(KeysUtils.getUser_fragment_bundle_location());
        Log.i(TAG,"location: "+location);
        String email_id = arguments.getString(KeysUtils.getUser_fragment_bundle_email());
        Log.i(TAG,"email_id: "+email_id);



        tv_user_name =  view.findViewById(R.id.tv_userName_value);
        tv_location = view.findViewById(R.id.tv_location_value);
        tv_email_id = view.findViewById(R.id.tv_email_value);
        iv_user_profile = view.findViewById(R.id.iv_user_profile);
        tv_user_name.setText(userName);
        tv_location.setText(location);
        tv_email_id.setText(email_id);
        iv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),userName, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}