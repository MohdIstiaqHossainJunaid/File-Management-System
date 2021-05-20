package com.example.cse327projet;

public class Users {

    private String email;
    private String fullName;
    private String isUser;
    private String userID;

    private Users(){

    }

    private Users(String email, String fullName, String isUser, String userID){
        this.email = email;
        this.fullName = fullName;
        this.isUser = isUser;
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
