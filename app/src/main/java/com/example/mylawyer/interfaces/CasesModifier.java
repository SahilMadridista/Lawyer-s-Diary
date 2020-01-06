package com.example.mylawyer.interfaces;


import com.google.firebase.Timestamp;

public interface CasesModifier {

    void addDetails(String clientName, Timestamp startTime);

    void deleteSelectedCase(String caseId);

}
