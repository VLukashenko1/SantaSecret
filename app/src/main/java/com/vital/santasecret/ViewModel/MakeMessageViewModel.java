package com.vital.santasecret.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class MakeMessageViewModel extends ViewModel {
    DbHelper dbHelper = new DbHelper();

    String userId, boxId;

    public MakeMessageViewModel(String userId, String boxId){
        this.userId = userId;
        this.boxId = boxId;
    }

    private MutableLiveData<String> currentMessage = new MutableLiveData<>();
    public MutableLiveData<String> getCurrentMessage(String boxId, String userId) {
        findMessage(boxId, userId);
        return currentMessage;
    }
    private void findMessage(String boxId, String userId){
        dbHelper.BOX_MESSAGE.get().addOnSuccessListener(queryDocumentSnapshots -> {
             for (DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()) {
                  if (doc.getId().equals(boxId)){
                        if (doc.get(userId) != null){
                            currentMessage.setValue(doc.get(userId).toString());
                        }
                    }
                }
            });
    }

    public String pushNewMessage(TextInputEditText input){
        if (input.getText().toString().isEmpty() || input.getText() == null){
            return "Fill input";
        }

        HashMap<String, String > forPush = new HashMap<>();
        forPush.put(userId, input.getText().toString());
        dbHelper.BOX_MESSAGE.document(boxId).set(forPush, SetOptions.merge());

        return "Message changed success";
    }
}
