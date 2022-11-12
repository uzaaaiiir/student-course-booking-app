package com.example.coursebookingapp.user;

import com.example.coursebookingapp.course.Course;

import java.util.List;

public class Instructor extends User {
    public static final String INSTRUCTOR = "Instructor";
    private List<Course> coursesTeaching;


    public Instructor(int id, String username, String password) {
        super(id, username, password, INSTRUCTOR);
    }

    public boolean addCourse(Course course) {
        return coursesTeaching.add(course);
    }
}

