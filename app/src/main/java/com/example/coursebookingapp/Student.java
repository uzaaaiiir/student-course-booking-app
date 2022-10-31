package com.example.coursebookingapp;

public class Student extends User {
    public Student(int id, String username, String password) {
        super(id, username, password, Role.Student);
    }
}
