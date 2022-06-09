package com.vital.santasecret.ViewModel;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.R;
import com.vital.santasecret.UI.Friends;
import com.vital.santasecret.UI.InBoxActivity;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;
import com.vital.santasecret.WorkWithDB.InBoxUsersFinder;

public class FriendsViewModel extends ViewModel {

    private User user;
    public FriendsViewModel(User user){
        this.user = user;
    }

    DbHelper dbHelper = new DbHelper();

    private MutableLiveData<String[]> friendNamesList = new MutableLiveData<>();
    public MutableLiveData<String[]> getFriendNamesList(){
        return friendNamesList;
    }

    private MutableLiveData<String[]> friendRequestNamesList = new MutableLiveData<>();
    public MutableLiveData<String[]> friendRequestNamesList(){
        return friendRequestNamesList;
    }


    public void getFriendNames(){
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
                    friendNamesList.setValue(friendNames);
                }
            }
        });
    }

    public void getRequestNames(){
        dbHelper.USERS_REF.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String[] names = new String[user.getRequestToFriends().size()];
                    int counter = 0;
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        for (int i = 0; i < user.getRequestToFriends().size(); i++ ){
                            if(document.getId().equals(user.getRequestToFriends().get(i))){
                                names[counter] = document.getData().get("displayName").toString();
                                counter++;
                            }
                        }
                    }
                    friendRequestNamesList.setValue(names);

                }
            }
        });
    }

    public boolean addUserToBox(String idOfBox, int i){
        String frId = UserHolder.getInstance().getLiveUser().getValue().getFriends().get(i);
        dbHelper.BOXES_REF.document(idOfBox).update("listOfUsers", FieldValue.arrayUnion(frId));
        return false;
    }
}
