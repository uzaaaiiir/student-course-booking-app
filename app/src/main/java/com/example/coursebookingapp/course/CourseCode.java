package com.example.coursebookingapp.course;

public class CourseCode {
    private String faculty;
    private int code;

    public CourseCode() {
    }

    public CourseCode(String faculty, int code) {
        this.faculty = faculty;
        this.code = code;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return faculty + code;
    }
}
