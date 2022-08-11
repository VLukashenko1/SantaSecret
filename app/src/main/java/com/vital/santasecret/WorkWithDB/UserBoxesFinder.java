package com.vital.santasecret.WorkWithDB;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Util.BoxesHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserBoxesFinder {
    private static final String LOG_TAG = "User Boxes FINDER";

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DbHelper dbHelper = new DbHelper();
    BoxesHolder boxesHolder = BoxesHolder.getInstance();

    private void findBoxCreatedByUser() {
        dbHelper.BOXES_REF.whereEqualTo("idOfCreator", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            List<Box> boxes = new ArrayList<>();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                Box box = document.toObject(Box.class);
                                boxes.add(box);
                            }
                            findBoxWhereUserInclude(boxes);
                        }
                    }

                });
    }

    private void findBoxWhereUserInclude(List<Box> boxes) {
        dbHelper.BOXES_REF.whereArrayContains("listOfUsers", auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Box> localBoxes = boxes;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Box box = document.toObject(Box.class);
                                localBoxes.add(box);
                            }
                            boxesHolder.getLiveBoxes().setValue(localBoxes);
                        }
                    }
                });

    }

    public void startBackgroundFindingBox() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                findBoxCreatedByUser();
                Log.d(LOG_TAG, "Box Finding Started");
            }
        });
    }

}
