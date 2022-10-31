package com.example.coursebookingapp;

public class Instructor extends User {
    public Instructor(int id, String username, String password) {
        super(id, username, password, Role.Instructor);
    }
}

