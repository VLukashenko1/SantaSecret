package com.vital.santasecret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.WorkWithDB.DbHelper;

public class CreateBox extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DbHelper dbHelper = new DbHelper();

    ImageButton backButton;
    RadioGroup radioGroup;
    TextInputEditText boxname;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_box);

        backButton = findViewById(R.id.backButtonCreateBoxAct);
        backButton.setOnClickListener(view -> startActivity(new Intent(CreateBox.this, MainActivity.class)));
        backButton.setVisibility(View.INVISIBLE);

        boxname = findViewById(R.id.createBoxInput);
        radioGroup = findViewById(R.id.radioGroupCreateBoxAct);
        go = findViewById(R.id.createBoxButtonCreateBoxAct);
        go.setOnClickListener(view -> {
            actionChoosing(radioGroup.getCheckedRadioButtonId());
        });

        setHint();
    }
    void setHint(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonCreateBox:
                        boxname.setHint("Enter box name");
                        break;
                    case R.id.radioButtonConnectToBox:
                        boxname.setHint("Enter box id");
                        break;
                }
            }
        });
    }
    void actionChoosing(int i){
        switch (i){
            case R.id.radioButtonCreateBox:
                 createBox();
                 break;
             case R.id.radioButtonConnectToBox:
                 if (!boxname.getText().toString().isEmpty()){
                     isBoxExist(boxname.getText().toString());
                     return;
                 }
                 makeText("Enter id of Box");
                 break;
          }


    }

    void createBox(){
        String name = boxname.getText().toString();
        if (name.isEmpty()){
            makeText("Enter name of box");
            return;
        }
        Box box = new Box(auth.getCurrentUser().getUid(), name, null);
        dbHelper.BOXES_REF.add(box);
        makeText("Box created");
        changeActivity();
    }
    void connectToBox(String idOfBox){
        dbHelper.BOXES_REF.document(idOfBox).update("listOfUsers", FieldValue
                .arrayUnion(auth.getCurrentUser().getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {}});
        makeText("you connected to box");
        changeActivity();
    }
    void isBoxExist(String idOfBox){
        dbHelper.BOXES_REF.document(idOfBox).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    if (!documentSnapshot.get(DbHelper.ID_OF_BOX_CREATOR).toString().equals(auth.getCurrentUser().getUid())){
                        connectToBox(idOfBox);
                    }
                    makeText("This box where created by yourself");
                    return;
                }
                makeText("Box not found");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeText("An error occurred");
            }
        });
    }
    void makeText(String text){
        Toast.makeText(CreateBox.this, text, Toast.LENGTH_SHORT).show();
    }
    void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}