package com.t.module_t.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class User {
    public String username;

    public String email;
    public boolean status;
    public String like_course;
    public String token;
    public ArrayList<String> piple = new ArrayList<>();
    public ArrayList<Notification> notifications = new ArrayList<>();
    public ArrayList<String> id_courses = new ArrayList<>();

    public User(String username, String email, boolean status, String token) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.token = token;
    }

    public User(HashMap<String, Object> map) {
        piple = new ArrayList<>();
        notifications = new ArrayList<>();
        if (map.containsKey("token"))
            token = Objects.requireNonNull(map.get("token")).toString();
        else
            token = null;
        id_courses = new ArrayList<>();
        this.username = map.get("username").toString();
        this.email = map.get("email").toString();
        this.status = Boolean.parseBoolean(map.get("status").toString());
        if (map.containsKey("like_course"))
            this.like_course = map.get("like_course").toString();
        HashMap<String, String> coursesMap = (HashMap<String, String>) map.get("courses");
        if (coursesMap != null) {
            id_courses.addAll(coursesMap.values());
        }
        // Обработка списка студентов
        HashMap<String, String> studentsMap = (HashMap<String, String>) map.get("students");
        if (studentsMap != null) {
            piple.addAll(studentsMap.values());
        }
        studentsMap = (HashMap<String, String>) map.get("teachers");
        if (studentsMap != null) {
            piple.addAll(studentsMap.values());
        }
        if (map.containsKey("notifications")) {
            HashMap<String, Object> data = (HashMap<String, Object>) map.get("notifications");
            // Проход по каждой записи в данных уведомлений
            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                HashMap<String, Object> notificationData = (HashMap<String, Object>) entry.getValue();
                HashMap<String, Object> notificationMap = (HashMap<String, Object>) notificationData.get("date");
                String text = (String) notificationData.get("text");
                String id_notifications = notificationData.get("id_notifications").toString();
                notifications.add(new Notification(text, id_notifications,notificationMap));
            }
        }
    }

    public ArrayList<String> getPeople() {
        return piple;
    }

    public String getEmail() {
        return email.replace("~", ".");
    }

    @NonNull
    @Override
    public String toString() {
        return "Username: " + username + "Email: " + getEmail() + "Students: " + piple + "Notifications: " + notifications;
    }
}
