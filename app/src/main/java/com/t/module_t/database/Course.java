package com.t.module_t.database;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
    private String courseName;
    public String id_course;
    public ArrayList<String> items;
    // Другие свойства

    public Course() {
        // Пустой конструктор, необходимый для сериализации объекта Firebase
    }

    public Course(String courseName, String id_course) {
        this.courseName = courseName;
        this.id_course = id_course;
        // Инициализация других свойств
    }
    public Course(HashMap<String, Object> data){
        this.courseName = data.get("courseName").toString();
        this.id_course = data.get("id_course").toString();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}