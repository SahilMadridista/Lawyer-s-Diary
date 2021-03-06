package com.example.mylawyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylawyer.consts.SharedPrefConstants;
import com.example.mylawyer.model.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Clientotpverification extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar otp_verify_toolbar;
    EditText otp_edittext;
    Button verify_button;
    String phone;
    String phonewithoutISD;
    String name;
    String codeSentToUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    private ProgressBar progressBar;
    FirebaseAuthSettings firebaseAuthSettings;
    String clientuserID;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientotpverification);

        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);


        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Just a moment...");
        progressDialog.setCancelable(false);

        otp_edittext = (EditText)findViewById(R.id.otp_edittext);
        verify_button = (Button)findViewById(R.id.verify_button);

        otp_verify_toolbar = findViewById(R.id.client_otp_verify_toolbar);
        setSupportActionBar(otp_verify_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        name = getIntent().getExtras().getString("name");
        phone = getIntent().getExtras().getString("phonenumber");
        phonewithoutISD = getIntent().getExtras().getString("phonewithoutISD");
        sendVerificationCode(phone); //Method to send otp

        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phone, codeSentToUser);

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code= otp_edittext.getText().toString().trim();
                if(code.length()!=6)
                {
                    otp_edittext.setError("Error Code");
                    otp_edittext.requestFocus();
                    return;
                }

                progressDialog.show();
                verifyOTPcode(code);

            }
        });

    }

    private void verifyOTPcode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSentToUser , code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            progressDialog.show();

                            Client clientinfo = new Client();

                            clientinfo.clientName = name;
                            clientinfo.clientPhone = phonewithoutISD;

                            final DocumentReference documentReference = firestore.collection("Clients")
                                    .document(phonewithoutISD);

                            documentReference.set(clientinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    //Toast.makeText(Clientotpverification.this, "Logged In Successfully",Toast.LENGTH_SHORT).show();

                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt("login", SharedPrefConstants.CLIENT_LOGIN);
                                    editor.putString("Name",name);
                                    editor.putString("PhoneNumber",phonewithoutISD);
                                    editor.apply();

                                    Intent intent = new Intent(Clientotpverification.this,Clienthomepage.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                        }
                        else {
                            if( task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                                progressDialog.cancel();
                                Toast.makeText(Clientotpverification.this, "Incorrect Verification Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(String phone)
    {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                100,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider
            .OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null) {
                otp_edittext.setText(code);
                verifyOTPcode(code);
            }
        }
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSentToUser =  s;
            progressDialog.show();

            //OTP that is sent to the user
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException){
                Toast.makeText(Clientotpverification.this, "Invalid Number", Toast.LENGTH_SHORT).show();
            }else if (e instanceof FirebaseTooManyRequestsException){
                Toast.makeText(Clientotpverification.this, "Too many Request", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Clientotpverification.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

}
