package com.vital.santasecret.ViewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.R;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class CreateBoxViewModel extends ViewModel {
    DbHelper dbHelper = new DbHelper();
    Context context;

    public CreateBoxViewModel(Context context){
        this.context = context;
    }

    public String createBox(String name){
        if (name.isEmpty()){
            return context.getResources().getString(R.string.enter_box_name);
        }
        Box box = new Box(dbHelper.currentUserID, name, null, null);
        dbHelper.BOXES_REF.add(box).addOnSuccessListener(documentReference -> {
            HashMap<String, String> id = new HashMap<>();
            id.put("idOfBox", documentReference.getId());
            dbHelper.BOXES_REF.document(documentReference.getId()).set(id, SetOptions.merge());
        });
        return context.getString(R.string.box_created);
    }

    MutableLiveData<String> result = new MutableLiveData<>();
    public MutableLiveData<String> getResult(){
        return result;
    }

    public void isBoxExist(String idOfBox){
        dbHelper.BOXES_REF.document(idOfBox).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    if (!documentSnapshot.get(DbHelper.ID_OF_BOX_CREATOR).toString().equals(dbHelper.currentUserID)){
                        connectToBox(idOfBox);
                    }
                    result.setValue(context.getResources().getString(R.string.box_created_by_yourself));
                    return;
                }
                result.setValue(context.getResources().getString(R.string.box_not_found));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.setValue(context.getString(R.string.error));
            }
        });
    }
    void connectToBox(String idOfBox){
        dbHelper.BOXES_REF.document(idOfBox).update("listOfUsers", FieldValue
                .arrayUnion(dbHelper.currentUserID));
        result.setValue(context.getResources().getString(R.string.you_connect_to_box_success));

    }
}
