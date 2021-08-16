package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

import java.util.ArrayList;

public class RemoveCandidates extends AppCompatActivity {

    EditText name,party;
    Button RemoveCandidate;
    ListView ShowCandidate;
    DbHelper db;
    ArrayList<String> ListCandidate;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_candidates);

        name = findViewById(R.id.RemoveCandidateName);
        party = findViewById(R.id.RemoveCandidateParty);

        RemoveCandidate = findViewById(R.id.RemoveCandidates);
        ShowCandidate = findViewById(R.id.ShowCandidates);
        db = new DbHelper(this);

        ListCandidates();
        RemoveCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelCandidates();
                ListCandidates();
                name.setText("");
                party.setText("");
            }
        });
    }
    private void DelCandidates() {
        String cname = name.getText().toString();
        String cparty = party.getText().toString();
        boolean check = db.DeleteCandidates(cname,cparty);
        if (check)
            Toast.makeText(RemoveCandidates.this,cname+" Deleted successfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(RemoveCandidates.this,"Not Deleted",Toast.LENGTH_LONG).show();
       }

    private void ListCandidates() {
        ListCandidate = db.ShowCandi();
        if (ListCandidate != null) {
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ListCandidate);
            ShowCandidate.setAdapter(arrayAdapter);
        }
        else {
            Toast.makeText(this,"No Candidates available",Toast.LENGTH_LONG).show();
        }
    }
}