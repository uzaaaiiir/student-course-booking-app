package com.example.coursebookingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

    public DatabaseHandler(@Nullable Context context) {
        super(context, "Course-Booking.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_ROLE + " TEXT)";
        String createCourseTable = "CREATE TABLE " + COURSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COURSE_CODE + " TEXT, " + COLUMN_COURSE_NAME + " TEXT, " + COLUMN_COURSE_INSTRUCTOR_ID + " INTEGER, FOREIGN KEY (" + COLUMN_COURSE_INSTRUCTOR_ID + ") REFERENCES USER_TABLE(" + COLUMN_COURSE_INSTRUCTOR_ID + "))";

        db.execSQL(createUserTable);
        db.execSQL(createCourseTable);
        addRoot();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
}
