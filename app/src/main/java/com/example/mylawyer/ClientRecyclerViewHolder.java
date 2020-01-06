package com.example.mylawyer;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.model.Case;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView client_name_text,client_case_text,client_phone_text,case_start_date,client_aadhar;
    public ImageButton remove_client,add_details,see_details;
    String Client_name;

    public ClientRecyclerViewHolder(@NonNull View itemView) {

        super(itemView);

        client_name_text = itemView.findViewById(R.id.client_name_text);
        client_case_text = itemView.findViewById(R.id.client_case_about_text);
        client_phone_text = itemView.findViewById(R.id.client_phone_text);
        case_start_date = itemView.findViewById(R.id.case_start_date);
        client_aadhar = itemView.findViewById(R.id.client_aadhar_number_text);
        remove_client = itemView.findViewById(R.id.remove_client_button);
        add_details = itemView.findViewById(R.id.adddetailsbuttononcard);
        see_details = itemView.findViewById(R.id.see_details);

        Client_name = client_name_text.getText().toString().trim();
    }

    public void bind(final Case clientCase) {
        client_name_text.setText(clientCase.clientName);
        client_case_text.setText(clientCase.caseDescription);
        client_phone_text.setText(clientCase.clientId);
        case_start_date.setText(clientCase.startTime.toString());
        client_aadhar.setText(clientCase.clientAadhar);

        add_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent adddetailsactivityintent = new Intent(view.getContext(),CourtScenes.class);

                adddetailsactivityintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                adddetailsactivityintent.putExtra("Client Name",clientCase.clientName);
                adddetailsactivityintent.putExtra("Start Date", clientCase.startTime);

                //lawyerProfile.getBaseContext().startActivity(adddetailsactivityintent);

            }
        });
    }

}
