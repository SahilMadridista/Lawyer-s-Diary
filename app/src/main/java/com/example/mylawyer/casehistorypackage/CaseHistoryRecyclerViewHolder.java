package com.example.mylawyer.casehistorypackage;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylawyer.R;
import com.example.mylawyer.model.CaseInformation;
import com.example.mylawyer.utils.Utils;

public class CaseHistoryRecyclerViewHolder extends RecyclerView.ViewHolder {

    public CaseHistoryRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(CaseInformation caseInformation) {

        TextView hearingDate = itemView.findViewById(R.id.Hearing_Date);
        TextView assignedLawyer = itemView.findViewById(R.id.AssignedLawyer);
        TextView whatHappenedText = itemView.findViewById(R.id.WhatHappenedText);
        TextView nextDateText = itemView.findViewById(R.id.NextDateText);

        hearingDate.setText(Utils.convertMillisToDateString(caseInformation.hearingDate));
        assignedLawyer.setText(caseInformation.assignedLawyer);
        whatHappenedText.setText(caseInformation.whatHappenedInCourt);
        nextDateText.setText(Utils.convertMillisToDateString(caseInformation.nextHearingDate));
    }


}
