package com.bVote.basicvoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bVote.basicvoting.Database.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    Button login,admin,results;
    ListView resultView;
    TextView resultText;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        admin = findViewById(R.id.admin);
        results = findViewById(R.id.results);
        resultView = findViewById(R.id.resultsView);
        resultText = findViewById(R.id.resultText);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLoginPage();
            }
        });

        BiometricManager biometricManager = BiometricManager.from(MainActivity.this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(MainActivity.this,"No hardware found",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(MainActivity.this,"Not Enrolled",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(MainActivity.this,"No hardware ",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Toast.makeText(MainActivity.this,"Security error",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Toast.makeText(MainActivity.this,"biometric error",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Toast.makeText(MainActivity.this,"unknown",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                //Toast.makeText(MainActivity.this,"FingerPrint Found",Toast.LENGTH_LONG).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(MainActivity.this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                GoToAdminPage();
            }


        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Admin Authorization").setDescription("Admin Login").setNegativeButtonText("cancel").build();

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
        
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowResults();
            }
        });
    }

    private void ShowResults() {
        DbHelper db = new DbHelper(this);
        ArrayList<String> results = db.getResultVotes();
        if (!results.isEmpty()) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, (List) results);
            resultView.setAdapter(arrayAdapter);
            resultText.setText("The candidate with highest vote is the winner");
            Toast.makeText(this, "All votes are here the highest is the winner", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"No result",Toast.LENGTH_LONG).show();
        }
    }

    private void GoToAdminPage() {
        Intent intent = new Intent(this, AdminPage.class);
        startActivity(intent);
    }


    private void GoToLoginPage() {
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
    }
}