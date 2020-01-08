package com.example.mylawyer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddStaffMembersActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar add_staff_toolbar;
    Spinner member_posts;
    Button add_member;
    EditText name_edit_text,phone_edit_text,staff_aadhar_edit_text;
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
        staff_aadhar_edit_text = (EditText)findViewById(R.id.staff_aadhar_number);
        member_posts = (Spinner)findViewById(R.id.staff_member_post_spinner);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Adding Member");
        progressDialog.setMessage("Just a moment...");

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        add_staff_toolbar = findViewById(R.id.add_members_toolbar);
        setSupportActionBar(add_staff_toolbar);
        getSupportActionBar().setTitle("Add your staff");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.posts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_posts.setAdapter(adapter);



        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveStaffMember();   // Method for adding member in staff

            }
        });

    }

    private void saveStaffMember() {

        final String name = name_edit_text.getText().toString().trim();
        final String stafflowercasename = name_edit_text.getText().toString().toLowerCase().trim();
        final String post = member_posts.getSelectedItem().toString().trim();
        final String phone = phone_edit_text.getText().toString().trim();
        final String staff_aadhar = staff_aadhar_edit_text.getText().toString().trim();
        String userID = mAuth.getCurrentUser().getUid();

        if(name.isEmpty()){
            name_edit_text.setError("Please enter name");
            name_edit_text.requestFocus();
            return;
        }

        if(post.equals("Select a post")){
            Toast.makeText(AddStaffMembersActivity.this,"Please select a post",Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone.isEmpty()){
            phone_edit_text.setError("Phone number Required");
            phone_edit_text.requestFocus();
            return;
        }

        if(phone.length()!=10){
            phone_edit_text.setError("Invalid phone number");
            phone_edit_text.requestFocus();
            return;
        }

        if(staff_aadhar.length()!=12){
            staff_aadhar_edit_text.setError("Please eenter correct Aadhar number");
            staff_aadhar_edit_text.requestFocus();
            return;
        }

        Map<String,Object> userMap = new HashMap<>();

        userMap.put("Name",name);
        userMap.put("Post",post);
        userMap.put("Phone",phone);
        userMap.put("Aadhar",staff_aadhar);
        userMap.put("Staff Lowercase Name",stafflowercasename);

        progressDialog.show();

        firestore.collection("Lawyers").document(userID).collection("Staff Members")
                .add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(getApplicationContext(),"Staff Member Added",Toast.LENGTH_SHORT).show();

                progressDialog.cancel();

                phone_edit_text.setText(null);
                name_edit_text.setText(null);
                staff_aadhar_edit_text.setText(null);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String error = e.getMessage();
                Toast.makeText(getApplicationContext(),"Error: "+ error,Toast.LENGTH_SHORT).show();

            }
        });


    }

}
