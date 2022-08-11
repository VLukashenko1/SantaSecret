package com.vital.santasecret.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.vital.santasecret.Model.Box;
import com.vital.santasecret.R;
import com.vital.santasecret.Util.BoxHolder;
import com.vital.santasecret.Util.BoxesHolder;
import com.vital.santasecret.WorkWithDB.InBoxUsersFinder;
import com.vital.santasecret.WorkWithDB.InBoxUsersFinderService;
import com.vital.santasecret.WorkWithDB.UserBoxesFinder;
import com.vital.santasecret.WorkWithDB.UserChecker;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MAIN ACTIVITY CLASS";

    Button createBox, friendsButton;
    ListView boxesListView;
    ImageView threeDots;

    InBoxUsersFinder inBoxUsersFinder = InBoxUsersFinder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UserChecker userChecker = UserChecker.getInstance();
        userChecker.observeUser();
        Log.d(LOG_TAG, "Application started");

        createBox = findViewById(R.id.addBoxMain);
        createBox.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CreateBox.class)));
        friendsButton = findViewById(R.id.friendsMain);
        friendsButton.setOnClickListener(view -> startActivity(new Intent(this, Friends.class)));
        boxesListView = findViewById(R.id.boxListViewOnMainPage);
        threeDots = findViewById(R.id.threeDotsMain);
        threeDots.setOnClickListener(View -> startActivity(new Intent(MainActivity.this, MyProfileActivity.class)));

        boxesFinder();

    }


    void boxesFinder() {
        UserBoxesFinder userBoxesFinder = new UserBoxesFinder();
        userBoxesFinder.startBackgroundFindingBox();
        BoxesHolder.getInstance().getLiveBoxes().observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                Log.d(LOG_TAG, "Boxes found and show");
                boxListFiller(boxes);
            }
        });
    }

    void boxListFiller(List<Box> boxes) {
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, boxNamesList(boxes));
        boxesListView.setAdapter(adapter);
        boxesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Box box = boxes.get(i);
                inBoxUsersFinder.getListWithIdOfUsers(box.getIdOfBox());
                BoxHolder.getInstance().getLiveBox().setValue(box);
                Intent intent = new Intent(MainActivity.this, InBoxActivity.class);
                intent.putExtra("nameOfBox", box.getNameOfBox());
                startActivity(intent);

            }
        });
    }

    String[] boxNamesList(List<Box> boxes) {
        String[] boxNamesArray = new String[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) {
            boxNamesArray[i] = boxes.get(i).getNameOfBox();
        }
        return boxNamesArray;
    }

}