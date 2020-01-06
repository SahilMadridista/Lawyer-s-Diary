package com.example.mylawyer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mylawyer.model.Case;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCaseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    androidx.appcompat.widget.Toolbar add_client_toolbar;

    TextView date_text;
    EditText client_name,case_about,client_phone,client_aadhar;
    Button add_client_button,choose_date_button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    private Timestamp caseStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclient);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        client_name = (EditText)findViewById(R.id.client_name_edit_text);
        case_about = (EditText)findViewById(R.id.client_case_about_edittext);
        client_phone = (EditText)findViewById(R.id.client_phone_edit_text);
        client_aadhar = (EditText)findViewById(R.id.client_aadhar_number);

        date_text = findViewById(R.id.date_text_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Client");
        progressDialog.setMessage("Wait a moment...");
        progressDialog.setCancelable(false);

        add_client_button = (Button)findViewById(R.id.add_client_button);
        choose_date_button = (Button)findViewById(R.id.choose_date_button);

        add_client_toolbar = findViewById(R.id.add_client_toolbar);
        setSupportActionBar(add_client_toolbar);
        getSupportActionBar().setTitle("Add your client");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        choose_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        add_client_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtheClient();
                //testFirestore();

            }
        });

    }



    private void addtheClient() {

        final String name = client_name.getText().toString().trim();
        final String clientlowercasename = client_name.getText().toString().toLowerCase().trim();
        final String caseDescription = case_about.getText().toString().trim();
        final String date = date_text.getText().toString().trim();
        final String phone = client_phone.getText().toString().trim();
        final String client_aadhar_number = client_aadhar.getText().toString().trim();
        //String userID = mAuth.getCurrentUser().getUid();
        final ArrayList<Integer> caseIdList = new ArrayList<>();

        if(name.isEmpty()){
            client_name.setError("Name Required");
            client_name.requestFocus();
            return;
        }

        if(caseDescription.isEmpty()){
            case_about.setError("Description can't be empty");
            case_about.requestFocus();
            return;
        }

        if(date.isEmpty()){
            Toast.makeText(AddCaseActivity.this,"Please choose case starting date",Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone.isEmpty()){
            client_phone.setError("Phone number Required");
            client_phone.requestFocus();
            return;
        }

        if(phone.length()!=10){
            client_phone.setError("Invalid phone number");
            client_phone.requestFocus();
            return;
        }

        if(client_aadhar_number.length()!=12){
            client_aadhar.setError("Please enter correct aadhar number");
            client_aadhar.requestFocus();
            return;
        }

        Case clientCase = new Case();

        clientCase.clientName = name;
        clientCase.caseDescription = caseDescription;
        clientCase.startTime = caseStartTime;
        clientCase.clientId = phone;
        clientCase.clientAadhar = client_aadhar_number;

        final DocumentReference ref = firestore.collection("Cases").document();

        progressDialog.show();

//        firestore.collection("Lawyers").document(mAuth.getCurrentUser().getUid()).collection("Clients")
//                .add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//                Toast.makeText(getApplicationContext(),"Client Added",Toast.LENGTH_SHORT).show();
//
//                progressDialog.cancel();
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                String error = e.getMessage();
//
//                Toast.makeText(getApplicationContext(),"Error: "+ error,Toast.LENGTH_SHORT).show();
//
//            }
//        });

        clientCase.caseId = ref.getId();

        ref.set(clientCase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String lawyerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        firestore.collection("Lawyers").document(lawyerId)
                                .update("clientsCasesList", FieldValue.arrayUnion(ref.getId()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.cancel();
                                        Toast.makeText(AddCaseActivity.this,"Stored in lawyer list",Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Toast.makeText(AddCaseActivity.this,"Stored the data",Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {

        Calendar c = Calendar.getInstance();
        caseStartTime = new Timestamp(c.getTimeInMillis()/1000, 0);

        c.set(Calendar.DAY_OF_MONTH,dayofmonth);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        date_text.setText(currentDateString);

    }

}
