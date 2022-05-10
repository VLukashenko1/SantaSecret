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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.BoxHolder;
import com.vital.santasecret.Util.DrawResult;
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

TextView drawResult;

DbHelper dbHelper = new DbHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_box);
        //
        drawResult = findViewById(R.id.drawResultTextView);
        //
        threeDots = findViewById(R.id.threeDotsButtonInBoxAct);
        nameOfBox = findViewById(R.id.nameOfBoxInBoxAct);
        size = findViewById(R.id.counterOfParicipantsInBoxAct);
        addUserToBox = findViewById(R.id.addUserToBoxInBoxAct);
        addUserToBox.setOnClickListener(view ->
                BoxHolder.getInstance().getLiveBox().observe(InBoxActivity.this, box -> {
                    addUserToBox(box);
                }));
        //
        setTitle();
        threeDotsActions();

        BoxHolder.getInstance().getLiveBox().observe(InBoxActivity.this, box -> {
            isDrawComplete(box);
        });
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
        listItems[0] = "BUG";
        listItems[1] = "Make message";
        if (box.getIdOfCreator().equals(dbHelper.currentUserID)){
            listItems[2] = "Start shake";
        }
        else {
            listItems[2] = "Leave box";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InBoxActivity.this, android.R.layout.simple_spinner_dropdown_item, listItems);
        threeDots.setAdapter(adapter);
        AdapterView.OnItemSelectedListener click = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        Intent intent = new Intent(InBoxActivity.this,MakeMessageActivity.class);
                        intent.putExtra("boxName", box.getNameOfBox());
                        intent.putExtra("boxId", box.getIdOfBox());
                        intent.putExtra("userId", dbHelper.currentUserID);
                        startActivity(intent);
                        break;
                    case 2:
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
        if (box.getListOfUsers() == null){
            makeText(getResources().getString(R.string.you_cant_start_yet));
            return;
        }
        HashMap<String,String> forPush = new HashMap<>();
        box.getListOfUsers().add(box.getIdOfCreator());
        for (int i = 0; i < box.getListOfUsers().size()-1; i++) {
            forPush.put(box.getListOfUsers().get(i), box.getListOfUsers().get( box.getListOfUsers().size()-i-1));
            forPush.put(box.getListOfUsers().get( box.getListOfUsers().size()-i-1), box.getListOfUsers().get(i));
        }
        dbHelper.BOX_RESULT.document(box.getIdOfBox()).set(forPush, SetOptions.merge());
        makeText(getResources().getString(R.string.success));
    }
    //DRAW RESULT
    public void isDrawComplete(Box box){
        dbHelper.BOX_RESULT.document(box.getIdOfBox()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    findMessage(documentSnapshot.get(dbHelper.currentUserID).toString(), box);
                }
                return;
            }
        });
    }
    void findMessage(String forWho, Box box){
        dbHelper.BOX_MESSAGE.document(box.getIdOfBox()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    if(documentSnapshot.get(forWho) != null){
                        findName(forWho, documentSnapshot.get(forWho).toString());
                    }
                }
                else {
                    findName(forWho, null);
                }
            }
        });
    }
    void findName(String forWho, String msg){
        dbHelper.USERS_REF.document(forWho).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                showDrawResult(documentSnapshot.get("displayName").toString(), msg);
            }
        });
    }
    void showDrawResult(String forWho, String msg){
        if (forWho == null){
            return;
        }
        if (msg == null){
            drawResult.setText("You should prepare for " + forWho + " no message");
        }
        drawResult.setText("You should prepare for " + forWho + " MSG: " + msg);

    }
//
    void makeText(String  text){
        Toast.makeText(InBoxActivity.this,text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        UsersInBoxHolder.getInstance().getLiveUsers().setValue(null);
        super.onDestroy();
    }
}