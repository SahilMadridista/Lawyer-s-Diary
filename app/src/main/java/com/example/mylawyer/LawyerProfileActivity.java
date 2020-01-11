package com.example.mylawyer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.consts.SharedPrefConstants;
import com.example.mylawyer.interfaces.CasesModifier;
import com.example.mylawyer.model.Case;
import com.example.mylawyer.model.Lawyer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class LawyerProfileActivity extends AppCompatActivity implements CasesModifier {

    private static final String TAG = "ViewDetails";

    FirebaseAuth mAuth;
    TextView profile_name,profile_email,profile_phone;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog LawyerProfileActivityProgressDialog,pd;
    private DatabaseReference myref;
    private String userID;
    RecyclerView clientInfoRecyclerView;
    FirebaseFirestore firestore;
    LawyerCaseRecyclerViewAdapter adapter;
    Toolbar lawyer_profile_toolbar;
    private ArrayList<Case> searchedcases;
    private ArrayList<Case> casesInformationList;
    String Lawyer_Name, Lawyer_ID, Lawyer_Email;


    // Starting of OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);


        mAuth = FirebaseAuth.getInstance();
        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_email = (TextView)findViewById(R.id.profile_email);
        profile_phone = (TextView)findViewById(R.id.profile_phone);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //builder = new AlertDialog.Builder(LawyerProfileActivity.this);

        clientInfoRecyclerView = (RecyclerView)findViewById(R.id.client_info_recycler_view);
        clientInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();

        lawyer_profile_toolbar = findViewById(R.id.lawyer_profile_toolbar);
        setSupportActionBar(lawyer_profile_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // ProgressDialog stuff start

        LawyerProfileActivityProgressDialog = new ProgressDialog(this);
        pd = new ProgressDialog(this);
        LawyerProfileActivityProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        LawyerProfileActivityProgressDialog.setCancelable(false);
        pd.setCancelable(false);

        // ProgressDialog stuff ended

        userID = mAuth.getCurrentUser().getUid();

        // Start - Displaying user info on the profile page

        DocumentReference documentReference = firestore.collection("Lawyers").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //LawyerProfileActivityProgressDialog.show();

                profile_name.setText(documentSnapshot.getString("name"));
                profile_email.setText(documentSnapshot.getString("lawyerEmail"));
                profile_phone.setText(documentSnapshot.getString("lawyerPhone"));

                Lawyer_Name = profile_name.getText().toString().trim();
                Lawyer_ID = profile_phone.getText().toString().trim();
                Lawyer_Email = profile_email.getText().toString().trim();

                //LawyerProfileActivityProgressDialog.cancel();

            }
        });

        // End - Displaying user info on the profile page


       showData();

    }

    // End of OnCreate

    public void showData() {

        pd.setTitle("Loading information");
        pd.setMessage("Just a moment...");
        pd.show();

        firestore.collection("Lawyers").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Lawyer lawyer = documentSnapshot.toObject(Lawyer.class);

                casesInformationList = new ArrayList<>();

                adapter = new LawyerCaseRecyclerViewAdapter(
                        LawyerProfileActivity.this, casesInformationList, LawyerProfileActivity.this);
                clientInfoRecyclerView.setAdapter(adapter);

                pd.dismiss();

                for (String caseId : lawyer.clientsCasesList) {
                    firestore.collection("Cases").document(caseId).get().addOnSuccessListener(
                            new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    synchronized (casesInformationList) {
                                        casesInformationList.add(documentSnapshot.toObject(Case.class));
                                        adapter.notifyItemInserted(casesInformationList.size()-1);

                                    }
                                }
                            }
                    );
                }

            }
        });

        LawyerProfileActivityProgressDialog.cancel();

    }

    @Override
    public void addDetails(String clientName, Timestamp startTime, String caseID) {

        Intent adddetailsactivityintent = new Intent(this, CourtScenes.class);

        adddetailsactivityintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        adddetailsactivityintent.putExtra("Client Name", clientName);
        adddetailsactivityintent.putExtra("Start Date", startTime.getSeconds());
        adddetailsactivityintent.putExtra("CaseID",caseID);

        startActivity(adddetailsactivityintent);

    }

    @Override
    public void seeCaseHistory(String caseId){

        Intent seeDetailsIntent = new Intent(this,CaseHistory.class);

        seeDetailsIntent.putExtra("Case ID",caseId);

        startActivity(seeDetailsIntent);


    }

    @Override
    public void deleteSelectedCase(final String caseId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LawyerProfileActivity.this);
        builder.setMessage("Do you really want to delete this case ?")
                .setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firestore.collection("Cases").document(caseId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firestore.collection("Lawyers").document(userID)
                                        .update("clientsCasesList", FieldValue.arrayRemove(caseId))
                                        .addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(LawyerProfileActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                        casesInformationList.remove(getCaseIndex(casesInformationList, caseId));
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                        );
                            }
                        });

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();
    }

    // Start - Top 3 dots menu bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.searchcase);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Client's Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Called when we press search button

                searchClient(s);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {

                //Called when we change the text

                if(s.isEmpty()){
                    showData();
                }

                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchClient(String s) {

        LawyerProfileActivityProgressDialog.setTitle("Searching");
        LawyerProfileActivityProgressDialog.setMessage("Just a moment...");
        LawyerProfileActivityProgressDialog.show();

        firestore.collection("Cases").whereEqualTo("clientNameLowerCase",s.toLowerCase())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        final ArrayList<String> casesIdList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            casesIdList.add(documentSnapshot.toObject(Case.class).caseId);
                        }

                        Log.v("Client cases", casesIdList.toString());

                        if(casesIdList.isEmpty()) {
                            Toast.makeText(LawyerProfileActivity.this, "You don't have any case",
                                    Toast.LENGTH_SHORT).show();

                        }

                        searchedcases = new ArrayList<>();

                        clientInfoRecyclerView.setLayoutManager(new LinearLayoutManager(LawyerProfileActivity.this));

                        adapter = new LawyerCaseRecyclerViewAdapter(LawyerProfileActivity.this,
                                searchedcases,LawyerProfileActivity.this);
                        clientInfoRecyclerView.setAdapter(adapter);

                        LawyerProfileActivityProgressDialog.dismiss();

                        for (String caseId : casesIdList) {
                            firestore.collection("Cases").document(caseId).get().addOnSuccessListener(
                                    new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            synchronized (searchedcases) {
                                                searchedcases.add(documentSnapshot.toObject(Case.class));
                                                adapter.notifyItemInserted(searchedcases.size()-1);
                                                Toast.makeText(LawyerProfileActivity.this,
                                                        "Number of cases found with this name : " +
                                                                casesIdList.size(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                        }

                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.refresh:
                showData();
                //Toast.makeText(LawyerProfileActivity.this,"Notification Activity to be added if needed",Toast.LENGTH_SHORT).show();
                break;

            case R.id.add_staff_member:
                startActivity(new Intent(this, AddStaffMembersActivity.class));
                break;

            case R.id.see_staff_members:
                startActivity(new Intent(this,StaffInformation.class));
                break;

            case R.id.add_client:
                Intent intent = new Intent(LawyerProfileActivity.this,AddCaseActivity.class);
                intent.putExtra("Lawyer_Name", Lawyer_Name);
                intent.putExtra("Lawyer_Id", Lawyer_ID);
                intent.putExtra("Lawyer_Email",Lawyer_Email);
                startActivity(intent);
                break;

            case R.id.sign_out:
                mAuth.signOut();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("login", SharedPrefConstants.NO_LOGIN);
                editor.apply();
                finish();
                startActivity(new Intent(LawyerProfileActivity.this,MainActivity.class));


        }

        return super.onOptionsItemSelected(item);
    }

    // End - Top 3 dots menu bar

    // Start - OnStart method

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            //Stay on home page of the app
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    // End - OnStart method

    // Start - Back pressed function

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    // End - Back pressed function

    private int getCaseIndex(ArrayList<Case> cases, String caseId) {

        for (int i = 0; i < cases.size(); i++) {
            if (cases.get(i).caseId.equals(caseId)) {
                return i;
            }
        }
        return -1;
    }
}