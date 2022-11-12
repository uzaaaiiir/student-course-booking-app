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

import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.course.CourseCode;
import com.example.coursebookingapp.R;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

public class ManageCoursesActivity extends AppCompatActivity {
    DatabaseHandler dbHandler = new DatabaseHandler(ManageCoursesActivity.this);
    User user;
    Course currentCourse;

    Button logoutBtn, createCourseBtn, searchCourseBtn, editCourseBtn, deleteCourseBtn, backBtn;
    TextView welcome;
    EditText facultyInput, codeInput, courseNameInput, courseCodeSearch, facultySearch, courseCodeEdit, courseFacultyEdit, courseNameEdit;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);

        logoutBtn = findViewById(R.id.logoutCourse);
        createCourseBtn = findViewById(R.id.createCourseBtn);
        searchCourseBtn = findViewById(R.id.searchCourseManager);
        editCourseBtn = findViewById(R.id.saveChangesBtn);
        deleteCourseBtn = findViewById(R.id.deleteCourseBtn);
        backBtn = findViewById(R.id.backBtn3);

        welcome = findViewById(R.id.textView6);

        facultyInput = findViewById(R.id.enterFaculty);
        codeInput = findViewById(R.id.enterCourseCode);
        courseNameInput = findViewById(R.id.enterCourseName);
        courseCodeSearch = findViewById(R.id.searchCourse);
        courseCodeEdit = findViewById(R.id.courseCodeResult);
        courseFacultyEdit = findViewById(R.id.courseFacultyResult);
        courseNameEdit = findViewById(R.id.courseNameResult);
        facultySearch = findViewById(R.id.enterFacultySearch);

        dbHandler = new DatabaseHandler(ManageCoursesActivity.this);

        generateUser();
        welcome.setText("Welcome, " + user.getUsername() + "\n" +
                " (" +user.getRole()+ ")");
        if (user.getRole().equals("Student") || user.getRole().equals("Instructor")) {
            editCourseBtn.setEnabled(false);
            deleteCourseBtn.setEnabled(false);
            createCourseBtn.setEnabled(false);
        }

        deleteCourseBtn.setEnabled(false);
        editCourseBtn.setEnabled(false);

        createCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(ManageCoursesActivity.this);
                Course course;

                try {
                    CourseCode courseCode = new CourseCode(facultyInput.getText().toString(), Integer.parseInt(codeInput.getText().toString()));
                    course = new Course(courseCode, courseNameInput.getText().toString());
                    if (dbHandler.addCourse(course)){
                        Toast.makeText(ManageCoursesActivity.this, "Course successfully added: " + course, Toast.LENGTH_SHORT).show();
                        facultyInput.setText("");
                        codeInput.setText("");
                        courseNameInput.setText("");
                    } else {
                        Toast.makeText(ManageCoursesActivity.this, "Course already exists: " + course, Toast.LENGTH_SHORT).show();
                        facultyInput.setText("");
                        codeInput.setText("");
                        courseNameInput.setText("");
                    }

                } catch (Exception e) {
                    Toast.makeText(ManageCoursesActivity.this, "Unable to create course.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(ManageCoursesActivity.this);
                Course course;

                if (facultySearch.getText().toString().isEmpty() || courseCodeSearch.getText().toString().isEmpty()) {
                    Toast.makeText(ManageCoursesActivity.this, "Must enter both Faculty Code and Course Code.", Toast.LENGTH_SHORT).show();
                } else {
                    CourseCode courseCode = new CourseCode(facultySearch.getText().toString(), Integer.parseInt(courseCodeSearch.getText().toString()));
                    course = new Course(courseCode, null);
                    if (dbHandler.courseExists(course)) {
                        Toast.makeText(ManageCoursesActivity.this, "Course Found.", Toast.LENGTH_SHORT).show();
                        Course courseFound = dbHandler.findCourse(course);
                        currentCourse = courseFound;
                        courseFacultyEdit.setText(courseFound.getCourseCode().getFaculty());
                        courseCodeEdit.setText(Integer.toString(courseFound.getCourseCode().getCode()));
                        courseNameEdit.setText(courseFound.getCourseName());
                        if (user.getRole().equals("Administrator")) {
                            editCourseBtn.setEnabled(true);
                            deleteCourseBtn.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(ManageCoursesActivity.this, "Course does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        editCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseCode courseCode = new CourseCode();
                Course course;
                if (courseFacultyEdit.getText().toString().isEmpty() || courseCodeEdit.getText().toString().isEmpty()) {
                    Toast.makeText(ManageCoursesActivity.this, "Please fill out the Code and Faculty Fields.", Toast.LENGTH_SHORT).show();
                } else {
                    courseCode.setFaculty(courseFacultyEdit.getText().toString());
                    courseCode.setCode(Integer.parseInt(courseCodeEdit.getText().toString()));
                    course = new Course(courseCode, courseNameEdit.getText().toString());
                    DatabaseHandler dbHandler = new DatabaseHandler(ManageCoursesActivity.this);
                    if (dbHandler.editCourse(currentCourse.getId(), course)) {
                        Toast.makeText(ManageCoursesActivity.this, "Course updated successfully.", Toast.LENGTH_SHORT).show();
                        courseFacultyEdit.setText("");
                        courseCodeEdit.setText("");
                        courseNameEdit.setText("");
                        courseCodeSearch.setText(Integer.toString(course.getCourseCode().getCode()));
                        facultySearch.setText(course.getCourseCode().getFaculty());
                        currentCourse = course;
                    }
                }

            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(ManageCoursesActivity.this);

                if (dbHandler.deleteCourse(currentCourse)) {
                    Toast.makeText(ManageCoursesActivity.this, "Successfully deleted course.", Toast.LENGTH_SHORT).show();
                    courseFacultyEdit.setText("");
                    courseCodeEdit.setText("");
                    courseNameEdit.setText("");
                    courseCodeSearch.setText("");
                    facultySearch.setText("");
                    editCourseBtn.setEnabled(false);
                    deleteCourseBtn.setEnabled(false);
                } else {
                    Toast.makeText(ManageCoursesActivity.this, "Unable to delete course.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

    }

    private void startLoginActivity() {
        Intent loginActivity = new Intent(ManageCoursesActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    private void startMainActivity() {
        Intent mainActivity = new Intent(ManageCoursesActivity.this, MainActivity.class);
        mainActivity.putExtra("Username", user.getUsername());
        mainActivity.putExtra("Password", user.getPassword());
        mainActivity.putExtra("Role", user.getRole());
        startActivity(mainActivity);
    }

    private void generateUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            user = new User(-1, bundle.getString("Username"), bundle.getString("Password"), bundle.getString("Role"));

        }
    }
}