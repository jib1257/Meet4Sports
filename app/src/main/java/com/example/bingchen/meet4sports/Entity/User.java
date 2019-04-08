package com.example.bingchen.meet4sports.Entity;

public class User {
    private String uid;
    private String displayName;
    private String email;

    public User(){
    }

    public User(String uid, String displayName, String email){
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUid(){
        return this.uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public void setDisplayName(String dislayName){
        this.displayName = dislayName;
    }
    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
