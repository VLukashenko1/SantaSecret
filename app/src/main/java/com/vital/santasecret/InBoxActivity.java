package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.BoxHolder;
import com.vital.santasecret.Util.UsersInBoxHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;
import com.vital.santasecret.adapter.UsersAdapter;

import java.util.HashMap;
import java.util.List;

public class InBoxActivity extends AppCompatActivity{
TextView nameOfBox, size;
RecyclerView recyclerView;
ImageButton addUserToBox;
Spinner threeDots;

DbHelper dbHelper = new DbHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_box);
        //
        threeDots = findViewById(R.id.threeDotsButtonInBoxAct);
        nameOfBox = findViewById(R.id.nameOfBoxInBoxAct);
        size = findViewById(R.id.counterOfParicipantsInBoxAct);
        addUserToBox = findViewById(R.id.addUserToBoxInBoxAct);
        addUserToBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxHolder.getInstance().getLiveBox().observe(InBoxActivity.this, new Observer<Box>() {
                    @Override
                    public void onChanged(Box box) {
                        addUserToBox(box);
                    }
                });
            }
        });
        //
        setTitle();
        threeDotsActions();

    }
    void setTitle(){
        nameOfBox.setText(getIntent().getExtras().get("nameOfBox").toString());
        UsersInBoxHolder.getInstance().getLiveUsers().observe(InBoxActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users == null){
                    return;
                }
                size.setText("Participant:" + users.size());
                fillRecyclerView(users);
            }
        });
    }
    void fillRecyclerView(List<User> users){
        recyclerView = findViewById(R.id.recyclerViewInBoxAct);
        UsersAdapter adapter = new UsersAdapter(InBoxActivity.this, users);
        recyclerView.setAdapter(adapter);
    }

    void addUserToBox(Box box){
        Intent intent = new Intent(InBoxActivity.this, Friends.class);
        intent.putExtra("IsFromInBoxActivity", true);
        intent.putExtra("IdOfBox", box.getIdOfBox());
        startActivity(intent);
    }

    void threeDotsActions(){
        Box box = BoxHolder.getInstance().getLiveBox().getValue();
        if (box == null){
            return;
        }
        String []listItems = new String[3];
        if (box.getIdOfCreator().equals(dbHelper.currentUserID)){
            listItems[0] = "BUG";
            listItems[1] = "Start shake";
            listItems[2] = "Make message";
        }
        else {
            listItems[0] = "BUG";
            listItems[1] = "Leave box";
            listItems[2] = "Make message";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InBoxActivity.this, android.R.layout.simple_spinner_dropdown_item, listItems);
        threeDots.setAdapter(adapter);
        AdapterView.OnItemSelectedListener click = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 2:
                        Intent intent = new Intent(InBoxActivity.this,MakeMessageActivity.class);
                        intent.putExtra("boxName", box.getNameOfBox());
                        intent.putExtra("boxId", box.getIdOfBox());
                        intent.putExtra("userId", dbHelper.currentUserID);
                        startActivity(intent);
                        break;
                    case 1:
                        shake(box);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        };
        threeDots.setOnItemSelectedListener(click);
    }

    void shake(Box box){
        if (box.getListOfUsers().size() < 2){
            makeText("You cant start yet :c");
        }
        for (int i = 1; i < box.getListOfUsers().size(); i++) {

        }
    }

    void makeText(String  text){
        Toast.makeText(InBoxActivity.this,text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        UsersInBoxHolder.getInstance().getLiveUsers().setValue(null);
        super.onDestroy();
    }
}