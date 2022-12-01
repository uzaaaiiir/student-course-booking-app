package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.user.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentManageCourseActivity extends AppCompatActivity {
    Button enrol, unenrol, logout, home, back;
    EditText time1, time2, duration, capacity, description;
    Spinner day1, day2;
    TextView courseCode, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manage_course);

        assignInputs();
        setCourseCodeAndCourseNameUI();
        generateDayDropdown();
        setCourseDetails();
        managePermissions();
        toggleButtons();

        enrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getCurrentUser().canEnrol(Course.getSelectedCourse())) {
                    User user = User.getCurrentUser().getUser(StudentManageCourseActivity.this).get(0);
                    user.enrolInCourse(Course.getSelectedCourse());

                    Course course = Course.getSelectedCourse().searchForCourses(StudentManageCourseActivity.this, 6).get(0);
                    course.addEnrolledStudent(User.getCurrentUser());

                    user.updateUser(StudentManageCourseActivity.this);
                    course.updateCourse(StudentManageCourseActivity.this);
                    enrol.setEnabled(false);
                    unenrol.setEnabled(true);
                } else {
                    printMessage("Time Conflict in Course.");
                }
            }
        });

        unenrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getCurrentUser().getUser(StudentManageCourseActivity.this).get(0);
                user.unenrolFromCourse(Course.getSelectedCourse());

                Course course = Course.getSelectedCourse().searchForCourses(StudentManageCourseActivity.this, 6).get(0);
                course.removeEnrolledStudent(User.getCurrentUser());

                user.updateUser(StudentManageCourseActivity.this);
                course.updateCourse(StudentManageCourseActivity.this);
                enrol.setEnabled(true);
                unenrol.setEnabled(false);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course.setSelectedCourse(null);
                startNewActivity(LoginActivity.class);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course.setSelectedCourse(null);
                startNewActivity(StudentMainActivity.class);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course.setSelectedCourse(null);
                startNewActivity(SearchCoursesActivity.class);
            }
        });

    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(StudentManageCourseActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(StudentManageCourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void managePermissions() {
    }

    private void assignInputs() {
        enrol = findViewById(R.id.saveChangesInstructor);
        unenrol = findViewById(R.id.unenrolBtnStudentCourse);
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
        courseCode = findViewById(R.id.courseCodeInstructorManageCourse);
        courseName = findViewById(R.id.courseNameIntructorManage);
    }

    private void setCourseCodeAndCourseNameUI() {
        String faculty = Course.getSelectedCourse().getCourseCode().getFaculty();
        String code = String.valueOf(Course.getSelectedCourse().getCourseCode().getCode());
        String name = Course.getSelectedCourse().getCourseName();
        courseCode.setText(faculty + code);
        courseName.setText(name);
    }

    private void generateDayDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daysOfWeek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        day1.setAdapter(adapter);
        day2.setAdapter(adapter);
    }

    private void setCourseDetails() {
        DateFormat formatter = new SimpleDateFormat("hh:mm");

        if (Course.getSelectedCourse().getDayOfWeek1() != null) {
            day1.setSelection(Course.getSelectedCourse().getDayOfWeek1().getValue());
            Date date = Course.getSelectedCourse().getStartTime1();
            String time = formatter.format(date);
            time1.setText(time);
        }

        if (Course.getSelectedCourse().getDayOfWeek2() != null) {
            day2.setSelection(Course.getSelectedCourse().getDayOfWeek2().getValue());
            Date date = Course.getSelectedCourse().getStartTime2();
            String time = formatter.format(date);
            time2.setText(time);
        }

        if (Course.getSelectedCourse().getDuration() != 0) {
            duration.setText(String.valueOf(Course.getSelectedCourse().getDuration()));
        }

        if (Course.getSelectedCourse().getCourseCapacity() != 0) {
            capacity.setText(String.valueOf(Course.getSelectedCourse().getCourseCapacity()));
        }

        if (Course.getSelectedCourse().getCourseDescription() != null) {
            description.setText(Course.getSelectedCourse().getCourseDescription());
        }
        disableInputs();
    }

    private void disableInputs() {
        day1.setEnabled(false);
        day2.setEnabled(false);
        time1.setEnabled(false);
        time2.setEnabled(false);
        duration.setEnabled(false);
        capacity.setEnabled(false);
        description.setEnabled(false);
    }

    private void toggleButtons() {
        User user = User.getCurrentUser().getUser(StudentManageCourseActivity.this).get(0);
        if (user.isEnrolled(Course.getSelectedCourse())) {
            enrol.setEnabled(false);
            unenrol.setEnabled(true);
        } else {
            enrol.setEnabled(true);
            unenrol.setEnabled(false);
        }
    }
}