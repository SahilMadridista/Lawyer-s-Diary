package com.example.mylawyer;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.interfaces.CasesModifier;
import com.example.mylawyer.model.Case;
import com.example.mylawyer.utils.Utils;
import com.google.firebase.Timestamp;

public class LawyerCaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView client_name_text,client_case_text,client_phone_text,case_start_date,client_aadhar;
    private ImageButton remove_client,add_details,see_details;
    private CasesModifier casesModifier;

    public LawyerCaseRecyclerViewHolder(@NonNull View itemView, CasesModifier casesModifier) {

        super(itemView);

        this.casesModifier = casesModifier;

        client_name_text = itemView.findViewById(R.id.client_name_text);
        client_case_text = itemView.findViewById(R.id.client_case_about_text);
        client_phone_text = itemView.findViewById(R.id.client_phone_text);
        case_start_date = itemView.findViewById(R.id.case_start_date);
        client_aadhar = itemView.findViewById(R.id.client_aadhar_number_text);
        remove_client = itemView.findViewById(R.id.remove_client_button);
        add_details = itemView.findViewById(R.id.adddetailsbuttononcard);
        see_details = itemView.findViewById(R.id.see_details);
    }

    public void bind(final Case clientCase) {
        client_name_text.setText(clientCase.clientName);
        client_case_text.setText(clientCase.caseDescription);
        client_phone_text.setText(clientCase.clientId);
        case_start_date.setText(getDateStringFromTimestamp(clientCase.startTime));
        client_aadhar.setText(clientCase.clientAadhar);

        add_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casesModifier.addDetails(clientCase.clientName, clientCase.startTime, clientCase.caseId);

            }
        });

        remove_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casesModifier.deleteSelectedCase(clientCase.caseId);
            }
        });
    }

    private String getDateStringFromTimestamp(Timestamp timestamp) {
        return Utils.convertMillisToDateString(timestamp.getSeconds()*1000);
    }
}
