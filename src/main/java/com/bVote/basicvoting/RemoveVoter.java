package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

import java.util.ArrayList;

public class RemoveVoter extends AppCompatActivity  {

    EditText userid,name;
    Button RemoveUsers;
    ListView ShowUsers;
    DbHelper db;
    ArrayList<String> ListUsers;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        userid = findViewById(R.id.RemoveUserId);
        name = findViewById(R.id.RemoveUserName);

        RemoveUsers = findViewById(R.id.RemoveUser);
        ShowUsers = findViewById(R.id.ShowVoters);
        db = new DbHelper(this);

        showAllUser();
        RemoveUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelUsers();
                showAllUser();
                userid.setText("");
                name.setText("");
            }
        });
    }

    private void DelUsers() {
        String uid = userid.getText().toString();
        String uname = name.getText().toString();

        boolean check = db.DeleteUsers(uid,uname);
        if (check)
            Toast.makeText(RemoveVoter.this,uname+" Deleted successfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(RemoveVoter.this,"Not deleted",Toast.LENGTH_LONG).show();
    }

    private void showAllUser() {
        ListUsers = db.ShowUsers();
        Log.d("Show", String.valueOf(ListUsers));
        if (ListUsers != null) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListUsers);
            ShowUsers.setAdapter(arrayAdapter);
        }
        else
            Toast.makeText(this, "No Voters available now",Toast.LENGTH_LONG).show();
    }
}