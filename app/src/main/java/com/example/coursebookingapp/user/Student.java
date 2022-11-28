package com.example.coursebookingapp.user;

import com.example.coursebookingapp.course.Course;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    public static final String STUDENT = "Student";

    public Student(int id, String username, String password) {
        super(id, username, password, STUDENT);
    }


}
