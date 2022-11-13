package com.example.coursebookingapp.course;

import android.content.Context;

import com.example.coursebookingapp.activities.SearchCoursesActivity;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.Instructor;
import com.example.coursebookingapp.user.Student;

import java.io.Serializable;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private int id;
    private static Course selectedCourse;
    private CourseCode courseCode;
    private String courseName;
    private Instructor courseInstructor;
    private List<Student> studentsEnrolled;

    // New Fields
    private DayOfWeek dayOfWeek1;
    private DayOfWeek dayOfWeek2;
    private Time startTime1;
    private Time startTime2;
    private int duration;
    private int courseCapacity;
    private String courseDescription;

    public Course() {}

    public Course(CourseCode courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public static void setSelectedCourse(Course course) { selectedCourse = course; }
    public static Course getSelectedCourse() { return selectedCourse; }

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

    public Instructor getCourseInstructor() { return courseInstructor; }
    public void setCourseInstructor(Instructor courseInstructor) { this.courseInstructor = courseInstructor; }

    public DayOfWeek getDayOfWeek1() { return dayOfWeek1; }
    public void setDayOfWeek1(DayOfWeek dayOfWeek1) { this.dayOfWeek1 = dayOfWeek1; }
    public DayOfWeek getDayOfWeek2() { return dayOfWeek2; }
    public void setDayOfWeek2(DayOfWeek dayOfWeek2) { this.dayOfWeek2 = dayOfWeek2; }
    public Time getStartTime1() { return startTime1; }
    public void setStartTime1(Time startTime1) { this.startTime1 = startTime1; }
    public Time getStartTime2() { return startTime2; }
    public void setStartTime2(Time startTime2) { this.startTime2 = startTime2; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getCourseCapacity() { return courseCapacity; }
    public void setCourseCapacity(int courseCapacity) { this.courseCapacity = courseCapacity; }
    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public String toString() {
        return courseCode + ": " + courseName;
    }

    public boolean addCourse(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        if (dbHandler.addCourse(this)) { return true; }
        dbHandler.close();
        return false;
    }


    public List<Course> searchForCourses(Context context, int searchOption) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        List<Course> coursesToReturn = dbHandler.findCourses(this, searchOption);
        dbHandler.close();
        return coursesToReturn;
    }

    public static List<Course> findAllCourses(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        List<Course> allCourses = dbHandler.allCourses();
        dbHandler.close();
        return allCourses;
    }

    public boolean updateCourse(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        boolean updateSuccess = dbHandler.editCourse(Course.getSelectedCourse().getId(), Course.getSelectedCourse());
        dbHandler.close();
        return updateSuccess;
    }

    public boolean deleteCourse(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        boolean deleteSuccess = dbHandler.deleteCourse(Course.getSelectedCourse());
        dbHandler.close();
        return deleteSuccess;
    }
}
