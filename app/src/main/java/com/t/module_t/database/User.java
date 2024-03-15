package com.t.module_t.database;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Objects;

public class User {

    public String username;
    public String email;
    private String password;
    public boolean status;


    public User(String username, String email, Boolean status, String password) {
        this.username = username;
        this.email = email;
        this.status = status;

    }

    public User(HashMap<String, Object> map){
        this.username = map.get("username").toString();
        this.email =  map.get("email").toString();
        this.status = Boolean.parseBoolean(map.get("status").toString());
    }

    public String getEmail() {
        return email.replace("~", ".");
    }

    @NonNull
    @Override
    public String toString() {
        return "Username: " + username+ "Email: " + getEmail();
    }
}