package com.example.mylawyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylawyer.model.CaseInformation;
import com.example.mylawyer.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.mylawyer.utils.Utils.convertDateStringToMillis;

public class CourtScenes extends AppCompatActivity{

    androidx.appcompat.widget.Toolbar court_scenes_toolbar;
    String Client_Name;
    TextView client_name,start_date;
    Button add_information;
    EditText AssignedLawyerEdittext, WhatHappenedinCourtEdittext;
    EditText HearingDateDayEdittext, HearingDateMonthEdittext, HearingDateYearEdittext;
    EditText NextDateDayEdittext, NextDateMonthEdittext, NextDateYearEdittext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    String HDD,HDM,HDY;
    String NDD,NDM,NDY;
    Spinner HDmonthSpinner,NDmonthSpinner;

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
        progressDialog.setTitle("Adding Case information");
        progressDialog.setMessage("Wait a moment...");

        client_name = (TextView)findViewById(R.id.client_name_add_scene);

        AssignedLawyerEdittext = (EditText)findViewById(R.id.assigned_lawyer);
        WhatHappenedinCourtEdittext = (EditText)findViewById(R.id.what_happened_edittext);

        HearingDateDayEdittext = (EditText)findViewById(R.id.HearingDateDay);
        //HearingDateMonthEdittext = (EditText)findViewById(R.id.HearingDateMonth);
        HearingDateYearEdittext = (EditText)findViewById(R.id.HearingDateYear);

        NextDateDayEdittext = (EditText)findViewById(R.id.NextDateDay);
        //NextDateMonthEdittext = (EditText)findViewById(R.id.NextDateMonth);
        NextDateYearEdittext = (EditText)findViewById(R.id.NextDateYear);

        HDmonthSpinner = (Spinner)findViewById(R.id.hearingdatemonthspinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HDmonthSpinner.setAdapter(adapter);

        NDmonthSpinner = (Spinner)findViewById(R.id.nextdatemonthspinner);

        ArrayAdapter<CharSequence> secondadapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        NDmonthSpinner.setAdapter(secondadapter);




        add_information = (Button)findViewById(R.id.add_info_button);
        add_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addInformation();

            }
        });

        Client_Name = getIntent().getExtras().getString("Client Name");
        long caseStartDateInSeconds = getIntent().getExtras().getLong("Start Date");

        client_name.setText(Client_Name);
        start_date = findViewById(R.id.case_start_date_add_scene);
        start_date.setText(Utils.convertMillisToDateString(caseStartDateInSeconds*1000));

    }

    private void addInformation() {

        HDD = HearingDateDayEdittext.getText().toString().trim();
        HDM = HDmonthSpinner.getSelectedItem().toString().trim();
        HDY = HearingDateYearEdittext.getText().toString().trim();

        NDD = NextDateDayEdittext.getText().toString().trim();
        NDM = NDmonthSpinner.getSelectedItem().toString().trim();
        NDY = NextDateYearEdittext.getText().toString().trim();

        String caseID = getIntent().getExtras().getString("CaseID");

        final String assigned_lawyer_name = AssignedLawyerEdittext.getText().toString().trim();
        final String what_happened_in_court = WhatHappenedinCourtEdittext.getText().toString().trim();
        final String FullHearingDate = HDD + " " + HDM + " " + HDY;
        final String FullNextHearingDate = NDD + " " + NDM + " " + NDY;

        if(assigned_lawyer_name.isEmpty()){
            AssignedLawyerEdittext.setError("Please Enter Lawyer's Name");
            AssignedLawyerEdittext.requestFocus();
            return;
        }

        if(HDD.isEmpty()){
            HearingDateDayEdittext.setError("Can't be empty");
            HearingDateDayEdittext.requestFocus();
            return;
        }

        if(HDM.equals("Month")){
            Toast.makeText(this,"Please select a month",Toast.LENGTH_SHORT).show();
            return;
        }

        if(HDY.isEmpty()){
            HearingDateYearEdittext.setError("Can't be empty");
            HearingDateYearEdittext.requestFocus();
            return;
        }


        if(what_happened_in_court.isEmpty()){
            WhatHappenedinCourtEdittext.setError("This can't be empty");
            WhatHappenedinCourtEdittext.requestFocus();
            return;
        }

        if(NDD.isEmpty()){
            NextDateDayEdittext.setError("Can't be empty");
            NextDateDayEdittext.requestFocus();
            return;
        }

        if(NDM.equals("Month")){
            Toast.makeText(this,"Please select a month for next date",Toast.LENGTH_SHORT).show();
            return;
        }

        if(NDY.isEmpty()){
            NextDateYearEdittext.setError("Can't be empty");
            NextDateYearEdittext.requestFocus();
            return;
        }

        CaseInformation caseInformation = new CaseInformation();

        caseInformation.hearingDate = convertDateStringToMillis(FullHearingDate);
        caseInformation.assignedLawyer = assigned_lawyer_name;
        caseInformation.whatHappenedInCourt = what_happened_in_court;
        caseInformation.nextHearingDate = convertDateStringToMillis(FullNextHearingDate);

        progressDialog.show();
        progressDialog.setCancelable(false);

        firestore.collection("Cases").document(caseID)
                .collection("Information").add(caseInformation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(CourtScenes.this,"Information Stored",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CourtScenes.this,LawyerProfileActivity.class));
                        progressDialog.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CourtScenes.this,"Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();


                    }
                });


    }
}
