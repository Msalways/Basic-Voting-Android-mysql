package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

public class LoginPage extends AppCompatActivity {

    EditText voter,password;
    Button login;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        voter = findViewById(R.id.voter);
        password = findViewById(R.id.vpassword);
        login = findViewById(R.id.login);

        db = new DbHelper(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify() {
        String voterId = voter.getText().toString();
        String pass = password.getText().toString();

        if (!voterId.isEmpty() && !pass.isEmpty()){
            if (!db.checkVisited(voterId)) {
                boolean check = db.loginCheck(voterId, pass);
                if (check) {
                    Intent intent = new Intent(this, VotingActivity.class);
                    intent.putExtra("USERID", voterId);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this,"The details are invalid",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this,"U have already voted",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
        else
            Toast.makeText(this, "Enter the details",Toast.LENGTH_LONG).show();
    }
}