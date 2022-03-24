package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Model.UserHolder;

public class MainActivity extends AppCompatActivity {
TextView userInfo;
ImageView userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = findViewById(R.id.userInfo);
        userPhoto = findViewById(R.id.userPhotoMainPage);

        UserHolder.getInstance().getLiveUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                showUserInfo(user);
            }
        });

    }

    void showUserInfo(User user){
        userInfo.setText("Name: " + user.getDisplayName() + "\nE-mail: " + user.getEmail());
        Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(userPhoto);

    }
}