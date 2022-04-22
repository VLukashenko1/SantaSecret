package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.Collections;
import java.util.List;

public class Friends extends AppCompatActivity {
    DbHelper dbHelper = new DbHelper();

TextView friendsCounter;
ImageView ellipseFriends;
ImageButton addFriendsButton;
ListView frList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsCounter = findViewById(R.id.friendsCounter);
        ellipseFriends = findViewById(R.id.elipseFriends);
        ellipseFriends.setVisibility(View.INVISIBLE);
        friendsCounter.setVisibility(View.INVISIBLE);

        addFriendsButton = findViewById(R.id.addFriendsButtonFriendsAct);
        addFriendsButton.setOnClickListener(View ->  startActivity(new Intent(Friends.this, FindFriends.class)));

        frList = findViewById(R.id.listViewFriendsAct);

        UserHolder.getInstance().getLiveUser().observe(this, user -> {
            showFriends(user);
            showFriendsRequest(user);
        });
    }

    void showFriends(User user){
        if (user == null || user.getFriends() == null){
            return;
        }
        for (int i = 0; i < user.getFriends().size(); i++){

        }
    }
    void showFriendsRequest(User user){
        if (user.getRequestToFriends() == null){
            return;
            }
        friendsCounter.setVisibility(View.VISIBLE);
        ellipseFriends.setVisibility(View.VISIBLE);

        friendsCounter.setText(Integer.toString(user.getRequestToFriends().size()));

        ellipseFriends.setOnClickListener(view -> findFriendsRequestNames(user.getRequestToFriends()));
    }
    void findFriendsRequestNames(List<String> requests){

    }
}