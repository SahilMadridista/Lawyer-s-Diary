package com.example.mylawyer;

public class Staffmembers {

    String name,post,phone,aadhar;
    String userID;

    public Staffmembers(){

    }

    public Staffmembers(String userID,String name, String post, String phone, String aadhar) {
        this.userID = userID;
        this.name = name;
        this.post = post;
        this.phone = phone;
        this.aadhar = aadhar;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
