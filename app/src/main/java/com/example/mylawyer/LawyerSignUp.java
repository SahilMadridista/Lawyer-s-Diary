package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class LawyerSignUp extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar_lawyer_signup;
    private CheckBox registercheckbox;
    private EditText name,registeremail,registerphone,registerpassword;
    private TextView login_text;
    private Button signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_signup);

        registercheckbox = (CheckBox)findViewById(R.id.registercheckbox);
        name = (EditText)findViewById(R.id.name_edit_text);
        registeremail = (EditText)findViewById(R.id.email_edit_text);
        registerphone = (EditText)findViewById(R.id.phone_edit_text);
        registerpassword = (EditText)findViewById(R.id.password_edit_text);
        login_text = (TextView)findViewById(R.id.login_text_view);
        signupbutton = (Button)findViewById(R.id.sign_up_button);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LawyerSignUp.this,LawyerProfile.class));
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
}
