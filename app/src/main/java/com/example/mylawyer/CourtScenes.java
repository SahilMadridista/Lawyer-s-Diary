package com.example.mylawyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CourtScenes extends AppCompatActivity{

    androidx.appcompat.widget.Toolbar court_scenes_toolbar;
    String Client_Name;
    String Client_Case_Start_Date;
    TextView client_name,start_date;
    EditText assigned_lawyer,what_happened,current_hearing_date,next_hearing_date;
    Button add_information;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_scenes);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        court_scenes_toolbar = findViewById(R.id.court_scenes_toolbar);
        setSupportActionBar(court_scenes_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("What happened in Court");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Client information");
        progressDialog.setMessage("Wait a moment...");

        client_name = (TextView)findViewById(R.id.client_name_add_scene);
        start_date = (TextView)findViewById(R.id.case_start_date_add_scene);

        assigned_lawyer = (EditText)findViewById(R.id.assigned_lawyer);
        what_happened = (EditText)findViewById(R.id.what_happened_edittext);
        current_hearing_date = (EditText)findViewById(R.id.current_date_edittext);
        next_hearing_date = (EditText)findViewById(R.id.next_date_edittext);


        add_information = (Button)findViewById(R.id.add_info_button);
        add_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addInformation();

            }
        });

        Client_Name = getIntent().getExtras().getString("Client Name");
        Client_Case_Start_Date = getIntent().getExtras().getString("Start Date");


        client_name.setText(Client_Name);
        start_date.setText(Client_Case_Start_Date);

    }

    private void addInformation() {

        final String assigned_lawyer_name = assigned_lawyer.getText().toString().trim();
        final String hearing_date_text = current_hearing_date.getText().toString().trim();
        final String next_date = next_hearing_date.getText().toString().trim();
        final String what_happened_in_court = what_happened.getText().toString().trim();

        if(assigned_lawyer_name.isEmpty()){
            assigned_lawyer.setError("Please enter name");
            assigned_lawyer.requestFocus();
            return;
        }

        if(hearing_date_text.isEmpty()){
            current_hearing_date.setError("Enter date");
            current_hearing_date.requestFocus();
            return;
        }

        if(next_date.isEmpty()){
            next_hearing_date.setError("Enter date");
            next_hearing_date.requestFocus();
            return;
        }

        if(what_happened_in_court.isEmpty()){
            what_happened.setError("This can't be empty");
            what_happened.requestFocus();
            return;
        }

        Map<String,Object> userMap = new HashMap<>();

        userMap.put("Assigned Lawyer",assigned_lawyer_name);
        userMap.put("Hearing Date",hearing_date_text);
        userMap.put("Next Date",next_date);
        userMap.put("What happened in court",what_happened_in_court);

        progressDialog.show();

        String uID = mAuth.getCurrentUser().getUid();



        firestore.collection("Lawyers").document(uID)
                .collection("Clients").document(mAuth.getCurrentUser().getUid())
                .collection("Court Scenes").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(CourtScenes.this,"Information Added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CourtScenes.this, LawyerProfileActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CourtScenes.this,"Some error occured, Please try again",Toast.LENGTH_SHORT).show();

            }
        });





    }

}
