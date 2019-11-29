package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    ProgressDialog progressDialog;
    private Button lawyer_continue,client_continue;
    private long backpressedtime;
    private Toast backtoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lawyer_continue = (Button)findViewById(R.id.lawyer_continue_button);
        client_continue = (Button)findViewById(R.id.client_continue_button);

        lawyer_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LawyerSignUp.class));
            }
        });

        client_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Loading...");
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
//
//        Runnable progressRunnable = new Runnable() {
//            @Override
//            public void run() {
//                progressDialog.cancel();
//            }
//        };
//
//        Handler canceller = new Handler();
//        canceller.postDelayed(progressRunnable,1000);

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
