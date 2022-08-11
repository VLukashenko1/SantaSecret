package com.vital.santasecret.Util;

import com.vital.santasecret.Model.Box;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class BoxesHolder {
    private static final BoxesHolder holder = new BoxesHolder();

    public static BoxesHolder getInstance() {
        return holder;
    }

    private MutableLiveData<List<Box>> boxesLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Box>> getLiveBoxes() {
        return boxesLiveData;
    }

}
