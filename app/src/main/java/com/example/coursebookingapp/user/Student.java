package com.example.coursebookingapp.user;

public class Student extends User {
    public static final String STUDENT = "Student";
    public Student(int id, String username, String password) {
        super(id, username, password, STUDENT);
    }
}