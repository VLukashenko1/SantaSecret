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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.BoxesHolder;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.UserBoxesFinder;

import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView userInfo;
ImageView userPhoto;
ImageButton createBox;
ListView boxesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = findViewById(R.id.userInfo);
        userPhoto = findViewById(R.id.userPhotoMainPage);

        createBox = findViewById(R.id.plusButtonMainPage);
        createBox.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CreateBox.class)));

        boxesListView = findViewById(R.id.boxListViewOnMainPage);

//
        UserHolder.getInstance().getLiveUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                showUserInfo(user);
            }
        });
//
        boxesFinder();

//
    }

    void showUserInfo(User user){
        userInfo.setText("Name: " + user.getDisplayName() + "\nE-mail: " + user.getEmail());
        Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(userPhoto);
    }
    void boxesFinder(){
        UserBoxesFinder ubf = new UserBoxesFinder();
        ubf.startBackgroundFindingBox();
        BoxesHolder.getInstance().getLiveBoxes().observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                boxListFiller(boxes);
            }
        });
    }

    void boxListFiller(List<Box> boxes){
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, boxNamesList(boxes));
        boxesListView.setAdapter(adapter);
    }
    String[] boxNamesList(List<Box> boxes){
        String[] boxNamesArray = new String[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) {
            boxNamesArray[i] = boxes.get(i).getNameOfBox();
        }
        return boxNamesArray;
    }

    void makeText(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}