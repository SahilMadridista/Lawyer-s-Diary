package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LawyerProfile extends AppCompatActivity {
    private Button signout;
    FirebaseAuth mAuth;
    TextView profile_text;
    FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);

        signout = (Button)findViewById(R.id.signout_button);
        mAuth = FirebaseAuth.getInstance();
        profile_text = (TextView)findViewById(R.id.name_text);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Lawyers");
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading information");
        progressDialog.setMessage("Just a moment...");
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                profile_text.setText("Welcome " + name);
                progressDialog.cancel();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(LawyerProfile.this,"You are signed out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LawyerProfile.this,MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
