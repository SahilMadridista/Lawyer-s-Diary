package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Addstaffmembers extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar add_staff_toolbar;
    Spinner member_posts;
    Button add_member;
    EditText name_edit_text,phone_edit_text;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
//    private static final int PICK_IMAGE=1;
//    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstaffmembers);

        add_member = (Button)findViewById(R.id.addmemberbutton);
        name_edit_text = (EditText)findViewById(R.id.add_member_name);
        phone_edit_text = (EditText)findViewById(R.id.member_phone);
        member_posts = (Spinner)findViewById(R.id.staff_member_post_spinner);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Adding Member");
        progressDialog.setMessage("Just a moment...");

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        add_staff_toolbar = findViewById(R.id.add_members_toolbar);
        setSupportActionBar(add_staff_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.posts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_posts.setAdapter(adapter);

        // Start - Method for adding member in staff

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = name_edit_text.getText().toString().trim();
                final String post = member_posts.getSelectedItem().toString().trim();
                final String phone = phone_edit_text.getText().toString().trim();
                String userID = mAuth.getCurrentUser().getUid();

                Map<String,Object> userMap = new HashMap<>();

                userMap.put("Name",name);
                userMap.put("Post",post);
                userMap.put("Phone",phone);

                progressDialog.show();

                firestore.collection("Lawyers").document(userID).collection("Staff Members").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getApplicationContext(),"User Added",Toast.LENGTH_SHORT).show();

                        progressDialog.cancel();

                        name_edit_text.setText(null);
                        phone_edit_text.setText(null);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        String error = e.getMessage();
                        Toast.makeText(getApplicationContext(),"Error: "+ error,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        // End - Method for adding member in staff

    }

}
