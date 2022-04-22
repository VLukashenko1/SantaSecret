package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.BoxesHolder;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.InBoxUsersFinder;
import com.vital.santasecret.WorkWithDB.UserBoxesFinder;

import java.util.List;

public class MainActivity extends AppCompatActivity {
Button createBox, friendsButton;
ListView boxesListView;

    InBoxUsersFinder inBoxUsersFinder = new InBoxUsersFinder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBox = findViewById(R.id.addBoxMain);
        createBox.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CreateBox.class)));
        friendsButton = findViewById(R.id.friendsMain);
        friendsButton.setOnClickListener(view -> startActivity(new Intent(this,Friends.class)));
        boxesListView = findViewById(R.id.boxListViewOnMainPage);

//
        UserHolder.getInstance().getLiveUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                showUserInfo(user);
            }
        });
        boxesFinder();
//

    }

    void showUserInfo(User user){
        //userInfo.setText("Name: " + user.getDisplayName() + "\nE-mail: " + user.getEmail());
        //Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(userPhoto);
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
        boxesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Box box = boxes.get(i);
                inBoxUsersFinder.getListWithIdOfUsers(box.getIdOfBox());
                Intent intent = new Intent(MainActivity.this, InBoxActivity.class);
                intent.putExtra("nameOfBox", box.getNameOfBox());
                startActivity(intent);

            }
        });
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