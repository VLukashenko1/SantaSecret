package com.vital.santasecret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.Arrays;
import java.util.HashMap;

public class FindFriends extends AppCompatActivity {
    DbHelper dbHelper = new DbHelper();

TextView badResult, goodResult;
TextInputEditText inputText;
Button startFind, addToFriendButton, cancel;
CardView cardView;
ImageView friendPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        badResult = findViewById(R.id.resultTextViewFindFriendsAct);
        inputText = findViewById(R.id.inputFindFrAct);
        startFind = findViewById(R.id.findButtonFindFrAct);

        startFind.setOnClickListener(View -> friendsFinder());
        cardView = findViewById(R.id.cardViewFindFriendsAct);
        cardView.setVisibility(View.INVISIBLE);

        friendPhoto = findViewById(R.id.friendImgFindFrAct);
        goodResult = findViewById(R.id.friendInfoFindFrAct);
        addToFriendButton = findViewById(R.id.addToFriendsButtonFindFriendsAct);
        cancel = findViewById(R.id.cancelButtonFindFriendsAct);
    }

    void friendsFinder(){
        if (inputText.getText().toString().isEmpty()){
            Toast.makeText(FindFriends.this, "Enter text", Toast.LENGTH_SHORT).show();
            return;
        }
        findByNickName(inputText.getText().toString());

    }
    void findByNickName(String input){
        dbHelper.USERS_REF.whereEqualTo("nickName", input).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()){
                    findById(inputText.getText().toString());
                    return;
                }
                for (DocumentSnapshot documentSnapshot:task.getResult()) {
                    findUser(documentSnapshot.getId());
                }
            }
        });
    }
    void findById(String input){
        dbHelper.USERS_REF.document(input).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() == null){
                    findByEmail(input);
                    return;
                }
                findUser(documentSnapshot.getId());
            }
        });
    }
    void findByEmail(String input){
        dbHelper.USERS_REF.whereEqualTo("email", input).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()){
                    badResult.setText("nothing found");
                    return;
                }
                for (DocumentSnapshot document:task.getResult()) {
                    findUser(document.getId());
                }
            }
        });
    }
    void findUser(String id){
        dbHelper.USERS_REF.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fillCard(documentSnapshot.toObject(User.class));
            }
        });
    }
    void fillCard(User user){
        if (user==null){
            badResult.setText("nothing found");
            return;
        }
        if (user.getuId().equals(UserHolder.getInstance().getLiveUser().getValue().getuId())){
            badResult.setText("You have entered your details");
            return;
        }
        cardView.setVisibility(View.VISIBLE);
        Glide.with(FindFriends.this).load(user.getPhotoUrl()).into(friendPhoto);
        goodResult.setText(user.getDisplayName());
        addToFriendButton.setOnClickListener(View -> addToFriendRequests(user.getuId()));
        cancel.setOnClickListener(View -> cardView.setVisibility(android.view.View.INVISIBLE));
    }
    void addToFriendRequests(String Uid){
        HashMap<String, Object> friends = new HashMap<>();
        friends.put("requestToFriends", Arrays.asList(Uid));
        dbHelper.USERS_REF.document(Uid).set(friends, SetOptions.merge());
        startActivity(new Intent(FindFriends.this, Friends.class));
    }
}