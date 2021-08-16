package com.bVote.basicvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

public class AdminPage extends AppCompatActivity {

    Button aVoters,rVoters,aCandidates,rCandidates,delVisited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        aVoters = findViewById(R.id.AddVoters);
        rVoters = findViewById(R.id.RemoveVoters);
        aCandidates = findViewById(R.id.addCandi);
        rCandidates = findViewById(R.id.RemoveCandi);
        delVisited = findViewById(R.id.delVisited);

        aVoters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVoters();
            }
        });

        rVoters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeVoters();
            }
        });

        aCandidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCandidates();
            }
        });

        rCandidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCandidates();
            }
        });
        
        delVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelVisited();
            }
        });
    }

    private void DelVisited() {
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.removeVisited();
        Toast.makeText(this,"Visited deleted",Toast.LENGTH_LONG).show();
    }

    private void removeCandidates() {
        Intent intent = new Intent(this,RemoveCandidates.class);
        startActivity(intent);
    }

    private void addCandidates() {
        Intent intent = new Intent(this,AddCandidates.class);
        startActivity(intent);
    }

    private void removeVoters() {
        Intent intent = new Intent(this,RemoveVoter.class);
        startActivity(intent);
    }

    private void addVoters() {
        Intent intent = new Intent(this,AddVoter.class);
        startActivity(intent);
    }
}