package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.user.User;

import java.util.List;

public class CoursesListActivity extends AppCompatActivity {
    ListView list;
    ArrayAdapter enrolledCoursesArrayAdapter;
    Button back, home, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        assignInputs();
        User user = User.getCurrentUser().getUser(CoursesListActivity.this).get(0);
        List<Course> coursesEnrolled = user.getEnrolledCourses();
        updateListView(coursesEnrolled);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(StudentMainActivity.class);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(StudentMainActivity.class);
            }
        });
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(CoursesListActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(CoursesListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        back = findViewById(R.id.backBtnViewCourses);
        home = findViewById(R.id.homeBtnViewCourses);
        logout = findViewById(R.id.logoutBtnViewCourses);
        list = findViewById(R.id.listOfCoursesEnrolled);
    }

    private void updateListView(List<Course> coursesEnrolled) {
        enrolledCoursesArrayAdapter = new ArrayAdapter<>(CoursesListActivity.this, android.R.layout.simple_list_item_1, coursesEnrolled);
        list.setAdapter(enrolledCoursesArrayAdapter);
    }
}