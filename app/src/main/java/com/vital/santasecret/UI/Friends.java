package com.vital.santasecret.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.R;
import com.vital.santasecret.ViewModel.FriendsViewModel;
import com.vital.santasecret.WorkWithDB.FriendsMover;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;
import com.vital.santasecret.WorkWithDB.InBoxUsersFinder;

public class Friends extends AppCompatActivity {
    DbHelper dbHelper = new DbHelper();

    TextView friendsCounter;
    ImageView ellipseFriends;
    ImageButton addFriendsButton;
    ListView frList;

    FriendsViewModel mViewModel = new FriendsViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsCounter = findViewById(R.id.friendsCounter);
        ellipseFriends = findViewById(R.id.elipseFriends);
        ellipseFriends.setVisibility(View.INVISIBLE);
        friendsCounter.setVisibility(View.INVISIBLE);

        addFriendsButton = findViewById(R.id.addFriendsButtonFriendsAct);
        addFriendsButton.setOnClickListener(View ->  startActivity(new Intent(Friends.this, FriendsFinder.class)));

        frList = findViewById(R.id.listViewFriendsAct);

        UserHolder.getInstance().getLiveUser().observe(this, user -> {
            mViewModel.starter(user);

            findFriendsNames(user);
            showFriendsRequest(user);
        });

        //!!
        addUserToBox();
    }

    void findFriendsNames(User user){
        if (user == null || user.getFriends() == null){
            return;
        }
        dbHelper.USERS_REF.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String[] friendNames = new String[user.getFriends().size()];
                    int counter = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        for (int i = 0; i < user.getFriends().size(); i++){
                            if (user.getFriends().get(i).equals(document.getId())){
                                friendNames[counter] = document.getData().get("displayName").toString();
                                counter++;
                            }
                        }
                    }
                    showFriends(friendNames);
                }
            }
        });
    }

    public void showFriends(String[] friendsNames){
        frList.setAdapter(new ArrayAdapter<String>(Friends.this, android.R.layout.simple_list_item_1,friendsNames));
        registerForContextMenu(frList);
    }

    void showFriendsRequest(User user){
        if (user.getRequestToFriends() == null || user.getRequestToFriends().size() == 0){
            return;
            }
        friendsCounter.setVisibility(View.VISIBLE);
        ellipseFriends.setVisibility(View.VISIBLE);

        friendsCounter.setText(Integer.toString(user.getRequestToFriends().size()));
        ellipseFriends.setOnClickListener(view -> findFriendsRequestNames());
    }
    void findFriendsRequestNames(){
        dbHelper.USERS_REF.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String[] names = new String[UserHolder.getInstance().getLiveUser().getValue().getRequestToFriends().size()];
                    int counter = 0;
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        for (int i = 0; i < UserHolder.getInstance().getLiveUser().getValue().getRequestToFriends().size(); i++ ){
                            if(document.getId().equals(UserHolder.getInstance().getLiveUser().getValue().getRequestToFriends().get(i))){
                                names[counter] = document.getData().get("displayName").toString();
                                counter++;
                            }
                        }
                    }
                    fillRequestsList(names);
                }
            }
        });
    }
    void fillRequestsList(String[] names){
        FriendsMover friendsMover = new FriendsMover();
        String currentUserId = UserHolder.getInstance().getLiveUser().getValue().getuId();

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Friends.this, android.R.layout.simple_list_item_1, names);
        frList.setAdapter(adapter);
        frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String friendId = UserHolder.getInstance().getLiveUser().getValue().getRequestToFriends().get(i);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.choose_an_action));
                alertDialogBuilder.setMessage(getResources().getString(R.string.add_to_friends_ask)).setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.yes),(dialogInterface, i1) -> {
                    friendsMover.addToFriends(currentUserId, friendId);
                }).setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i12) ->
                        friendsMover.dellUserFromFriendsRequest(currentUserId,friendId));
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    void addUserToBox(){
        if (getIntent().getBooleanExtra("IsFromInBoxActivity", false)){
            String idOfBox = getIntent().getStringExtra("IdOfBox");
            if (idOfBox == null){
                startActivity(new Intent(Friends.this, InBoxActivity.class));
                return;
            }
            frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String frId = UserHolder.getInstance().getLiveUser().getValue().getFriends().get(i);
                    dbHelper.BOXES_REF.document(idOfBox).update("listOfUsers", FieldValue.arrayUnion(frId));
                    Toast.makeText(Friends.this,getResources().getString(R.string.you_added_friend_to_box),Toast.LENGTH_SHORT).show();
                    InBoxUsersFinder inBoxUsersFinder = new InBoxUsersFinder();
                    inBoxUsersFinder.getListWithIdOfUsers(idOfBox);
                    startActivity(new Intent(Friends.this, InBoxActivity.class));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Friends.this, MainActivity.class));
        super.onBackPressed();
    }
}