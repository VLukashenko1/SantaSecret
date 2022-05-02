package com.vital.santasecret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class MyProfileActivity extends AppCompatActivity {
    TextView currentNickName, displayName, idTextView;
    TextInputEditText nickNameInput;
    Button setNickNameButton;
    DbHelper dbHelper = new DbHelper();
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        currentNickName = findViewById(R.id.currentNickNameMyProfileAct);
        nickNameInput = findViewById(R.id.nickNameInputActivityMyProfile);
        setNickNameButton = findViewById(R.id.setNickNameButtonMyProfileAct);
        displayName = findViewById(R.id.displayNameMyProfileAct);
        userPhoto = findViewById(R.id.userPhotoMyProfileAct);
        setNickNameButton.setOnClickListener(View->{
            if (UserHolder.getInstance().getLiveUser().getValue() == null ||
                    UserHolder.getInstance().getLiveUser().getValue().getuId() == null){
                makeText("an error occurred");
                return;
            }
            setNickName();
        });
        idTextView = findViewById(R.id.idTextViewMyProfileAct);
        idTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String id = UserHolder.getInstance().getLiveUser().getValue().getuId();
                if (id == null){
                    return false;
                }
                setClipboard(MyProfileActivity.this, id);
                makeText("copied");
                return false;
            }
        });

        UserHolder.getInstance().getLiveUser().observe(MyProfileActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                showCurrentNickName(user);
            }
        });

    }

    void showCurrentNickName(User user){
        if (user.getNickName() == null){
            currentNickName.setText("NickName not set");
        }
        else {
            currentNickName.setText("NickName: " + user.getNickName());
        }
        Glide.with(MyProfileActivity.this).load(user.getPhotoUrl()).into(userPhoto);
        displayName.setText(user.getDisplayName());
        idTextView.setText("hold your finger here to copy your id");
    }

    void setNickName(){
        String input = nickNameInput.getText().toString();
        if (input != null && input.isEmpty()){
            makeText("Enter nick name");
            return;
        }
        else if(UserHolder.getInstance().getLiveUser().getValue().getNickName() != null &&
                input.equals(UserHolder.getInstance().getLiveUser().getValue().getNickName())){
            makeText("Entered text equals your current nickname");
            return;
        }
        changeNickName(input);

    }
    void changeNickName(String input){
        dbHelper.USERS_REF.whereEqualTo("nickName", input).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().size() > 0 ){
                        makeText("Nick name already used");
                    }
                    else pushNickName(input);
                }
            }
        });
    }
    void pushNickName(String input){
        HashMap<String,String> forPush = new HashMap<>();
        forPush.put("nickName", input);
        dbHelper.USERS_REF.document(UserHolder.getInstance().getLiveUser().getValue().getuId())
                .set(forPush, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                makeText("Nickname changed");
                nickNameInput.setHint("");
                nickNameInput.setText("");
            }
        });
    }
    void makeText(String text){
        Toast.makeText(MyProfileActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

}