package com.vital.santasecret.Util;

import com.vital.santasecret.Model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class UsersInBoxHolder {
    private static final UsersInBoxHolder holder = new UsersInBoxHolder();
    public static UsersInBoxHolder getInstance(){
        return holder;
    }

    private MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> getLiveUsers() {
        return usersLiveData;
    }

    public void adder(User user){
        List<User> usersList = getLiveUsers().getValue();
        if (usersList != null){
            usersList.add(user);
        }
        else {
            usersList = new ArrayList<>();
            usersList.add(user);
        }
        getLiveUsers().setValue(usersList);
    }
}
