package com.example.mylawyer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String UID = mAuth.getCurrentUser().getUid();


    StaffInformation staffInformation;
    ArrayList<Staffmembers> staffmembersArrayList = new ArrayList<>();

    public MyRecyclerViewAdapter(StaffInformation staffInformation, ArrayList<Staffmembers> staffmembersArrayList) {
        this.staffInformation = staffInformation;
        this.staffmembersArrayList = staffmembersArrayList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(staffInformation.getBaseContext());
        View view = layoutInflater.inflate(R.layout.listview_layout,parent,false);

        return new MyRecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, final int position) {

        holder.name_text.setText(staffmembersArrayList.get(position).getName());
        holder.post_text.setText(staffmembersArrayList.get(position).getPost());
        holder.phone_text.setText(staffmembersArrayList.get(position).getPhone());
        holder.aadhar_text.setText(staffmembersArrayList.get(position).getAadhar());
        holder.delete_staff_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteSelectedstaffRow(position);

            }
        });

    }

    private void deleteSelectedstaffRow(final int position) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(staffInformation.getBaseContext());
//        builder.setMessage("Do you really want to remove this member ?")
//                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                firestore.collection("Lawyers").document(UID).collection("Staff Members")
//                        .document(staffmembersArrayList.get(position).getUserID())
//                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Toast.makeText(staffInformation.getBaseContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
//                        staffInformation.showStaffData();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(staffInformation.getBaseContext(),"Can't be deleted", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        builder.show();

        firestore.collection("Lawyers").document(UID).collection("Staff Members")
                .document(staffmembersArrayList.get(position).getUserID())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(staffInformation.getBaseContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                staffInformation.showStaffData();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(staffInformation.getBaseContext(),"Can't be deleted", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return staffmembersArrayList.size();

    }

}
