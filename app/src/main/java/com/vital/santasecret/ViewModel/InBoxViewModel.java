package com.vital.santasecret.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.R;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InBoxViewModel extends ViewModel {
    DbHelper dbHelper = new DbHelper();

    private MutableLiveData<String> drawResult = new MutableLiveData<>();
    public LiveData<String> getDrawResult(Box box){
        isDrawComplete(box);
        return drawResult;
    }

    private void isDrawComplete(Box box) {
        dbHelper.BOX_RESULT.document(box.getIdOfBox()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null) {
                    findMessage(documentSnapshot.get(dbHelper.currentUserID).toString(), box);
                }
                else {
                    return;
                }
            }
        });
    }
    private void findMessage(String forWho, Box box) {
        dbHelper.BOX_MESSAGE.document(box.getIdOfBox()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null && documentSnapshot.get(forWho) != null) {
                    findName(forWho, documentSnapshot.get(forWho).toString());
                } else {
                    findName(forWho, null);
                }
            }
        });
    }
    private void findName(String forWho, String msg) {
        dbHelper.USERS_REF.document(forWho).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                showDrawResult(documentSnapshot.get("displayName").toString(), msg);
            }
        });
    }
    private void showDrawResult(String forWho, String msg) {
        if (forWho == null) {
            return;
        }
        if (msg == null || msg.isEmpty()) {
            drawResult.setValue("You should prepare for " + forWho + ", but he not leave message yet");
        } else {
            drawResult.setValue("You should prepare for " + forWho + "Message for you: " + msg);
        }
    }

    public String shake(Box box) {
        if (box.getListOfUsers() == null) {
            return "Sorry, you can't start yet";
        }
        List<String> tempForShake = new ArrayList<>();
        HashMap<String, String> forPush = new HashMap<>();
        tempForShake.add(box.getIdOfCreator());
        for (int i = 0; i < box.getListOfUsers().size() - 1; i++) {
            forPush.put(tempForShake.get(i), tempForShake.get(tempForShake.size() - i - 1));
            forPush.put(tempForShake.get(tempForShake.size() - i - 1), tempForShake.get(i));
        }
        dbHelper.BOX_RESULT.document(box.getIdOfBox()).set(forPush, SetOptions.merge());
        return "SUCCESS";
    }
}
