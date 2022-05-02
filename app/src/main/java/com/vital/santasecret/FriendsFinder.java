package com.vital.santasecret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FriendsFinder extends AppCompatActivity {
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

        startFind.setOnClickListener(View -> findFriends());
        inputText.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                findFriends();
                return true;
            }
            return false;
        });// start finding when enter pressed

        cardView = findViewById(R.id.cardViewFindFriendsAct);
        cardView.setVisibility(View.INVISIBLE);

        friendPhoto = findViewById(R.id.friendImgFindFrAct);
        goodResult = findViewById(R.id.friendInfoFindFrAct);
        addToFriendButton = findViewById(R.id.addToFriendsButtonFindFriendsAct);
        cancel = findViewById(R.id.cancelButtonFindFriendsAct);
    }

    void findFriends(){
        if (inputText.getText().toString().isEmpty()){
            badResult.setText("Enter text");
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
                    fillCard(documentSnapshot.toObject(User.class));
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
                fillCard(documentSnapshot.toObject(User.class));
            }
        });
    }
    void findByEmail(String input){
        dbHelper.USERS_REF.whereEqualTo("email", input).get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()){
                badResult.setText("nothing found");
                return;
            }
            for (DocumentSnapshot document:task.getResult()) {

                fillCard(document.toObject(User.class));
            }
        });
    }
    void fillCard(User user){
        if (user == null){
            badResult.setText("nothing found");
            return;
        }
        if (user.getuId() != null && user.getuId().equals(UserHolder.getInstance().getLiveUser().getValue().getuId())){
            badResult.setText("You have entered your details");
            return;
        }
        if (UserHolder.getInstance().getLiveUser().getValue().getFriends() != null){
        for (int i=0; i < UserHolder.getInstance().getLiveUser().getValue().getFriends().size();i++){
                if (UserHolder.getInstance().getLiveUser().getValue().getFriends().get(i).equals(user.getuId())){
                    badResult.setText("You are already friends :)");
                    return;
                }
            }
        }
        hideKeyboard(FriendsFinder.this);
        cardView.setVisibility(View.VISIBLE);
        Glide.with(FriendsFinder.this).load(user.getPhotoUrl()).into(friendPhoto);
        goodResult.setText(user.getDisplayName());
        addToFriendButton.setOnClickListener(View -> addToFriendRequests(user.getuId()));
        cancel.setOnClickListener(View -> cardView.setVisibility(android.view.View.INVISIBLE));
    }
    void addToFriendRequests(String Uid){
        HashMap<String, Object> friends = new HashMap<>();
        friends.put("requestToFriends", Arrays.asList(UserHolder.getInstance().getLiveUser().getValue().getuId()));
        dbHelper.USERS_REF.document(Uid).set(friends, SetOptions.merge());
        startActivity(new Intent(FriendsFinder.this, Friends.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FriendsFinder.this, Friends.class));
        super.onBackPressed();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}