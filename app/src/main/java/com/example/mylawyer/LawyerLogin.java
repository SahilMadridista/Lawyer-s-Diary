package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LawyerLogin extends AppCompatActivity {

    CheckBox login_checkbox;
    private EditText emailLoginEdittext,passwordLoginEdittext;

    androidx.appcompat.widget.Toolbar toolbar_lawyer_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_login);
        emailLoginEdittext = (EditText)findViewById(R.id.email_login_edit_text);
        passwordLoginEdittext = (EditText)findViewById(R.id.password_login_edit_text);

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
    }
}
