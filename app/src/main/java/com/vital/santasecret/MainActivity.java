package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
FirebaseAuth auth =  FirebaseAuth.getInstance();
TextView userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = findViewById(R.id.textView);
        showUserInfo();
    }

    void showUserInfo(){
        if (auth.getCurrentUser() != null){
            userInfo.setText(auth.getCurrentUser().getDisplayName() + "\n" + auth.getCurrentUser().getEmail());
        }
        else {
            userInfo.setText("You are not authorized");
        }
    }
}