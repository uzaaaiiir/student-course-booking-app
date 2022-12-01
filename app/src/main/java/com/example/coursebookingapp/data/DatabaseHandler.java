package com.example.coursebookingapp.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.course.CourseCode;
import com.example.coursebookingapp.user.Instructor;
import com.example.coursebookingapp.user.Student;
import com.example.coursebookingapp.user.User;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // USER TABLE
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ROLE = "ROLE";
    public static final String COLUMN_USER_OBJECT = "USER_OBJECT";

    // COURSE TABLE
    public static final String COURSE_TABLE = "COURSE_TABLE";
    public static final String COLUMN_COURSE_CODE = "COURSE_CODE";
    public static final String COLUMN_COURSE_NAME = "COURSE_NAME";
    public static final String COLUMN_FACULTY_CODE = "FACULTY_CODE";

    // New Course Columns
    public static final String COLUMN_COURSE_OBJECT = "COURSE_OBJECT";
    public static final String COLUMN_DAY_OFFERED_ONE = "DAY_OFFERED_1";
    public static final String COLUMN_DAY_OFFERED_TWO = "DAY_OFFERED_2";


    public DatabaseHandler(@Nullable Context context) {

        super(context, "course.db", null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_ROLE + " TEXT, " + COLUMN_USER_OBJECT + " BLOB)";
        String createCourseTable = "CREATE TABLE " + COURSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FACULTY_CODE + " TEXT, " + COLUMN_COURSE_CODE + " INTEGER, " + COLUMN_COURSE_NAME + " TEXT, " + COLUMN_COURSE_OBJECT + " BLOB, " + COLUMN_DAY_OFFERED_ONE + " TEXT, " + COLUMN_DAY_OFFERED_TWO  + " TEXT)";

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
        courseValues.put(COLUMN_COURSE_OBJECT, makeByte(new Course()));
        courseValues.put(COLUMN_DAY_OFFERED_ONE, "SATURDAY");
        courseValues.put(COLUMN_DAY_OFFERED_TWO, "SUNDAY");
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

    public byte[] makeByte(Course course) {
        return SerializationUtils.serialize(course);
    }
    public byte[] makeUserByte(User user) { return SerializationUtils.serialize(user); }

    public Course read(byte[] data) {
        return SerializationUtils.deserialize(data);
    }
    public User readUser(byte[] data) { return SerializationUtils.deserialize(data); }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_ROLE, user.getRole());
        cv.put(COLUMN_USER_OBJECT, makeUserByte(user));

        long insert = db.insert(USER_TABLE, null, cv);

        return insert != -1;
    }

    public boolean userExists(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE (" + COLUMN_USERNAME + " = '" + user.getUsername() + "') AND (" + COLUMN_PASSWORD + " = '" + user.getPassword() + "') AND (" + COLUMN_ROLE + " = '" + user.getRole() + "')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public List<User> getUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE (" + COLUMN_USERNAME + " = '" + user.getUsername() + "') AND (" + COLUMN_PASSWORD + " = '" + user.getPassword() + "') AND (" + COLUMN_ROLE + " = '" + user.getRole() + "')";

        Cursor cursor = db.rawQuery(query, null);
        List<User> users = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int userID = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                User userFound = readUser(cursor.getBlob(4));

                userFound.setId(userID);
                userFound.setUsername(username);
                userFound.setPassword(password);
                users.add(userFound);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return users;
    }

    public List<User> allStudents(String search) {
        List<User> students = new ArrayList<>();

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ROLE + " = 'Student' AND (" + COLUMN_USERNAME + " LIKE '%" + search + "%')";
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

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ROLE + " = 'Instructor' AND (" + COLUMN_USERNAME + " LIKE '%" + search + "%')";
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
        String query = "DELETE FROM " + USER_TABLE + " WHERE (" + COLUMN_USERNAME + " = '" + user.getUsername() + "') AND (" + COLUMN_PASSWORD + " = '" + user.getPassword() + "') AND (" + COLUMN_ROLE + " = '" + user.getRole() + "')";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean updateUser(User updateUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, updateUser.getUsername());
        cv.put(COLUMN_PASSWORD, updateUser.getPassword());
        cv.put((COLUMN_USER_OBJECT), makeUserByte(updateUser));

        return db.update(USER_TABLE, cv, COLUMN_USERNAME + "=?", new String[]{String.valueOf(updateUser.getUsername())}) == 1;
    }




    // COURSES

    public boolean addCourse(Course course) {
        if (courseExists(course)) { return false; }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FACULTY_CODE, course.getCourseCode().getFaculty());
        cv.put(COLUMN_COURSE_CODE, course.getCourseCode().getCode());
        cv.put(COLUMN_COURSE_NAME, course.getCourseName());
        cv.put(COLUMN_COURSE_OBJECT, makeByte(course));

        long insert = db.insert(COURSE_TABLE, null, cv);

        return insert != -1;
    }


    public boolean courseExists(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = '" + course.getCourseCode().getFaculty() + "') AND (" + COLUMN_COURSE_CODE + " = '" + course.getCourseCode().getCode() + "')";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }

    public List<Course> findCourses(Course course, int searchOption) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Course> coursesFound = new ArrayList<>();
        String query = "";

        switch(searchOption) {
            case 1:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_DAY_OFFERED_ONE + " = '" + course.getDayOfWeek1().toString() + "') OR (" + COLUMN_DAY_OFFERED_TWO + " = '" + course.getDayOfWeek1().toString() + "')";
                break;
            case 2:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_COURSE_NAME + " LIKE '%" + course.getCourseName() + "%')";
                break;
            case 3:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE ((" + COLUMN_DAY_OFFERED_ONE + " = '" + course.getDayOfWeek1().toString() + "') OR (" + COLUMN_DAY_OFFERED_TWO + " = " + course.getDayOfWeek1().toString() + ")) AND (" + COLUMN_COURSE_NAME + " LIKE '%" + course.getCourseName() + "%')";
                break;
            case 4:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE ((" + COLUMN_COURSE_CODE + " LIKE '%" + course.getCourseCode().getCode() + "%') AND (" + COLUMN_FACULTY_CODE + " LIKE '%" + course.getCourseCode().getFaculty() + "%'))";
                break;
            case 5:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE ((" + COLUMN_DAY_OFFERED_ONE + " = '" + course.getDayOfWeek1().toString() + "') OR (" + COLUMN_DAY_OFFERED_TWO + " = '" + course.getDayOfWeek1().toString() + "')) AND ((" + COLUMN_COURSE_CODE + " LIKE '%" + course.getCourseCode().getCode() + "%') AND (" + COLUMN_FACULTY_CODE + " LIKE '%" + course.getCourseCode().getFaculty() + "%'))";
                break;
            case 6:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE ((" + COLUMN_COURSE_CODE + " LIKE '%" + course.getCourseCode().getCode() + "%') AND (" + COLUMN_FACULTY_CODE + " LIKE '%" + course.getCourseCode().getFaculty() + "%')) AND " + COLUMN_COURSE_NAME + " LIKE '%" + course.getCourseName() + "%'";
                break;
            case 7:
                query = "SELECT * FROM " + COURSE_TABLE + " WHERE ((" + COLUMN_DAY_OFFERED_ONE + " = '" + course.getDayOfWeek1().toString() + "') OR (" + COLUMN_DAY_OFFERED_TWO + " = '" + course.getDayOfWeek1().toString() + "')) AND ((" + COLUMN_COURSE_CODE + " LIKE '%" + course.getCourseCode().getCode() + "%') AND (" + COLUMN_FACULTY_CODE + " LIKE '%" + course.getCourseCode().getFaculty() + "%')) AND " + COLUMN_COURSE_NAME + " LIKE '%" + course.getCourseName() + "%'";
                break;
        }

        Cursor cursor = db.rawQuery(query, null);


        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                CourseCode courseCode = new CourseCode();
                courseCode.setFaculty(cursor.getString(1));
                courseCode.setCode(cursor.getInt(2));
                String courseName = cursor.getString(3);
                Course courseFound = read(cursor.getBlob(4));
                courseFound.setCourseCode(courseCode);
                courseFound.setCourseName(courseName);
                courseFound.setId(id);
                String day1 = cursor.getString(5);
                String day2 = cursor.getString(6);
                if (day1 != null) {
                    courseFound.setDayOfWeek1(DayOfWeek.valueOf(day1));
                }
                if (day2 != null) {
                    courseFound.setDayOfWeek2(DayOfWeek.valueOf(day2));
                }

                coursesFound.add(courseFound);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return coursesFound;

    }

    public List<Course> allCourses() {
        List<Course> courses = new ArrayList<>();

        String query = "SELECT * FROM " + COURSE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Course course = read(cursor.getBlob(4));
                int id = cursor.getInt(0);
                course.setId(id);
                CourseCode courseCode = new CourseCode();
                courseCode.setFaculty(cursor.getString(1));
                courseCode.setCode(cursor.getInt(2));
                String courseName = cursor.getString(3);
                course.setCourseCode(courseCode);
                course.setCourseName(courseName);
                if (cursor.getString(5) != null) {
                    course.setDayOfWeek1(DayOfWeek.valueOf(cursor.getString(5)));
                }
                if (cursor.getString(6) != null) {
                    course.setDayOfWeek2(DayOfWeek.valueOf(cursor.getString(6)));
                }
                courses.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courses;
    }

    public Course findCourse(Course course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE + " WHERE (" + COLUMN_FACULTY_CODE + " = '" + course.getCourseCode().getFaculty() + "') AND (" + COLUMN_COURSE_CODE + " = " + course.getCourseCode().getCode() + ")";

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

        cursor.close();
        return courseToReturn;

    }

    public boolean editCourse(int id, Course newValues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FACULTY_CODE, newValues.getCourseCode().getFaculty());
        cv.put(COLUMN_COURSE_CODE, newValues.getCourseCode().getCode());
        cv.put(COLUMN_COURSE_NAME, newValues.getCourseName());
        cv.put(COLUMN_COURSE_OBJECT, makeByte(newValues));
        cv.put(COLUMN_DAY_OFFERED_ONE, newValues.getDayOfWeek1() != null ? newValues.getDayOfWeek1().toString() : null);
        cv.put(COLUMN_DAY_OFFERED_TWO, newValues.getDayOfWeek2() != null ? newValues.getDayOfWeek2().toString() : null);

        return db.update(COURSE_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public boolean deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COURSE_TABLE, COLUMN_ID + "=" + course.getId(), null) > 0;
    }
}
