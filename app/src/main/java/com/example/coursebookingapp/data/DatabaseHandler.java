package com.example.coursebookingapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.coursebookingapp.Course;
import com.example.coursebookingapp.CourseCode;
import com.example.coursebookingapp.user.Administrator;
import com.example.coursebookingapp.user.Instructor;
import com.example.coursebookingapp.user.Student;
import com.example.coursebookingapp.user.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ROLE = "ROLE";

    public static final String COURSE_TABLE = "COURSE_TABLE";
    public static final String COLUMN_COURSE_CODE = "COURSE_CODE";
    public static final String COLUMN_COURSE_NAME = "COURSE_NAME";
    public static final String COLUMN_COURSE_INSTRUCTOR_ID = "COURSE_INSTRUCTOR_ID";
    public static final String COLUMN_FACULTY_CODE = "FACULTY_CODE";


    public DatabaseHandler(@Nullable Context context) {

        super(context, "course.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_ROLE + " TEXT)";
        String createCourseTable = "CREATE TABLE " + COURSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FACULTY_CODE + " TEXT, " + COLUMN_COURSE_CODE + " INTEGER, " + COLUMN_COURSE_NAME + " TEXT)";

        db.execSQL(createUserTable);
        db.execSQL(createCourseTable);

        setDefaultLabel(db);

    }

    public void setDefaultLabel(SQLiteDatabase db) {
        // create default label
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, 0);
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_ROLE, "Administrator");
        db.insert(USER_TABLE, null, values);

        ContentValues courseValues = new ContentValues();
        courseValues.put(COLUMN_ID, 0);
        courseValues.put(COLUMN_FACULTY_CODE, "Faculty");
        courseValues.put(COLUMN_COURSE_CODE, 0);
        courseValues.put(COLUMN_COURSE_NAME, "Name");
        db.insert(COURSE_TABLE, null, courseValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);

        // Create tables again
        onCreate(db);
    }

    public boolean addRoot() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Administrator admin = new Administrator(-1, "admin", "admin123");

        cv.put(COLUMN_USERNAME, admin.getUsername());
        cv.put(COLUMN_PASSWORD, admin.getPassword());
        cv.put(COLUMN_ROLE, admin.getRole().toString());

        long insert = db.insert(USER_TABLE, null, cv);
        if (insert != -1) { return true; }

        return false;
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_ROLE, user.getRole().toString());

        long insert = db.insert(USER_TABLE, null, cv);

        if (insert != -1) { return true; }

        return false;
    }

    public boolean userExists(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE (" + COLUMN_USERNAME + " = \'" + user.getUsername().toString() + "\') AND (" + COLUMN_PASSWORD + " = \'" + user.getPassword().toString() + "\') AND (" + COLUMN_ROLE + " = \'" + user.getRole().toString() + "\')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }

        return false;
    }

    public List<User> allStudents(String search) {
        List<User> students = new ArrayList<>();

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ROLE + " = \'Student\' AND (" + COLUMN_USERNAME + " LIKE \'%" + search + "%\')";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                int userID = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);

                Student student = new Student(userID, username, password);
                students.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return students;
    }

    public List<User> allInstructors(String search) {
        List<User> instructors = new ArrayList<>();

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ROLE + " = \'Instructor\' AND (" + COLUMN_USERNAME + " LIKE \'%" + search + "%\')";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                int userID = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);

                Instructor instructor = new Instructor(userID, username, password);
                instructors.add(instructor);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instructors;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + USER_TABLE + " WHERE (" + COLUMN_USERNAME + " = \'" + user.getUsername().toString() + "\') AND (" + COLUMN_PASSWORD + " = \'" + user.getPassword().toString() + "\') AND (" + COLUMN_ROLE + " = \'" + user.getRole().toString() + "\')";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addCourse(Course course) {
        if (courseExists(course)) { return false; }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FACULTY_CODE, course.getCourseCode().getFaculty().toString());
        cv.put(COLUMN_COURSE_CODE, course.getCourseCode().getCode());
        cv.put(COLUMN_COURSE_NAME, course.getCourseName().toString());

        long insert = db.insert(COURSE_TABLE, null, cv);

        if (insert != -1) { return true; }

        return false;
    }


    public boolean courseExists(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = \'" + course.getCourseCode().getFaculty().toString() + "\') AND (" + COLUMN_COURSE_CODE + " = \'" + course.getCourseCode().getCode() + "\')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }

        return false;
    }

    private int findCourseID(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = \'" + course.getCourseCode().getFaculty().toString() + "\') AND (" + COLUMN_COURSE_CODE + " = \'" + Integer.toString(course.getCourseCode().getCode()) + "\')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public Course findCourse(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = \'" + course.getCourseCode().getFaculty().toString() + "\') AND (" + COLUMN_COURSE_CODE + " = " + Integer.toString(course.getCourseCode().getCode()) + ")";

        Cursor cursor = db.rawQuery(query, null);
        Course courseToReturn;
        CourseCode courseCode = new CourseCode();
        String courseName = "";
        int id = 0;

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                courseCode.setFaculty(cursor.getString(1));
                courseCode.setCode(cursor.getInt(2));
                courseName = cursor.getString(3);
            } while (cursor.moveToNext());
        }
        courseToReturn = new Course(courseCode, courseName);
        courseToReturn.setId(id);


        return courseToReturn;

    }

    public boolean editCourse(int id, Course newValues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FACULTY_CODE, newValues.getCourseCode().getFaculty().toString());
        cv.put(COLUMN_COURSE_CODE, newValues.getCourseCode().getCode());
        cv.put(COLUMN_COURSE_NAME, newValues.getCourseName());

        if (db.update(COURSE_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = \'" + course.getCourseCode().getFaculty().toString() + "\') AND (" + COLUMN_COURSE_CODE + " = \'" + Integer.toString(course.getCourseCode().getCode()) + "\')";
//        Cursor cursor = db.rawQuery(query, null);
//        int courseId = findCourseID(course);
//
//        if (cursor.moveToFirst()) {
//            return true;
//        } else {
//            return false;
//        }

        return db.delete(COURSE_TABLE, COLUMN_ID + "=" + course.getId(), null) > 0;
    }
}
