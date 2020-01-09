package com.example.mylawyer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylawyer.consts.SharedPrefConstants;
import com.example.mylawyer.model.Lawyer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LawyerSignUp extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar_lawyer_signup;
    private CheckBox registercheckbox;
    private EditText registername,registeremail,registerphone,registerpassword,registeraadhar;
    private Button signupbutton;
    private FirebaseAuth mAuth;
    private TextView login_text;
    private ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    String LawyeruserID;
//    ArrayList<Integer> clientid = new ArrayList<>();
//    static int n;
//    private static final int PICK_IMAGE=1;
//    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_signup);

        registercheckbox = (CheckBox)findViewById(R.id.registercheckbox);
        registername = (EditText)findViewById(R.id.name_edit_text);
        registeremail = (EditText)findViewById(R.id.email_edit_text);
        registerphone = (EditText)findViewById(R.id.phone_edit_text);
        registerpassword = (EditText)findViewById(R.id.password_edit_text);
        registeraadhar = (EditText)findViewById(R.id.lawyer_aadhar);
        signupbutton = (Button)findViewById(R.id.sign_up_button);
        login_text = (TextView) findViewById(R.id.login_text);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


        toolbar_lawyer_signup = findViewById(R.id.lawyer_signup_toolbar);
        setSupportActionBar(toolbar_lawyer_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LawyerSignUp.this,LawyerLogin.class));
            }
        });

        registercheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    registerpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    registerpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(getApplicationContext(), LawyerProfileActivity.class));
        }
    }

    @SuppressLint("NewApi")
    private void registerUser(){

        final String name = registername.getText().toString().trim();
        final String namelowercase = registername.getText().toString().toLowerCase().trim();
        final String password = registerpassword.getText().toString().trim();
        final String phone = registerphone.getText().toString().trim();
        final String aadhar = registeraadhar.getText().toString().trim();


        if(name.isEmpty()){
            registername.setError("Name Required");
            registername.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            registerphone.setError("Phone number Required");
            registerphone.requestFocus();
            return;
        }

        if(phone.length()!=10){
            registerphone.setError("Invalid phone number");
            registerphone.requestFocus();
            return;
        }

        final String email = registeremail.getText().toString().trim();
        if(email.isEmpty()){
            registeremail.setError("Email Required");
            registeremail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registeremail.setError("Enter a valid email address");
            registeremail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            registerpassword.setError("Please enter password");
            registerpassword.requestFocus();
            return;
        }

        if(password.length()<6){
            registerpassword.setError("Password must be atleast 6 characters long");
            registerpassword.requestFocus();
            return;
        }

        if(aadhar.length()!=12){
            registeraadhar.setError("Enter correct Aadhar number");
            registeraadhar.requestFocus();
            return;
        }

        progressDialog.setTitle("Creating profile");
        progressDialog.setMessage("Please wait a moment...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                            Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_SHORT).show();

                            progressDialog.cancel();
                            LawyeruserID = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = firestore.collection("Lawyers").document(LawyeruserID);

                            Lawyer lawyer = new Lawyer();
                            lawyer.name = name;
                            lawyer.lawyerEmail = email;
                            lawyer.lawyerPhone = phone;
                            lawyer.lawyerAadhar = aadhar;
                            lawyer.lowerCaseName = name.toLowerCase();

                            documentReference.set(lawyer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Profile Created",Toast.LENGTH_SHORT).show();

                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt("login", SharedPrefConstants.LAWYER_LOGIN);
                                    editor.apply();

                                    startActivity(new Intent(LawyerSignUp.this, LawyerProfileActivity.class));
                                }
                            });


                        } else {

                            Toast.makeText(LawyerSignUp.this,
                                    "Could not register , Please try again !!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                        }
                    }
                });
    }
}
