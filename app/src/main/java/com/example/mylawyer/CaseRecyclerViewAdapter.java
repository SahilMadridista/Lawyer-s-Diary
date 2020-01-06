package com.example.mylawyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylawyer.model.Case;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class CaseRecyclerViewAdapter extends RecyclerView.Adapter<ClientRecyclerViewHolder>{

    private Context context;
    private ArrayList<Case> casesArrayList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String UID = mAuth.getCurrentUser().getUid();

    public CaseRecyclerViewAdapter(Context context, ArrayList<Case> casesArrayList){
        this.context = context;
        this.casesArrayList = casesArrayList;
    }

    @NonNull
    @Override
    public ClientRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_client_layout,parent,false);
        return new ClientRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientRecyclerViewHolder holder, final int position) {

        holder.bind(casesArrayList.get(position));

//        holder.remove_client.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                deleteSelectedRow(position);
//
//            }
//        });
//
//        holder.add_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent adddetailsactivityintent = new Intent(lawyerProfile.getBaseContext(),CourtScenes.class);
//
//                adddetailsactivityintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                adddetailsactivityintent.putExtra("Client Name",casesArrayList.get(position).getName());
//                adddetailsactivityintent.putExtra("Start Date",casesArrayList.get(position).getDate());
//                adddetailsactivityintent.putExtra("Client ID",UID);
//
//                lawyerProfile.getBaseContext().startActivity(adddetailsactivityintent);
//
//            }
//        });
//
//        holder.see_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(lawyerProfile.getBaseContext(),"See deails button",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

//    private void deleteSelectedRow(int position) {
//
//        context.firestore.collection("Lawyers").document(UID).collection("Clients")
//                .document(casesArrayList.get(position).clientId)
//                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                Toast.makeText(lawyerProfile.getBaseContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
//
//                lawyerProfile.showData();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(lawyerProfile.getBaseContext(),"Can't be deleted",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return casesArrayList.size();
    }
}
