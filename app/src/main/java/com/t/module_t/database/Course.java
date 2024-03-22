package com.t.module_t.database;

import java.util.HashMap;

public class Course {
    private String courseName;
    // Другие свойства

    public Course() {
        // Пустой конструктор, необходимый для сериализации объекта Firebase
    }

    public Course(String courseName) {
        this.courseName = courseName;
        // Инициализация других свойств
    }
    public Course(HashMap<String, Object> data){
        this.courseName = data.get("courseName").toString();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}