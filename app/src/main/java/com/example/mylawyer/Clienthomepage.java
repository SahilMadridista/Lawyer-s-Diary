package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Clienthomepage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    private String phonewithoutISD;

    Button sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthomepage);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sign_out = (Button)findViewById(R.id.sign_out_button);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        //phonewithoutISD = getIntent().getExtras().getString("phoneWithoutISD");

        identifyClient();

    }

    private void identifyClient() {

        firestore.collection("Lawyer Clients").whereEqualTo("Phone",phonewithoutISD).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            //functionForDataRetreiving();

                            Toast.makeText(Clienthomepage.this,"Identified",Toast.LENGTH_SHORT).show();

                        }
                        else {

                            Toast.makeText(Clienthomepage.this,"You are not a client of any Lawyer",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}
