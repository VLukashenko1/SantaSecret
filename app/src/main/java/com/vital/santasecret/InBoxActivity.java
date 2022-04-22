package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.UsersInBoxHolder;
import com.vital.santasecret.adapter.UsersAdapter;

import java.util.List;

public class InBoxActivity extends AppCompatActivity{
TextView nameOfBox, size;
RecyclerView recyclerView;
ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_box);

        //
        nameOfBox = findViewById(R.id.nameOfBoxInBoxAct);
        size = findViewById(R.id.counterOfParicipantsInBoxAct);
        imageButton = findViewById(R.id.addUserToBoxInBoxAct);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //
        setTitle();

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

    void makeText(String  text){
        Toast.makeText(InBoxActivity.this,text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        UsersInBoxHolder.getInstance().getLiveUsers().setValue(null);
        super.onDestroy();
    }
}