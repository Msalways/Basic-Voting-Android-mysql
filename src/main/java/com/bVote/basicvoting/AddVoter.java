package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

public class AddVoter extends AppCompatActivity {

    DbHelper dbHelper;
    EditText userId,UserName,UserState,UserRegion,UserPassword;
    Button AddUserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voter);
        dbHelper = new DbHelper(this);

        userId = findViewById(R.id.AddUserId);
        UserName = findViewById(R.id.UserName);
        UserRegion = findViewById(R.id.UserRegion);
        UserState = findViewById(R.id.UserState);
        UserPassword = findViewById(R.id.UserPassword);
        AddUserData = findViewById(R.id.AddUserData);

        AddUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDetails(userId,UserName,UserRegion,UserState,UserPassword);
                userId.setText("");
                UserName.setText("");
                UserRegion.setText("");
                UserState.setText("");
                UserPassword.setText("");
            }
        });

    }

    private void SaveDetails(EditText userId, EditText userName, EditText userRegion, EditText userState, EditText userPassword) {
        String Id = userId.getText().toString();
        String Name = userName.getText().toString();
        String Region = userRegion.getText().toString();
        String State = userState.getText().toString();
        String Password = userPassword.getText().toString();
        dbHelper.insUser(Id,Name,Region,State,Password);
        Toast.makeText(AddVoter.this,"Added successfully",Toast.LENGTH_LONG).show();
    }
}