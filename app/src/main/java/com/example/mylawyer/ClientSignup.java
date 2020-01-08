package com.example.mylawyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClientSignup extends AppCompatActivity {

    private EditText client_name,client_phone;
    private Button client_verify_button;
    private String phonenumber;
    private FirebaseAuth mAuth;

    androidx.appcompat.widget.Toolbar toolbar_client_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup);

        mAuth = FirebaseAuth.getInstance();


        client_name = (EditText)findViewById(R.id.client_name_edit_text);
        client_phone = (EditText)findViewById(R.id.client_phone_edit_text);

        client_verify_button = (Button)findViewById(R.id.verify_client_buton);


        toolbar_client_signup = findViewById(R.id.client_signup_toolbar);
        setSupportActionBar(toolbar_client_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        client_verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToOTPpage();

            }
        });

    }

    private void goToOTPpage() {

        final String name = client_name.getText().toString().trim();
        final String phone = client_phone.getText().toString().trim();

        if(name.isEmpty()){
            client_name.setError("Name Required");
            client_name.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            client_phone.setError("Phone number Required");
            client_phone.requestFocus();
            return;
        }

        if(phone.length()!=10){
            client_phone.setError("Enter a valid phone number");
            client_phone.requestFocus();
            return;
        }

        phonenumber = "+91"+phone;

        Intent intent = new Intent(ClientSignup.this, Clientotpverification.class);
        intent.putExtra("phonewithoutISD",phone);
        intent.putExtra("phonenumber",phonenumber);
        intent.putExtra("name",name);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(ClientSignup.this,Clienthomepage.class));
        }
    }
}
