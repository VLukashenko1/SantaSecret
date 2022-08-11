package com.vital.santasecret.ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.UI.MyProfileActivity;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class MyProfileViewModel extends ViewModel {
    DbHelper dbHelper = DbHelper.getInstance();
    public static final String GOOD_RESULT = "Nickname changed";

    MutableLiveData<String> result = new MutableLiveData<>();

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void setNickName(TextInputEditText inputEditText) {
        String input = inputEditText.getText().toString();
        if (input != null && input.isEmpty()) {
            result.setValue("Enter nick name");
            return;
        } else if (UserHolder.getInstance().getLiveUser().getValue().getNickName() != null &&
                input.equals(UserHolder.getInstance().getLiveUser().getValue().getNickName())) {
            result.setValue("Entered text equals your current nickname");
            return;
        }
        changeNickName(input);

    }

    private void changeNickName(String input) {
        dbHelper.USERS_REF.whereEqualTo("nickName", input).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        result.setValue("Nick name already used");
                    } else pushNickName(input);
                }
            }
        });
    }

    private void pushNickName(String input) {
        HashMap<String, String> forPush = new HashMap<>();
        forPush.put("nickName", input);
        dbHelper.USERS_REF.document(UserHolder.getInstance().getLiveUser().getValue().getuId())
                .set(forPush, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        result.setValue("Nickname changed");
                    }
                });
    }
}
