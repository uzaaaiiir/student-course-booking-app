package com.example.coursebookingapp.user;

import android.content.Context;

import com.example.coursebookingapp.activities.LoginActivity;
import com.example.coursebookingapp.activities.ManageStudentsActivity;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.data.DatabaseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 12358903454875L;

    private static User currentUser;
    private int id;
    private String username;
    private String password;
    private String role;
    private List<Course> coursesTeaching;
    public List<Course> enrolledCourses;

    public User() {
        coursesTeaching = new ArrayList<>();
        enrolledCourses = new ArrayList<>();
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        coursesTeaching = new ArrayList<>();
        enrolledCourses = new ArrayList<>();
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdministrator() {
        return this.getRole().equals("Administrator");
    }

    public boolean isInstructor() {
        return this.getRole().equals("Instructor");
    }

    public boolean isStudent() {
        return this.getRole().equals("Student");
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return getRole() + ": " + getUsername();
    }

    public boolean enrolInCourse(Course course) {
        if (canEnrol(course)) {
            enrolledCourses.add(course);
            return true;
        }
        return false;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public boolean canEnrol(Course course) {
        if (course.getStartTime1() == null || course.getStartTime2() == null) {
            return true;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(course.getStartTime1());
        cal.add(Calendar.HOUR_OF_DAY, course.getDuration());
        Date enrollingCourseEndTime1 = cal.getTime();

        cal.setTime(course.getStartTime2());
        cal.add(Calendar.HOUR_OF_DAY, course.getDuration());
        Date enrollingCourseEndTime2 = cal.getTime();
        for (Course enrolledCourse : enrolledCourses) {
            if (enrolledCourse.getStartTime1() == null || enrolledCourse.getStartTime2() == null) {
                return true;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(enrolledCourse.getStartTime1());
            calendar.add(Calendar.HOUR_OF_DAY, enrolledCourse.getDuration());
            Date endTime1 = calendar.getTime();

            calendar.setTime(enrolledCourse.getStartTime2());
            calendar.add(Calendar.HOUR_OF_DAY, enrolledCourse.getDuration());
            Date endTime2 = calendar.getTime();

            if (enrolledCourse.getDayOfWeek1() == null || enrolledCourse.getDayOfWeek2() == null) {
                continue;
            }

            if (enrolledCourse.getDayOfWeek1().equals(course.getDayOfWeek1())) {
                if ((course.getStartTime1().after(enrolledCourse.getStartTime1()) && course.getStartTime1().before(endTime1))
                || (enrollingCourseEndTime1.after(enrolledCourse.getStartTime1()) && enrollingCourseEndTime1.before(endTime1))) {
                    return false;
                }
            } else if (enrolledCourse.getDayOfWeek1().equals(course.getDayOfWeek2())) {
                if ((course.getStartTime2().after(enrolledCourse.getStartTime1()) && course.getStartTime2().before(endTime1))
                        || (enrollingCourseEndTime2.after(enrolledCourse.getStartTime1()) && enrollingCourseEndTime2.before(endTime1))) {
                    return false;
                }
            } else if (enrolledCourse.getDayOfWeek2().equals(course.getDayOfWeek1())) {
                if ((course.getStartTime1().after(enrolledCourse.getStartTime2()) && course.getStartTime1().before(endTime2))
                        || (enrollingCourseEndTime1.after(enrolledCourse.getStartTime2()) && enrollingCourseEndTime1.before(endTime2))) {
                    return false;
                }
            } else if (enrolledCourse.getDayOfWeek2().equals(course.getDayOfWeek2())) {
                if ((course.getStartTime2().after(enrolledCourse.getStartTime2()) && course.getStartTime2().before(endTime2))
                        || (enrollingCourseEndTime2.after(enrolledCourse.getStartTime2()) && enrollingCourseEndTime2.before(endTime2))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean unenrolFromCourse(Course course) {
        return enrolledCourses.remove(course);
    }

    public boolean isEnrolled(Course course) {
        for (Course enrolledCourse : enrolledCourses) {
            if (enrolledCourse.getCourseCode().equals(course.getCourseCode())) {
                return true;
            }
        }
        return false;
    }

    public boolean addCourseTaught(Course course) {
        return coursesTeaching.add(course);
    }

    public boolean checkIfUserExists(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        if (databaseHandler.userExists(this)) {
            databaseHandler.close();
            return true;
        }

        databaseHandler.close();
        return false;
    }

    public boolean updateUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        return databaseHandler.updateUser(this);
    }

    public boolean addUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        boolean addedSuccessfully = databaseHandler.addUser(this);
        databaseHandler.close();
        return addedSuccessfully;
    }

    public static List<User> getAllInstructors(Context context, String search) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<User> instructorsSearched = databaseHandler.allInstructors(search);
        databaseHandler.close();
        return instructorsSearched;
    }

    public static List<User> getAllStudents(Context context, String search) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<User> instructorsSearched = databaseHandler.allStudents(search);
        databaseHandler.close();
        return instructorsSearched;
    }

    public boolean deleteUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        boolean deleted = databaseHandler.deleteUser(this);
        databaseHandler.close();
        return deleted;
    }

    public boolean userExists(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        boolean exists = databaseHandler.userExists(this);
        databaseHandler.close();
        return exists;
    }

    public List<User> getUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<User> users = databaseHandler.getUser(this);
        databaseHandler.close();
        return users;
    }
}
