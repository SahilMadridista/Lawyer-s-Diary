package com.example.mylawyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylawyer.consts.SharedPrefConstants;
import com.example.mylawyer.model.Case;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Clienthomepage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    private String phonewithoutISD,name;
    private TextView phoneText,nameText;
    Button sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthomepage);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sign_out = (Button)findViewById(R.id.sign_out_button);
        phoneText = (TextView)findViewById(R.id.phoneText);
        nameText = (TextView)findViewById(R.id.nameText);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("login", SharedPrefConstants.NO_LOGIN);
                editor.apply();

                startActivity(new Intent(Clienthomepage.this,MainActivity.class));
                finish();
            }
        });

        name = getSharedPreferences("MyPref",MODE_PRIVATE).getString("Name","");

        phonewithoutISD = getSharedPreferences("MyPref",MODE_PRIVATE).getString("PhoneNumber","");

        nameText.setText(name);
        phoneText.setText(phonewithoutISD);

        firestore.collection("Cases").whereEqualTo("clientId",phonewithoutISD).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> casesIdList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            casesIdList.add(documentSnapshot.toObject(Case.class).caseId);
                        }

                        firestore.collection("Clients").document(phonewithoutISD)
                                .update("cases", casesIdList);

                        Toast.makeText(Clienthomepage.this, "Data obtained: " + casesIdList.size() + " cases", Toast.LENGTH_SHORT).show();
                        Log.v("Client cases", casesIdList.toString());
                    }
                });

    }

}