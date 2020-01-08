package com.example.mylawyer.interfaces;


import com.google.firebase.Timestamp;

public interface CasesModifier {

    void addDetails(String clientName, Timestamp startTime, String caseID);

    void deleteSelectedCase(String caseId);

}
