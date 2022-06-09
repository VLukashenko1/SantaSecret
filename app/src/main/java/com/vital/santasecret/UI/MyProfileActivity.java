package com.vital.santasecret.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.R;
import com.vital.santasecret.Util.UserHolder;
import com.vital.santasecret.ViewModel.MyProfileViewModel;
import com.vital.santasecret.WorkWithDB.DbHelper;

public class MyProfileActivity extends AppCompatActivity {
    private MyProfileViewModel mViewModel;

    private TextView currentNickName, displayName, idTextView;
    private TextInputEditText nickNameInput;
    private DbHelper dbHelper = new DbHelper();
    private ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mViewModel = new MyProfileViewModel();

        currentNickName = findViewById(R.id.currentNickNameMyProfileAct);
        nickNameInput = findViewById(R.id.nickNameInputActivityMyProfile);
        Button setNickNameButton = findViewById(R.id.setNickNameButtonMyProfileAct);
        displayName = findViewById(R.id.displayNameMyProfileAct);
        userPhoto = findViewById(R.id.userPhotoMyProfileAct);

        setNickNameButton.setOnClickListener(View->{
            if (UserHolder.getInstance().getLiveUser().getValue() == null
                    || UserHolder.getInstance().getLiveUser().getValue().getuId() == null){
                makeText("an error occurred");
                return;
            }
            setNickName();
            mViewModel.setNickName(nickNameInput);
        });

        idTextView = findViewById(R.id.idTextViewMyProfileAct);
        idTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (dbHelper.currentUserID == null){
                    return false;
                }
                setClipboard(MyProfileActivity.this, dbHelper.currentUserID);
                makeText(getString(R.string.copied));
                return false;
            }
        });

        UserHolder.getInstance().getLiveUser().observe(MyProfileActivity.this, user -> showCurrentNickName(user));

    }

    private void showCurrentNickName(User user){
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

    private void setNickName(){
        mViewModel.getResult().observe(this,res -> {
            if (res.equals(mViewModel.GOOD_RESULT)){
                nickNameInput.setHint("");
                nickNameInput.setText("");
                makeText(res);
                return;
            }
            makeText(res);
        });
    }

    private void makeText(String text){
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