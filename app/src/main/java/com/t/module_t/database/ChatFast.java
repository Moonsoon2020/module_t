package com.t.module_t.database;

public class ChatFast {
    protected String email_teacher;
    protected String email_student;

    // Обязательный конструктор без аргументов для Firebase
    public ChatFast() {
    }

    public ChatFast(String em1, String em2) {
        this.email_teacher = em1;
        this.email_student = em2;
    }


    // Геттеры и сеттеры
    public String getEmail_teacher() {
        return email_teacher;
    }

    public void setEmail_teacher(String email_teacher) {
        this.email_teacher = email_teacher;
    }

    public String getEmail_student() {
        return email_student;
    }

    public void setEmail_student(String email_student) {
        this.email_student = email_student;
    }
}
