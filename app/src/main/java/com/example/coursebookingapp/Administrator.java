package com.example.coursebookingapp;

public class Administrator extends User {
    public Administrator(int id, String username, String password) {
        super(id, username, password, Role.Administrator);
    }
}
