package com.t.module_t.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    public String username;
    public String email;
    private String password;
    public boolean status;
    public ArrayList<User> students = new ArrayList<>();
    public ArrayList<Notification> notifications = new ArrayList<>();

    public User(String username, String email, boolean status) {
        this.username = username;
        this.email = email;
        this.status = status;
    }

    public User(HashMap<String, Object> map){
        this.username = map.get("username").toString();
        this.email = map.get("email").toString();
        this.status = Boolean.parseBoolean(map.get("status").toString());

        // Обработка списка студентов
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

        // Обработка списка уведомлений
        if (map.get("notifications") != null) {
            ArrayList<HashMap<String, Object>> notificationList = (ArrayList<HashMap<String, Object>>) map.get("notifications");
            for (HashMap<String, Object> notificationMap : notificationList) {
                String text = (String) notificationMap.get("text");
                this.notifications.add(new Notification(text, (HashMap<String, Long>) notificationMap.get("date")));
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
        return "Username: " + username + "Email: " + getEmail() + "Students: " + students + "Notifications: " + notifications;
    }
}
