package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Addclient extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    androidx.appcompat.widget.Toolbar add_client_toolbar;

    TextView date_text;
    EditText client_name,case_about,client_phone;
    Button add_client_button,choose_date_button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclient);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        client_name = (EditText)findViewById(R.id.client_name_edit_text);
        case_about = (EditText)findViewById(R.id.case_about_edit_text);
        client_phone = (EditText)findViewById(R.id.client_phone_edit_text);

        date_text = (TextView)findViewById(R.id.date_text_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Client");
        progressDialog.setMessage("Wait a moment...");

        add_client_button = (Button)findViewById(R.id.add_client_button);
        choose_date_button = (Button)findViewById(R.id.choose_date_button);

        add_client_toolbar = findViewById(R.id.add_client_toolbar);
        setSupportActionBar(add_client_toolbar);
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
                addClient();

            }
        });

    }

    private void addClient() {

        final String name = client_name.getText().toString().trim();
        final String case_details = case_about.getText().toString().trim();
        //final String date = date_text.getText().toString().trim();
        final String phone = client_phone.getText().toString().trim();
        String userID = mAuth.getCurrentUser().getUid();

        if(name.isEmpty()){
            client_name.setError("Name Required");
            client_name.requestFocus();
            return;
        }

        if(case_details.isEmpty()){
            case_about.setError("Case about can't be empty");
            case_about.requestFocus();
            return;
        }

//                if(date.isEmpty()){
//                    date_text.setError("Please select a date");
//                    date_text.requestFocus();
//                    return;
//                }

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

        Map<String,String> userMap = new HashMap<>();

        userMap.put("Name",name);
        userMap.put("Case About",case_details);
        //userMap.put("Date",date);
        userMap.put("Phone",phone);

        progressDialog.show();

        firestore.collection("Lawyer Clients").document(userID).collection("Clients")
                .add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(getApplicationContext(),"Client Added",Toast.LENGTH_SHORT).show();

                progressDialog.cancel();

                client_phone.setText(null);
                case_about.setText(null);
                //date_text.setText(null);
                client_name.setText(null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String error = e.getMessage();

                Toast.makeText(getApplicationContext(),"Error: "+ error,Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_MONTH,dayofmonth);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        date_text.setText(currentDateString);

    }


}
