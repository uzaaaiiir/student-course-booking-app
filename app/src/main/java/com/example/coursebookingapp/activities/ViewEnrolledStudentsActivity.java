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

public class ViewEnrolledStudentsActivity extends AppCompatActivity {
    ListView list;
    ArrayAdapter enrolledStudentsArrayAdapter;
    Button back, home, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enrolled_students);

        assignInputs();
        updateListView(Course.getSelectedCourse().getEnrolledStudents());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(InstructorMainActivity.class);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(InstructorManageCourseActivity.class);
            }
        });
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(ViewEnrolledStudentsActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(ViewEnrolledStudentsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        back = findViewById(R.id.backBtnViewEnrolledStudents);
        home = findViewById(R.id.homeBtnViewEnrolledStudents);
        logout = findViewById(R.id.logoutBtnViewEnrolledStudents);
    }

    private void updateListView(List<User> studentsEnrolled) {
        enrolledStudentsArrayAdapter = new ArrayAdapter<User>(ViewEnrolledStudentsActivity.this, android.R.layout.simple_list_item_1, studentsEnrolled);
        list.setAdapter(enrolledStudentsArrayAdapter);
    }
}