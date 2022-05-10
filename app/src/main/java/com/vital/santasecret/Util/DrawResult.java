package com.vital.santasecret.Util;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.WorkWithDB.DbHelper;

public class DrawResult {
    DbHelper dbHelper = new DbHelper();

    public void isDrawComplete(Box box){
        dbHelper.BOX_RESULT.document(box.getIdOfBox()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    findMessage(documentSnapshot.get(dbHelper.currentUserID).toString());
                }
                return;
            }
        });
    }
    void findMessage(String forWho){
        dbHelper.BOX_MESSAGE.document(forWho).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                   if(documentSnapshot.get(forWho) != null){
                       findName(forWho,documentSnapshot.get(forWho).toString());
                   }
                }
                findName(forWho, null);
            }
        });
    }
    void findName(String forWho, String msg){
        dbHelper.USERS_REF.document(forWho).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //(documentSnapshot.get("displayName").toString(), msg);
            }
        });
    }
}
