package com.t.module_t.database.entity;

public class CourseElement {

    private String name;
    public CourseElement(String name){

        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}