package com.example.mylawyer.model;

import java.util.ArrayList;

public class Lawyer {

    public String name;
    public String lawyerPhone;
    public String lawyerEmail;
    public String lawyerAadhar;
    public String lowerCaseName;
    public ArrayList<String> clientsCasesList = new ArrayList<>();         //jhefghjtyhtrgr

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getClientsCasesList() {
        return clientsCasesList;
    }

    public void setClientsCasesList(ArrayList<String> clientsCasesList) {
        this.clientsCasesList = clientsCasesList;
    }
}
