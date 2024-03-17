package com.t.module_t.database;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    public String username;
    public String email;
    private String password;
    public boolean status;
    public ArrayList<User> students = new ArrayList<>();


    public User(String username, String email, Boolean status) {
        this.username = username;
        this.email = email;
        this.status = status;

    }

    public User(@NonNull HashMap<String, Object> map){
        this.username = map.get("username").toString();
        this.email =  map.get("email").toString();
        this.status = Boolean.parseBoolean(map.get("status").toString());
        if (map.get("students") != null) {
            String stud = map.get("students").toString();
            Pattern pattern = Pattern.compile("email=(.*?),\\s*status=(.*?),\\s*username=(.*?)\\}");
            Matcher matcher = pattern.matcher(stud);
            while (matcher.find()) {
                String email = matcher.group(1);
                boolean status = Boolean.parseBoolean(matcher.group(2));
                String username = matcher.group(3);
                this.students.add(new User(username, email, status));
            }
        }
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public String getEmail() {
        return email.replace("~", ".");
    }

    @NonNull
    @Override
    public String toString() {
        return "Username: " + username+ "Email: " + getEmail() + "Students: " +students;
    }
}