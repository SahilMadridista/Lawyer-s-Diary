package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LawyerLogin extends AppCompatActivity {

    CheckBox login_checkbox;
    private EditText emailLoginEdittext,passwordLoginEdittext;
    private Button login_button;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    androidx.appcompat.widget.Toolbar toolbar_lawyer_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_login);
        emailLoginEdittext = (EditText)findViewById(R.id.email_login_edit_text);
        passwordLoginEdittext = (EditText)findViewById(R.id.password_login_edit_text);
        login_button = (Button)findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        login_checkbox = (CheckBox)findViewById(R.id.login_checkbox);
        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    passwordLoginEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordLoginEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        toolbar_lawyer_login = findViewById(R.id.lawyer_login_toolbar);
        setSupportActionBar(toolbar_lawyer_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_the_user_in();
            }
        });
    }

    public void log_the_user_in(){

        String email = emailLoginEdittext.getText().toString().trim();
        String password = passwordLoginEdittext.getText().toString().trim();

        if(email.isEmpty()){
            emailLoginEdittext.setError("Email Required");
            emailLoginEdittext.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLoginEdittext.setError("Enter a valid email address");
            emailLoginEdittext.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordLoginEdittext.setError("Please enter password");
            passwordLoginEdittext.requestFocus();
            return;
        }

        if(password.length()<6){
            passwordLoginEdittext.setError("Password must be atleast 6 characters long");
            passwordLoginEdittext.requestFocus();
            return;
        }

        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("It will take a moment...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.cancel();

                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),LawyerProfile.class));
                            Toast.makeText(LawyerLogin.this,"You are Logged in",Toast.LENGTH_SHORT)
                                    .show();
                        } else{
                            Toast.makeText(LawyerLogin.this,"Re-check your email and password",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

}

