package com.t.module_t.database;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
    private String courseName;
    public String id_course;
    public ArrayList<String> items = new ArrayList<>();
    public ArrayList<String> student = new ArrayList<>();

    public Course(String courseName, String id_course) {
        this.courseName = courseName;
        this.id_course = id_course;

    }
    public Course(HashMap<String, Object> data){
        this.courseName = data.get("courseName").toString();
        this.id_course = data.get("id_course").toString();
        HashMap<String, String> itemMap = (HashMap<String, String>) data.get("items");
        if (itemMap != null) {
            items.addAll(itemMap.values());
        }

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}