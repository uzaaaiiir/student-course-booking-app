package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.coursebookingapp.R;

public class InstructorManageCourseActivity extends AppCompatActivity {
    Button save, logout, home, back;
    EditText time1, time2, duration, capacity, description;
    Spinner day1, day2;
    Switch assign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_manage_course);

        assignInputs();
    }

    private void assignInputs() {
        save = findViewById(R.id.saveBtnInstructorManage);
        logout = findViewById(R.id.logoutInstructorManage);
        home = findViewById(R.id.homeBtnInstructorManage);
        back = findViewById(R.id.backBtnInstructorManage);
        time1 = findViewById(R.id.day1StartTime);
        time2 = findViewById(R.id.day2StartTIme);
        day1 = findViewById(R.id.day1Dropdown);
        day2 = findViewById(R.id.day2Dropdown);
        duration = findViewById(R.id.lectureDuration);
        capacity = findViewById(R.id.courseCapacity);
        description = findViewById(R.id.courseDescription);

    }
}