package com.example.coursebookingapp.user;

import com.example.coursebookingapp.course.Course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Serializable {
    private static final long serialVersionUID = 12358903454875L;

    public static final String STUDENT = "Student";

    public Student(int id, String username, String password) {
        super(id, username, password, STUDENT);
    }


}
