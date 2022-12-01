package com.example.coursebookingapp.course;

import android.content.Context;

import com.example.coursebookingapp.activities.SearchCoursesActivity;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.Instructor;
import com.example.coursebookingapp.user.Student;
import com.example.coursebookingapp.user.User;

import java.io.Serializable;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Course implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    private int id;
    private static Course selectedCourse;
    private CourseCode courseCode;
    private String courseName;
    private String instructorUsername;

    private List<User> studentsEnrolled;
    public boolean addEnrolledStudent(User user) { return studentsEnrolled.add(user); }
    public boolean removeEnrolledStudent(User user) { return studentsEnrolled.remove(user); }
    public List<User> getEnrolledStudents() { return studentsEnrolled; }


    // New Fields
    private DayOfWeek dayOfWeek1;
    private DayOfWeek dayOfWeek2;
    private Date startTime1;
    private Date startTime2;
    private int duration = 0;
    private int courseCapacity = 0;
    private String courseDescription;

    public Course() {}

    public Course(CourseCode courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.studentsEnrolled = new ArrayList<User>();
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

    public String getCourseInstructor() { return instructorUsername; }
    public void setCourseInstructor(String instructorUsername) { this.instructorUsername = instructorUsername; }

    public DayOfWeek getDayOfWeek1() { return dayOfWeek1; }
    public void setDayOfWeek1(DayOfWeek dayOfWeek1) { this.dayOfWeek1 = dayOfWeek1; }
    public DayOfWeek getDayOfWeek2() { return dayOfWeek2; }
    public void setDayOfWeek2(DayOfWeek dayOfWeek2) { this.dayOfWeek2 = dayOfWeek2; }
    public Date getStartTime1() { return startTime1; }
    public void setStartTime1(Date startTime1) { this.startTime1 = startTime1; }
    public Date getStartTime2() { return startTime2; }
    public void setStartTime2(Date startTime2) { this.startTime2 = startTime2; }
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

    public void emptyAllFields() {
        Course.getSelectedCourse().setCourseInstructor(null);
        Course.getSelectedCourse().setDayOfWeek1(null);
        Course.getSelectedCourse().setDayOfWeek2(null);
        Course.getSelectedCourse().setStartTime1(null);
        Course.getSelectedCourse().setStartTime2(null);
        Course.getSelectedCourse().setDuration(0);
        Course.getSelectedCourse().setCourseCapacity(0);
        Course.getSelectedCourse().setCourseDescription(null);
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
