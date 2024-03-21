package com.t.module_t.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User{
    public String username;
    public String email;
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
            Pattern pattern = Pattern.compile("email=(.*?),\\s*notifications=(.*?),\\s*status=(.*?),\\s*username=(.*?)\\}");
            Matcher matcher = pattern.matcher(stud);
            while (matcher.find()) {
                String email = matcher.group(1);
                boolean status = Boolean.parseBoolean(matcher.group(3));
                String username = matcher.group(4);
                this.students.add(new User(username, email, status));
            }
        }
        HashMap<String, HashMap<String, Object>> notificationsMap = (HashMap<String, HashMap<String, Object>>) map.get("notifications");
        // Обработка списка уведомлений
        if (notificationsMap != null) {
            for (HashMap.Entry<String, HashMap<String, Object>> entry : notificationsMap.entrySet()) {
                HashMap<String, Object> notificationMap = entry.getValue();
                String text = (String) notificationMap.get("text");
                HashMap<String, Long> dateMap = (HashMap<String, Long>) notificationMap.get("date");

                // Создаем объект Notification и добавляем его в список
                Notification notification = new Notification(text, dateMap);
                notifications.add(notification);
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
