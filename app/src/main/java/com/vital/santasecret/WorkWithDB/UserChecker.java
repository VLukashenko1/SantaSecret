package com.vital.santasecret.WorkWithDB;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.UserHolder;

import androidx.annotation.Nullable;

public class UserChecker {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    UserHolder uh = UserHolder.getInstance();

    DbHelper dbHelper = new DbHelper();
    public void isUserRegister(){
        dbHelper.USERS_REF.document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() == null){
                    createUserFolder();
                }
                updateLocalHolder();
            }
        });
    }
    void createUserFolder(){
        User user = new User(auth.getCurrentUser().getDisplayName(),
                             auth.getCurrentUser().getEmail(),
                             auth.getCurrentUser().getUid(),
                             auth.getCurrentUser().getPhotoUrl().toString(),
                null, null,null);

        dbHelper.USERS_REF.document(auth.getCurrentUser().getUid()).set(user);

        uh.getLiveUser().setValue(user);
    }
    public void updateLocalHolder(){
        dbHelper.USERS_REF.document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                uh.getLiveUser().setValue(user);
            }
        });
    }
    public void userObserver(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fillLocalHolder();
            }
        });
    }
    void fillLocalHolder(){
        dbHelper.USERS_REF.document(auth.getUid()).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UserHolder.getInstance().getLiveUser().setValue(value.toObject(User.class));
                System.out.println("User info changed");
            }
        });
    }
}
