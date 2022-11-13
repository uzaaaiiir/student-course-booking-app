package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.course.CourseCode;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

public class ManageCourseActivity extends AppCompatActivity {
    Button saveChanges, deleteCourse;
    Button back, home, logout;
    EditText faculty, code, courseName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);

        assignInputs();
        setEditTexts();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course.setSelectedCourse(null);
                startNewActivity(SearchCoursesActivity.class);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getCurrentUser().isAdministrator()) {
                    startNewActivity(AdminMainActivity.class);
                } else if (User.getCurrentUser().isInstructor()) {
                    startNewActivity(InstructorMainActivity.class);
                } else if (User.getCurrentUser().isStudent()) {
                    startNewActivity(StudentMainActivity.class);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(LoginActivity.class); }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseCode courseCode = new CourseCode();
                Course course;
                if (anyFieldEmpty()) {
                    printMessage("Please fill out all the fields.");
                } else {
                    updateSelectedCourse();
                    if (Course.getSelectedCourse().updateCourse(ManageCourseActivity.this)) {
                        printMessage("Course updated successfully");
                        emptyFields();
                        Course.setSelectedCourse(null);
                        startNewActivity(SearchCoursesActivity.class);
                    } else {
                        printMessage("Unable to update course.");
                    }
                }

            }
        });

        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Course.getSelectedCourse().deleteCourse(ManageCourseActivity.this)) {
                    printMessage("Successfully deleted course.");
                    emptyFields();
                    startNewActivity(SearchCoursesActivity.class);
                } else {
                    printMessage("Unable to delete course.");
                }
            }
        });
    }

    private void emptyFields() {
        code.setText("");
        faculty.setText("");
        courseName.setText("");
    }

    private void updateSelectedCourse() {
        Course.getSelectedCourse().getCourseCode().setFaculty(faculty.getText().toString());
        Course.getSelectedCourse().getCourseCode().setCode(Integer.parseInt(code.getText().toString()));
        Course.getSelectedCourse().setCourseName(courseName.getText().toString());
    }

    private boolean anyFieldEmpty() {
        return faculty.getText().toString().isEmpty() || code.getText().toString().isEmpty() || courseName.getText().toString().isEmpty();
    }

    private void setEditTexts() {
        faculty.setText(Course.getSelectedCourse().getCourseCode().getFaculty());
        code.setText(String.valueOf(Course.getSelectedCourse().getCourseCode().getCode()));
        courseName.setText(Course.getSelectedCourse().getCourseName());
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(ManageCourseActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(ManageCourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        saveChanges = findViewById(R.id.saveChanges);
        deleteCourse = findViewById(R.id.delete);
        back = findViewById(R.id.backBtnManageCourse);
        home = findViewById(R.id.homeBtnManageCourse);
        logout = findViewById(R.id.logoutBtnManageCourse);
        faculty = findViewById(R.id.facultySearchResults);
        code = findViewById(R.id.codeSearchResult);
        courseName = findViewById(R.id.courseNameSearchResult);
    }

}