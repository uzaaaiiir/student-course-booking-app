package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.user.User;

public class AdminMainActivity extends AppCompatActivity {
    TextView welcome;
    Button createCourse, searchCourse, manageInstructors, manageStudents, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        assignInputs();
        setWelcomeText();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(LoginActivity.class); }
        });

        manageInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(ManageInstructorsActivity.class); }
        });

        manageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(ManageStudentsActivity.class); }
        });

        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(CreateCourseActivity.class); }
        });

        searchCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(SearchCoursesActivity.class); }
        });
    }

    private void assignInputs() {
        welcome = findViewById(R.id.welcome3);
        createCourse = findViewById(R.id.createCourseAdminMain);
        searchCourse = findViewById(R.id.searchBtnAdminMain);
        logout = findViewById(R.id.logoutBtnAdminMain);
        manageInstructors = findViewById(R.id.manageInstructorsAdminMain);
        manageStudents = findViewById(R.id.manageStudentsAdminMain);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(AdminMainActivity.this, activityToStart);
        startActivity(activity);
    }
}