package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

public class AddCandidates extends AppCompatActivity {

    EditText name,party,region,state;
    Button save;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidates);

        name = findViewById(R.id.AddCandidateName);
        party = findViewById(R.id.AddCandidateParty);
        region = findViewById(R.id.AddCandidateRegion);
        state = findViewById(R.id.AddCandidateState);

        db = new DbHelper(this);

        save = findViewById(R.id.AddCandidateData);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsIntoCandidates(name,party,region,state);
                name.setText("");
                party.setText("");
                region.setText("");
                state.setText("");
            }
        });
    }

    private void InsIntoCandidates(EditText name, EditText party, EditText region, EditText state) {
        String cname = name.getText().toString();
        String cparty = party.getText().toString();
        String cstate = state.getText().toString();
        String cregion = region.getText().toString();
        db.insCandidates(cname,cparty,cstate,cregion);
        Toast.makeText(AddCandidates.this,"Data Added",Toast.LENGTH_LONG).show();

    }
}