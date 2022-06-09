package com.vital.santasecret.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.UI.Friends;
import com.vital.santasecret.WorkWithDB.DbHelper;

public class FriendsViewModel extends ViewModel {

    DbHelper dbHelper = new DbHelper();

    private MutableLiveData<String[]> friendNamesList = new MutableLiveData<>();
    public MutableLiveData<String[]> getFriendNamesList(){
        return friendNamesList;
    }

    public void starter(User user){

    }


}
