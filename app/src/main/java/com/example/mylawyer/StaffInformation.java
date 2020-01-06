package com.example.mylawyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

public class StaffInformation extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar staff_info_toolbar;
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String userID;
    ArrayList<Staffmembers> staffmembersArrayList = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    // Start - OnCreate method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_information);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();


        staff_info_toolbar = findViewById(R.id.staff_member_toolbar);
        setSupportActionBar(staff_info_toolbar);
        getSupportActionBar().setTitle("Your Staff");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.member_info_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addTestdatatoFirebase();
        showStaffData();

    }

    // End - OnCreate method


    // Start - ShowData method

    public void showStaffData() {

        if(staffmembersArrayList.size()>0)
            staffmembersArrayList.clear();

        firestore.collection("Lawyers").document(userID).collection("Staff Members")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot querySnapshot : task.getResult()){

                            Staffmembers staffmembers = new Staffmembers(querySnapshot.getId()
                                    ,querySnapshot.getString("Name")
                                    ,querySnapshot.getString("Post")
                                    ,querySnapshot.getString("Phone")
                                    ,querySnapshot.getString("Aadhar"));

                            staffmembersArrayList.add(staffmembers);

                        }

                        adapter = new MyRecyclerViewAdapter(StaffInformation.this,staffmembersArrayList);
                        recyclerView.setAdapter(adapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(StaffInformation.this,"Failed",Toast.LENGTH_SHORT).show();

            }
        });

    }

    // End - ShowData method

    private void addTestdatatoFirebase(){

        Random random =  new Random();
        for(int i = 0;i<2;i++){


            Map<String, String> dataMap = new HashMap<>();

            dataMap.put("Name", "" + random.nextInt(50));
            dataMap.put("Post", "" + random.nextInt(50));
            dataMap.put("Phone", "" + random.nextInt(50));
            dataMap.put("Aadhar","" + random.nextInt(50));

            firestore.collection("Lawyers").document(userID).collection("Staff Members")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                            @Nullable FirebaseFirestoreException e) {

//                            Toast.makeText(StaffInformation.this,"Opening Staff List",Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }

}
