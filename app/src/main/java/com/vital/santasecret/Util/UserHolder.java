package com.vital.santasecret.Util;

import com.vital.santasecret.Model.User;

import androidx.lifecycle.MutableLiveData;

public class UserHolder {

    private static final UserHolder holder = new UserHolder();
    public static UserHolder getInstance(){
        return holder;
    }

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getLiveUser() {
        return userLiveData;
    }

}
