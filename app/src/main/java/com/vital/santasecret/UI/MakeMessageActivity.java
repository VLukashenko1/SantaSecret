package com.vital.santasecret.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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
import com.vital.santasecret.R;
import com.vital.santasecret.ViewModel.MakeMessageViewModel;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class MakeMessageActivity extends AppCompatActivity {
    TextInputEditText input;
    Button pushMessage;
    TextView currentMessage, instruction;
    String boxId, boxName, userId;

    private MakeMessageViewModel mViewModel;

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
        instruction.setText(instruction.getText().toString() + " for box: " + boxName);
//
        mViewModel = new MakeMessageViewModel(userId, boxId);
//
        findCurrentMessage();
    }

    void findCurrentMessage() {
        mViewModel.getCurrentMessage(boxId, userId).observe(this, currentMessage -> {
            this.currentMessage.setText("");
            input.setText(currentMessage);
        });
    }

    void pushNewMessage() {
        makeText(mViewModel.pushNewMessage(input));
        findCurrentMessage();
    }

    void makeText(String text) {
        Toast.makeText(MakeMessageActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MakeMessageActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}