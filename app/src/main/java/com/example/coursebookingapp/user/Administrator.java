package com.example.coursebookingapp.user;

import java.io.Serializable;

public class Administrator extends User implements Serializable {
    private static final long serialVersionUID = 12358903454875L;

    public static final String ADMINISTRATOR = "Administrator";
    public Administrator(int id, String username, String password) {
        super(id, username, password, ADMINISTRATOR);
    }
}
