package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.course.CourseCode;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

public class CreateCourseActivity extends AppCompatActivity {
    Button createCourse, home, logout;
    EditText faculty, code, courseName;
    TextView welcome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        assignInputs();
        setWelcomeText();

        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course;

                try {
                    if (faculty.getText().toString().trim().length() == 0 || code.getText().toString().trim().length() == 0 || courseName.getText().toString().trim().length() == 0){
                        printMessage("Fill out all the fields.");
                    } else {
                        CourseCode courseCode = new CourseCode(faculty.getText().toString().toUpperCase(), Integer.parseInt(code.getText().toString()));
                        course = new Course(courseCode, courseName.getText().toString());
                        if (course.addCourse(CreateCourseActivity.this)){
                            printMessage("Course successfully added: " + course);
                        } else {
                            printMessage("Course already exists: " + course);
                        }
                        emptyFields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    printMessage("Unable to create course. Fill faculty code and course code.");
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(AdminMainActivity.class); }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(LoginActivity.class); }
        });
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(CreateCourseActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(CreateCourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        welcome = findViewById(R.id.welcomeCreateCourse);
        createCourse = findViewById(R.id.createCourseBtn2);
        home = findViewById(R.id.homeBtnCreateCourse);
        logout = findViewById(R.id.logoutBtnCreateCourse);
        faculty = findViewById(R.id.facultyCodeCreateCourse);
        code = findViewById(R.id.courseCodeCreateCourse);
        courseName = findViewById(R.id.courseNameCreateCourse);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void emptyFields() {
        faculty.setText("");
        code.setText("");
        courseName.setText("");
    }
}