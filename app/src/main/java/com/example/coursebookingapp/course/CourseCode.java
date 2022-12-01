package com.example.coursebookingapp.course;

import java.io.Serializable;
import java.util.Objects;

public class CourseCode implements Serializable {

    private static final long serialVersionUID = 6529685098267757691L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCode that = (CourseCode) o;
        return code == that.code && this.getFaculty().equals(that.getFaculty());
    }

}
