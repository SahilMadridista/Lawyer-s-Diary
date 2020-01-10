package com.example.mylawyer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.casehistorypackage.CaseHistoryRecyclerViewAdapter;
import com.example.mylawyer.model.CaseInformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class CaseHistory extends AppCompatActivity {

    String caseID;
    CaseHistoryRecyclerViewAdapter adapter;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history);

        toolbar  =findViewById(R.id.case_history_toolbar);
        setSupportActionBar(toolbar);

        caseID = getIntent().getExtras().getString("Case ID");

        RecyclerView recyclerView = findViewById(R.id.caseHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CaseHistoryRecyclerViewAdapter(getCaseInformation(caseID));
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<CaseInformation> getCaseInformation(String caseId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<CaseInformation> caseInformations = new ArrayList<>();

        db.collection("Cases").document(caseId).collection("Information")
                .orderBy("hearingDate").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            caseInformations.add(documentSnapshot.toObject(CaseInformation.class));
                        }

                        adapter.notifyDataSetChanged();

                    }
                });

        return caseInformations;

    }
}
