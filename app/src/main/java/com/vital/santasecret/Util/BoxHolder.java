package com.vital.santasecret.Util;


import com.vital.santasecret.Model.Box;

import androidx.lifecycle.MutableLiveData;

public class BoxHolder {
    private static final BoxHolder holder = new BoxHolder();
    public static BoxHolder getInstance(){
        return holder;
    }

    private MutableLiveData<Box> userLiveData = new MutableLiveData<>();
    public MutableLiveData<Box> getLiveUser() {
        return userLiveData;
    }

}
