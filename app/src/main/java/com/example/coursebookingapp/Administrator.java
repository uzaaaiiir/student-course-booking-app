package com.example.coursebookingapp;

public class Administrator extends User {
    public static final String ADMINISTRATOR = "Administrator";
    public Administrator(int id, String username, String password) {
        super(id, username, password, ADMINISTRATOR);
    }
}
