package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import javax.annotation.Nullable;

public class LawyerProfile extends AppCompatActivity {

    private static final String TAG = "ViewDetails";

    FirebaseAuth mAuth;
    TextView profile_name,profile_email,profile_phone;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private DatabaseReference myref;
    private String userID;
    RecyclerView client_info_recycler_view;
    FirebaseFirestore firestore;
    androidx.appcompat.widget.Toolbar lawyer_profile_toolbar;


    // Starting of OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);

        mAuth = FirebaseAuth.getInstance();
        myref = mFirebaseDatabase.getInstance().getReference();
        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_email = (TextView)findViewById(R.id.profile_email);
        profile_phone = (TextView)findViewById(R.id.profile_phone);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        client_info_recycler_view = (RecyclerView)findViewById(R.id.client_info_recycler_view);

        firestore = FirebaseFirestore.getInstance();

        lawyer_profile_toolbar = findViewById(R.id.lawyer_profile_toolbar);
        setSupportActionBar(lawyer_profile_toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading information");
        progressDialog.setMessage("Just a moment...");

        userID = mAuth.getCurrentUser().getUid();

        // Start - Displaying user info on the profile page

        DocumentReference documentReference = firestore.collection("Lawyers").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                progressDialog.show();

                profile_name.setText(documentSnapshot.getString("Name"));
                profile_email.setText(documentSnapshot.getString("Email"));
                profile_phone.setText(documentSnapshot.getString("Phone"));

                progressDialog.cancel();

            }
        });

        // End - Displaying user info on the profile page

    }

    // End of OnCreate

    // Start - Top 3 dots menu bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.notifications:
                Toast.makeText(LawyerProfile.this,"Notification Activity to be added if needed",Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_unique_id:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Unique ID of your Lawyer "+profile_name.getText().toString()+" is : " + userID;
                String shareSub = "Use this ID connect to your Lawyer";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent,"Share via"));
                break;

            case R.id.add_staff_member:
                startActivity(new Intent(this,Addstaffmembers.class));
                break;

            case R.id.see_staff_members:
                startActivity(new Intent(this,StaffInformation.class));
                break;

            case R.id.add_client:
                startActivity(new Intent(this,Addclient.class));
                break;

            case R.id.sign_out:
                mAuth.signOut();
                startActivity(new Intent(LawyerProfile.this,MainActivity.class));

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
            startActivity(new Intent(LawyerProfile.this,MainActivity.class));
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

}