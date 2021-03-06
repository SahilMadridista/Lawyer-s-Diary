package com.example.mylawyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylawyer.consts.SharedPrefConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button lawyer_continue,client_continue;
    private long backpressedtime;
    private Toast backtoast;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lawyer_continue = (Button)findViewById(R.id.lawyer_continue_button);
        client_continue = (Button)findViewById(R.id.client_continue_button);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        final int loginStatus = preferences.getInt("login", SharedPrefConstants.NO_LOGIN);

        lawyer_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginStatus == SharedPrefConstants.CLIENT_LOGIN) {
                    Toast.makeText(MainActivity.this, "Client logged in", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, LawyerSignUp.class));
                    finish();
                }
            }
        });

        client_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginStatus == SharedPrefConstants.LAWYER_LOGIN) {
                    Toast.makeText(MainActivity.this, "Lawyer logged in", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, ClientSignup.class));
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(backpressedtime+2000>System.currentTimeMillis()){
            backtoast.cancel();
            super.onBackPressed();
            finish();
        }
        else {
            backtoast= Toast.makeText(MainActivity.this,"Press back again to exit",Toast.
                    LENGTH_SHORT);
            backtoast.show();
        }
        backpressedtime = System.currentTimeMillis();
    }
}
