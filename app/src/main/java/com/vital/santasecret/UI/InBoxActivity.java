package com.vital.santasecret.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vital.santasecret.Model.Box;
import com.vital.santasecret.Model.User;
import com.vital.santasecret.R;
import com.vital.santasecret.Util.BoxHolder;
import com.vital.santasecret.Util.UsersInBoxHolder;
import com.vital.santasecret.ViewModel.InBoxViewModel;
import com.vital.santasecret.WorkWithDB.DbHelper;
import com.vital.santasecret.Adapter.UsersAdapter;

import java.util.List;

public class InBoxActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;

    TextView nameOfBox, size, drawResult;
    RecyclerView recyclerView;
    ImageButton addUserToBox;
    Spinner threeDots;

    DbHelper dbHelper = new DbHelper();
    InBoxViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_box);

        //ViewModel
        mViewModel = ViewModelProviders.of(this).get(InBoxViewModel.class);
        mViewModel.sendContext(getApplicationContext());

        threeDots = findViewById(R.id.threeDotsButtonInBoxAct);
        nameOfBox = findViewById(R.id.nameOfBoxInBoxAct);
        size = findViewById(R.id.counterOfParicipantsInBoxAct);
        drawResult = findViewById(R.id.drawResultTextView);

        addUserToBox = findViewById(R.id.addUserToBoxInBoxAct);
        addUserToBox.setOnClickListener(view -> BoxHolder.getInstance().getLiveBox().observe(InBoxActivity.this, this::addUserToBox));

        setTitle();
        threeDotsActions();

        BoxHolder.getInstance().getLiveBox().observe(InBoxActivity.this, box -> {
            mViewModel.getDrawResult(box).observe(this, result -> drawResult.setText(result));
        });

    }

    void setTitle() {
        nameOfBox.setText(getIntent().getExtras().get("nameOfBox").toString());
        UsersInBoxHolder.getInstance().getLiveUsers().observe(InBoxActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users == null) {
                    return;
                }
                size.setText(getResources().getString(R.string.participant) + users.size());
                fillRecyclerView(users);
            }
        });
    }

    void fillRecyclerView(List<User> users) {
        recyclerView = findViewById(R.id.recyclerViewInBoxAct);
        UsersAdapter adapter = new UsersAdapter(InBoxActivity.this, users);
        recyclerView.setAdapter(adapter);
    }

    void addUserToBox(Box box) {
        Intent intent = new Intent(InBoxActivity.this, Friends.class);
        intent.putExtra("IsFromInBoxActivity", true);
        intent.putExtra("IdOfBox", box.getIdOfBox());
        startActivity(intent);
    }

    void threeDotsActions() {
        Box box = BoxHolder.getInstance().getLiveBox().getValue();
        if (box == null) {
            return;
        }
        String[] listItems = new String[3];
        listItems[0] = "BUG";
        listItems[1] = "Make message";
        if (box.getIdOfCreator().equals(dbHelper.currentUserID)) {
            listItems[2] = "Start shake";
        } else {
            listItems[2] = "Leave box";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InBoxActivity.this, android.R.layout.simple_spinner_dropdown_item, listItems);
        threeDots.setAdapter(adapter);
        AdapterView.OnItemSelectedListener click = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        Intent intent = new Intent(InBoxActivity.this, MakeMessageActivity.class);
                        intent.putExtra("boxName", box.getNameOfBox());
                        intent.putExtra("boxId", box.getIdOfBox());
                        intent.putExtra("userId", dbHelper.currentUserID);
                        startActivity(intent);
                        break;
                    case 2:
                        makeText(mViewModel.shake(box));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        };
        threeDots.setOnItemSelectedListener(click);
    }


    protected void makeText(String text) {
        Toast.makeText(InBoxActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        UsersInBoxHolder.getInstance().getLiveUsers().setValue(null);
        super.onDestroy();
    }

}