package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

import java.util.ArrayList;

public class VotingActivity extends AppCompatActivity {

    ArrayList<String> candidates,party;
    RadioGroup votingGroup;
    RadioButton radioButton;
    Button vote;
    String radioText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        votingGroup = findViewById(R.id.votingGroup);
        DbHelper db = new DbHelper(this);
        Intent intent = getIntent();
        String UserId = intent.getStringExtra("USERID");
        Log.d("zz", "onCreate: "+UserId);
        candidates = db.FetchCandidates(UserId);
        Log.d("zz", "onCreate: "+candidates);
        createRadio(candidates);
        int size = candidates.size();
        votingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio = findViewById(checkedId);
                radioText = radio.getText().toString();
                String[] radioName = radioText.split("-");
                db.AddVoteTo(radioName[0]);
                Toast.makeText(VotingActivity.this,radioName[0],Toast.LENGTH_LONG).show();
                GoToMainPage();
            }
        });
    }

    private void GoToMainPage() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    private void createRadio(ArrayList<String> candidates) {
        Log.d("zz","Radio");
        votingGroup.setOrientation(LinearLayout.VERTICAL);
        int len = candidates.size();
        for (int i = 0; i < len; i++){
            radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            radioButton.setText(candidates.get(i));
            radioButton.setId(i);
            votingGroup.addView(radioButton);
        }
    }
}