package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.user.User;

public class StudentMainActivity extends AppCompatActivity {
    TextView welcome;
    Button viewCourses, searchAndEnroll, logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        assignInputs();
        setWelcomeText();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(LoginActivity.class); }
        });

        searchAndEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(SearchCoursesActivity.class); }
        });

        viewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(CoursesListActivity.class);
            }
        });
    }

    private void assignInputs() {
        welcome = findViewById(R.id.welcomeStudentMain);
        viewCourses = findViewById(R.id.viewEnrolledCourses);
        searchAndEnroll = findViewById(R.id.searchCoursesStudentMain);
        logout = findViewById(R.id.logoutStudentMain);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(StudentMainActivity.this, activityToStart);
        startActivity(activity);
    }
}