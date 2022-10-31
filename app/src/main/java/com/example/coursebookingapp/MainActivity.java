package com.example.coursebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler dbHandler = new DatabaseHandler(MainActivity.this);
    Button logout, manageCourses, manageInstructors, manageStudents;
    User user;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        manageCourses = findViewById(R.id.manageCourses);
        manageInstructors = findViewById(R.id.manageInstructors);
        manageStudents = findViewById(R.id.manageStudents);
        welcome = findViewById(R.id.Welcome);

        generateUser();
        welcome.setText("Welcome, " + user.getUsername() + "\n" +
                " (" +user.getRole()+ ")");
        enableAdminPermissions();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });

        manageInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startManageInstructorsActivity();
            }
        });

        manageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startManageStudentsActivity();
            }
        });

        manageCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startManageCoursesActivity();
            }
        });

    }

    private void startLoginActivity() {
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    private void startManageCoursesActivity() {
        Intent manageCoursesActivity = new Intent(MainActivity.this, ManageCoursesActivity.class);
        manageCoursesActivity.putExtra("Username", user.getUsername());
        manageCoursesActivity.putExtra("Password", user.getPassword());
        manageCoursesActivity.putExtra("Role", user.getRole());
        startActivity(manageCoursesActivity);
    }
    private void startManageInstructorsActivity() {
        Intent manageInstructorsActivity = new Intent(MainActivity.this, ManageInstructorsActivity.class);
        manageInstructorsActivity.putExtra("Username", user.getUsername());
        manageInstructorsActivity.putExtra("Password", user.getPassword());
        manageInstructorsActivity.putExtra("Role", user.getRole());
        startActivity(manageInstructorsActivity);
    }

    private void startManageStudentsActivity() {
        Intent manageStudentsActivity = new Intent(MainActivity.this, ManageStudentsActivity.class);
        manageStudentsActivity.putExtra("Username", user.getUsername());
        manageStudentsActivity.putExtra("Password", user.getPassword());
        manageStudentsActivity.putExtra("Role", user.getRole());
        startActivity(manageStudentsActivity);
    }

    private void generateUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            user = new User(-1, bundle.getString("Username"), bundle.getString("Password"), bundle.getString("Role"));

        }
    }

    private void enableAdminPermissions() {
        if (user.getRole().equals("Instructor") || user.getRole().equals("Student")) {
            manageStudents.setEnabled(false);
            manageInstructors.setEnabled(false);
        }
    }
}