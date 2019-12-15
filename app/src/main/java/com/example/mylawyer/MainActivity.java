package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        lawyer_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LawyerSignUp.class));
            }
        });

        client_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ClientSignup.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

//        if(mAuth.getCurrentUser()!=null){
//            finish();
//            startActivity(new Intent(getApplicationContext(),LawyerProfile.class));
//        }

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
