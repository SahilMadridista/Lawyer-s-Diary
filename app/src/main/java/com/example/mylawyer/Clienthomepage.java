package com.example.mylawyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.clientpackage.ClientCaseRecyclerViewAdapter;
import com.example.mylawyer.consts.SharedPrefConstants;
import com.example.mylawyer.interfaces.ClientCases;
import com.example.mylawyer.model.Case;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Clienthomepage extends AppCompatActivity implements ClientCases {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    private String phonewithoutISD, name;
    androidx.appcompat.widget.Toolbar chptoolbar;
    ClientCaseRecyclerViewAdapter adapter;
    private ArrayList<Case> cases;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthomepage);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        TextView phoneText = (TextView) findViewById(R.id.phoneText);
        TextView nameText = (TextView) findViewById(R.id.nameText);
        chptoolbar = findViewById(R.id.client_homepage_toolbar);
        setSupportActionBar(chptoolbar);
        getSupportActionBar().setTitle("Lawyer's Diary");
        pd = new ProgressDialog(this);
        pd.setCancelable(false);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.client_homepage_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        name = getSharedPreferences("MyPref", MODE_PRIVATE).getString("Name", "");
        phonewithoutISD = getSharedPreferences("MyPref", MODE_PRIVATE).getString("PhoneNumber", "");

        nameText.setText(name);
        phoneText.setText(phonewithoutISD);

        showClientCases();

    }

    private void showClientCases() {


        pd.setTitle("Searching for lawyer");
        pd.setMessage("Just a moment...");
        pd.show();

        firestore.collection("Cases").whereEqualTo("clientId", phonewithoutISD).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        final ArrayList<String> casesIdList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            casesIdList.add(documentSnapshot.toObject(Case.class).caseId);
                        }

                        firestore.collection("Clients").document(phonewithoutISD)
                                .update("cases", casesIdList);

                        Toast.makeText(Clienthomepage.this, "Number of cases found : " +
                                casesIdList.size(), Toast.LENGTH_SHORT).show();
                        Log.v("Client cases", casesIdList.toString());

                        if (casesIdList.isEmpty()) {
                            Toast.makeText(Clienthomepage.this, "You are not a client of any lawyer",
                                    Toast.LENGTH_SHORT).show();
                        }


                        cases = new ArrayList<>();

                        pd.dismiss();

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.client_homepage_recyclerview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Clienthomepage.this));

                        adapter = new ClientCaseRecyclerViewAdapter(Clienthomepage.this,
                                cases, Clienthomepage.this);
                        recyclerView.setAdapter(adapter);

                        for (String caseId : casesIdList) {
                            firestore.collection("Cases").document(caseId).get().addOnSuccessListener(
                                    new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            synchronized (cases) {
                                                cases.add(documentSnapshot.toObject(Case.class));
                                                adapter.notifyItemInserted(cases.size() - 1);
                                            }
                                        }
                                    }
                            );
                        }
                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.refresh:
                showClientCases();
                break;

            case R.id.sign_out:
                mAuth.signOut();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("login", SharedPrefConstants.NO_LOGIN);
                editor.apply();
                finish();
                startActivity(new Intent(Clienthomepage.this, MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void callLawyer(String lawyerPhone) {



    }

    @Override
    public void messageLawyer(String lawyerPhone) {



    }

    @Override
    public void emailLawyer(String lawyerEmail) {



    }

    @Override
    public void openCaseHistory(String caseID) {

        Intent seeDetailsintent = new Intent(Clienthomepage.this,CaseHistory.class);
        seeDetailsintent.putExtra("Case ID",caseID);
        startActivity(seeDetailsintent);

    }


}