package com.t.module_t.database;

public class User {

    public String username;
    public String email;
    private String password;
    public boolean status;


    public User(String username, String email, Boolean status, String password) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.password = password;
    }

}