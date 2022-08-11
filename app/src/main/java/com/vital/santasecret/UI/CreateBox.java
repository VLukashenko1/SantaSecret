package com.vital.santasecret.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vital.santasecret.R;
import com.vital.santasecret.ViewModel.CreateBoxViewModel;

public class CreateBox extends AppCompatActivity {
    private CreateBoxViewModel mViewModel;

    ImageButton backButton;
    RadioGroup radioGroup;
    TextInputEditText boxName;
    Button createBoxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_box);

        mViewModel = new CreateBoxViewModel(this);

        backButton = findViewById(R.id.backButtonCreateBoxAct);
        backButton.setOnClickListener(view -> startActivity(new Intent(CreateBox.this, MainActivity.class)));
        backButton.setVisibility(View.INVISIBLE);

        boxName = findViewById(R.id.createBoxInput);

        radioGroup = findViewById(R.id.radioGroupCreateBoxAct);

        createBoxButton = findViewById(R.id.createBoxButtonCreateBoxAct);
        createBoxButton.setOnClickListener(view -> {
            actionChoosing(radioGroup.getCheckedRadioButtonId());
        });

        boxNameHintSetter();
    }

    void boxNameHintSetter() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonCreateBox:
                        boxName.setHint(getResources().getString(R.string.enter_box_name));
                        break;
                    case R.id.radioButtonConnectToBox:
                        boxName.setHint(getResources().getString(R.string.enter_box_id));
                        break;
                }
            }
        });
    }

    void actionChoosing(int i) {
        switch (i) {
            case R.id.radioButtonCreateBox:
                makeText(mViewModel.createBox(boxName.getText().toString()));
                changeActivity();
                break;
            case R.id.radioButtonConnectToBox:
                if (!boxName.getText().toString().isEmpty()) {
                    mViewModel.getResult().observe(this, result -> {
                        mViewModel.isBoxExist(boxName.getText().toString());
                        makeText(result);
                        changeActivity();
                    });
                    return;
                }
                makeText(getResources().getString(R.string.enter_box_id));
                break;
        }

    }

    void makeText(String text) {
        Toast.makeText(CreateBox.this, text, Toast.LENGTH_SHORT).show();
    }
    void changeActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}