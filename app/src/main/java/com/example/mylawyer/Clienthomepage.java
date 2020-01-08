package com.example.mylawyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Clienthomepage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    private String phonewithoutISD;
    private TextView phoneText;
    Button sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthomepage);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sign_out = (Button)findViewById(R.id.sign_out_button);
        phoneText = (TextView)findViewById(R.id.phoneText);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Clienthomepage.this,MainActivity.class));
            }
        });

        phonewithoutISD = getIntent().getExtras().getString("phoneWithoutISD");

        phoneText.setText(phonewithoutISD);

        firestore.collection("Cases").whereEqualTo("clientId",phonewithoutISD).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(Clienthomepage.this,"Matched",Toast.LENGTH_SHORT).show();

                        }
                        else{

                            Toast.makeText(Clienthomepage.this,"Number not Matched",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}