package com.example.coursebookingapp;

public class Instructor extends User {
    public static final String INSTRUCTOR = "Instructor";
    public Instructor(int id, String username, String password) {
        super(id, username, password, INSTRUCTOR);
    }
}

