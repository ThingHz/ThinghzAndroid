package com.thinghz.thinghzapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thinghz.thinghzapplication.Utils.KeysUtils;

public class UserActivity extends AppCompatActivity {

    View view;
    TextView tv_user_name,tv_location,tv_email_id;
    ImageView iv_user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("User");
        String userName = getIntent().getStringExtra(KeysUtils.getUser_fragment_bundle_userName());
        String email_id = getIntent().getStringExtra(KeysUtils.getUser_fragment_bundle_email());
        String location = getIntent().getStringExtra(KeysUtils.getUser_fragment_bundle_location());

        tv_user_name =  findViewById(R.id.tv_userName_value_1);
        tv_location = findViewById(R.id.tv_location_value_1);
        tv_email_id = findViewById(R.id.tv_email_value_1);
        iv_user_profile = findViewById(R.id.iv_user_profile_1);
        tv_user_name.setText(userName);
        tv_location.setText(location);
        tv_email_id.setText(email_id);
        iv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserActivity.this,userName, Toast.LENGTH_SHORT).show();
            }
        });
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