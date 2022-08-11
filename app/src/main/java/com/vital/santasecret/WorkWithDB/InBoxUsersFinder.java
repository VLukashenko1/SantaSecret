package com.vital.santasecret.WorkWithDB;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.BoxesHolder;
import com.vital.santasecret.Util.UsersInBoxHolder;

import java.util.ArrayList;
import java.util.List;

public class InBoxUsersFinder {

    private static final InBoxUsersFinder holder = new InBoxUsersFinder();
    public static InBoxUsersFinder getInstance() {
        return holder;
    }

    DbHelper dbHelper = DbHelper.getInstance();

    public void getListWithIdOfUsers(String idOfBox) {
        dbHelper.BOXES_REF.document(idOfBox).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> idOfUsers = (List<String>) documentSnapshot.get("listOfUsers");
                if (idOfUsers == null) {
                    idOfUsers = new ArrayList<>();
                    idOfUsers.add(documentSnapshot.get("idOfCreator").toString());
                } else {
                    idOfUsers.add(documentSnapshot.get("idOfCreator").toString());
                }
                getUsersInBox(idOfUsers);
            }
        });
    }

    void getUsersInBox(List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            dbHelper.USERS_REF.document(ids.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    UsersInBoxHolder.getInstance().adder(user);
                }
            });
        }
    }

}
