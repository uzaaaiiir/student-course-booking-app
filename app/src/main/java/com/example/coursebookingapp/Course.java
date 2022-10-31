package com.example.coursebookingapp;

public class Course {

    private CourseCode courseCode;
    private String courseName;
    private int id;

    public Course(CourseCode courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public CourseCode getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(CourseCode courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return courseCode + ": " + courseName;
    }
}
