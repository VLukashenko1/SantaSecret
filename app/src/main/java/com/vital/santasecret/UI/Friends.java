package com.vital.santasecret.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

import com.google.firebase.firestore.FieldValue;
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

    FriendsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        friendsCounter = findViewById(R.id.friendsCounter);
        ellipseFriends = findViewById(R.id.elipseFriends);

        addFriendsButton = findViewById(R.id.addFriendsButtonFriendsAct);
        addFriendsButton.setOnClickListener(View -> startActivity(new Intent(Friends.this, FriendsFinder.class)));

        frList = findViewById(R.id.listViewFriendsAct);

        UserHolder.getInstance().getLiveUser().observe(this, user -> {
            if (user != null){
                mViewModel.setUser(user);
                System.out.println("User transfer to view model");
            }
            //FriendsList
            mViewModel.getFriendNamesList().observe(this, friendNames -> {
                frList.setAdapter(new ArrayAdapter<String>(Friends.this, android.R.layout.simple_list_item_1, friendNames));
                registerForContextMenu(frList);
            });
            //RequestsList
            showFriendsRequest(user);
        });

        //!!
        addUserToBox();
    }

    void showFriendsRequest(User user) {
        if (user.getRequestToFriends() == null || user.getRequestToFriends().size() == 0) {
            return;
        }
        friendsCounter.setVisibility(View.VISIBLE);
        ellipseFriends.setVisibility(View.VISIBLE);

        friendsCounter.setText(Integer.toString(user.getRequestToFriends().size()));
        ellipseFriends.setOnClickListener(view -> mViewModel.getRequestNames());
        fillRequestsList();
    }

    void fillRequestsList() {
        FriendsMover friendsMover = new FriendsMover();
        mViewModel.friendRequestNamesList().observe(this, names -> {
            frList.setAdapter(new ArrayAdapter<String>(Friends.this, android.R.layout.simple_list_item_1, names));

            frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String friendId = UserHolder.getInstance().getLiveUser().getValue().getRequestToFriends().get(i);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.choose_an_action));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.add_to_friends_ask)).setCancelable(true)
                            .setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i1) -> {
                                friendsMover.addToFriends(dbHelper.currentUserID, friendId);
                            }).setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i12) ->
                                    friendsMover.dellUserFromFriendsRequest(dbHelper.currentUserID, friendId));
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        });
    }

    void addUserToBox() {
        if (getIntent().getBooleanExtra("IsFromInBoxActivity", false)) {
            String idOfBox = getIntent().getStringExtra("IdOfBox");
            if (idOfBox == null) {
                startActivity(new Intent(Friends.this, MainActivity.class));
                return;
            }
            frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mViewModel.addUserToBox(idOfBox, i);

                    Toast.makeText(Friends.this, getResources().getString(R.string.you_added_friend_to_box), Toast.LENGTH_SHORT).show();

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