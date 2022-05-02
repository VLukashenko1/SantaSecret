package com.vital.santasecret;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class MakeMessageActivity extends AppCompatActivity {
TextInputEditText input;
Button pushMessage;
TextView currentMessage, instruction;
String boxId, boxName, userId;

DbHelper dbHelper = new DbHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_message);

        input = findViewById(R.id.messageInput);
        pushMessage = findViewById(R.id.pushMessage);
        pushMessage.setOnClickListener(View -> {
            pushNewMessage();
        });
        currentMessage = findViewById(R.id.currentMessage);
        instruction = findViewById(R.id.textView3);

        boxId = getIntent().getExtras().get("boxId").toString();
        boxName = getIntent().getExtras().get("boxName").toString();
        userId = getIntent().getExtras().get("userId").toString();

        findCurrentMessage();
    }

    void findCurrentMessage(){
        currentMessage.setText("You don't create message yet");
        instruction.setText(instruction.getText().toString() + " for box: " + boxName);

        dbHelper.BOX_MESSAGE.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()) {
                    if (doc.getId().equals(boxId)){
                        if (doc.get(userId) != null){
                            currentMessage.setText("");
                            input.setText(doc.get(userId).toString());
                        }
                    }
                }
            }
        });
    }
    void pushNewMessage(){
        if (input.getText().toString().isEmpty() || input.getText() == null){
            makeText("Fill input");
            return;
        }
        HashMap<String, String > forPush = new HashMap<>();
        forPush.put(userId, input.getText().toString());
        dbHelper.BOX_MESSAGE.document(boxId).set(forPush, SetOptions.merge());
        makeText("Message changed success");
        findCurrentMessage();
        return;
    }

    void makeText(String text){
        Toast.makeText(MakeMessageActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MakeMessageActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}