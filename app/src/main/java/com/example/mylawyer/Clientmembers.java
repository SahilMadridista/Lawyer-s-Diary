package com.example.mylawyer;

public class Clientmembers {

    String name,about,phone,date,aadhar;
    String userID;

    public Clientmembers(){

    }

    public Clientmembers(String userID,String name, String about, String phone, String date, String aadhar) {

        this.userID = userID;
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.date = date;
        this.aadhar = aadhar;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
