package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler dbHandler = new DatabaseHandler(MainActivity.this);
    Button logout, manageCourses, manageInstructors, manageStudents;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignButtons();
        setWelcomeText();
        enableAdminPermissions();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });

        manageInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(ManageInstructorsActivity.class); }
        });

        manageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(ManageStudentsActivity.class);}
        });

        manageCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(ManageCoursesActivity.class);
            }
        });

    }

    private void assignButtons() {
        logout = findViewById(R.id.logout);
        manageCourses = findViewById(R.id.manageCourses);
        manageInstructors = findViewById(R.id.manageInstructors);
        manageStudents = findViewById(R.id.manageStudents);
        welcome = findViewById(R.id.Welcome);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(MainActivity.this, activityToStart);
        startActivity(activity);
    }

    private void enableAdminPermissions() {
        if (User.getCurrentUser().getRole().equals("Instructor") || User.getCurrentUser().getRole().equals("Student")) {
            manageStudents.setEnabled(false);
            manageInstructors.setEnabled(false);
        }
    }
}