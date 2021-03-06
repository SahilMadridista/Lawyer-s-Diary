package com.example.mylawyer.clientpackage;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylawyer.R;
import com.example.mylawyer.interfaces.ClientCases;
import com.example.mylawyer.model.Case;
import com.example.mylawyer.utils.Utils;

public class ClientCaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final ClientCases clientcases;
    private TextView LawyerName, LawyerPhone,LawyerEmail,CaseDesc,startDate;
    //private ImageView call,message,email;
    private Button seehistory;

    public ClientCaseRecyclerViewHolder(@NonNull View itemView, ClientCases clientCases) {

        super(itemView);

        this.clientcases = clientCases;

        LawyerName = itemView.findViewById(R.id.LawyerName);
        LawyerPhone = itemView.findViewById(R.id.LawyerPhone);
        LawyerEmail = itemView.findViewById(R.id.LawyerEmail);
        CaseDesc = itemView.findViewById(R.id.LawyerCaseAbout);
        startDate = itemView.findViewById(R.id.CaseStartDate);

//        call = itemView.findViewById(R.id.callLawyerButton);
//        message = itemView.findViewById(R.id.messageLawyerButton);
//        email = itemView.findViewById(R.id.emailLawyerButton);

        seehistory = itemView.findViewById(R.id.seeHistorybutton);

    }

    public void bind(final Case clientCaseList){


        LawyerName.setText(clientCaseList.lawyerName);
        LawyerPhone.setText(clientCaseList.lawyerId);
        LawyerEmail.setText(clientCaseList.lawyerEmail);
        CaseDesc.setText(clientCaseList.caseDescription);
        startDate.setText(Utils.convertMillisToDateString(clientCaseList.startTime.getSeconds()*1000));

//        call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                 clientcases.callLawyer(clientCaseList.lawyerId);
//
//            }
//        });
//
//        message.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                clientcases.messageLawyer(clientCaseList.lawyerId);
//
//            }
//        });
//
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                clientcases.emailLawyer(clientCaseList.lawyerEmail);
//
//            }
//        });

        seehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clientcases.openCaseHistory(clientCaseList.caseId);

            }
        });

    }
}
