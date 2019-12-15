package com.example.mylawyer;

public class Members {

    String name;
    String post;
    String phone,member_ID;

    public Members(){


    }

    public Members(String name, String post, String phone,String member_ID) {
        this.name = name;
        this.post = post;
        this.phone = phone;
        this.member_ID = member_ID;
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

    public String getMember_ID() {
        return member_ID;
    }

    public void setMember_ID(String member_ID) {
        this.member_ID = member_ID;
    }
}
