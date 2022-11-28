package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.course.CourseCode;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class SearchCoursesActivity extends AppCompatActivity {
    ArrayAdapter coursesArrayAdapter;
    Button search, clear, viewAll;
    Button back, logout;
    EditText courseName, faculty, code;
    Spinner daysOfWeek;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_courses);

        assignInputs();
        setPermission();
        generateDaysOfWeekDropdown();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = new Course();
                CourseCode courseCode;
                List<Course> coursesFound = new ArrayList<>();

                String facultyInput = faculty.getText().toString();
                String codeInput = code.getText().toString();
                String courseNameInput = courseName.getText().toString();
                String daysOfWeekInput = "";

                if (daysOfWeek.isEnabled()) {
                    daysOfWeekInput = daysOfWeek.getSelectedItem().toString();
                }

                if (checkIfFacultyAndCodeEmpty() && checkIfCourseNameEmpty() && checkIfDaysOfWeekEmpty()) {
                    printMessage("Enter a Course Code, Course Name, or Day of Week.");
                } else if (checkIfFacultyAndCodeEmpty() && checkIfCourseNameEmpty() && !checkIfDaysOfWeekEmpty()){
                    course.setDayOfWeek1(DayOfWeek.valueOf(daysOfWeekInput));
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 1);
                } else if (checkIfFacultyAndCodeEmpty() && !checkIfCourseNameEmpty() && checkIfDaysOfWeekEmpty()) {
                    course.setCourseName(courseNameInput);
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 2);
                } else if (checkIfFacultyAndCodeEmpty() && !checkIfCourseNameEmpty() && !checkIfDaysOfWeekEmpty()) {
                    course.setCourseName(courseNameInput);
                    course.setDayOfWeek1(DayOfWeek.valueOf(daysOfWeekInput));
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 3);
                } else if (!checkIfFacultyAndCodeEmpty() && checkIfCourseNameEmpty() && checkIfDaysOfWeekEmpty()) {
                    courseCode = new CourseCode(facultyInput, Integer.parseInt(codeInput));
                    course.setCourseCode(courseCode);
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 4);
                } else if (!checkIfFacultyAndCodeEmpty() && checkIfCourseNameEmpty() && !checkIfDaysOfWeekEmpty()) {
                    courseCode = new CourseCode(facultyInput, Integer.parseInt(codeInput));
                    course.setCourseCode(courseCode);
                    course.setDayOfWeek1(DayOfWeek.valueOf(daysOfWeekInput));
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 5);
                } else if (!checkIfFacultyAndCodeEmpty() && !checkIfCourseNameEmpty() && checkIfDaysOfWeekEmpty()) {
                    courseCode = new CourseCode(facultyInput, Integer.parseInt(codeInput));
                    course.setCourseCode(courseCode);
                    course.setCourseName(courseNameInput);
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 6);
                } else if (!checkIfFacultyAndCodeEmpty() && !checkIfCourseNameEmpty() && !checkIfDaysOfWeekEmpty()) {
                    courseCode = new CourseCode(facultyInput, Integer.parseInt(codeInput));
                    course.setCourseCode(courseCode);
                    course.setCourseName(courseNameInput);
                    course.setDayOfWeek1(DayOfWeek.valueOf(daysOfWeekInput));
                    coursesFound = course.searchForCourses(SearchCoursesActivity.this, 7);

                }

                updateListView(coursesFound);

                if (coursesFound.size() == 0) {
                    printMessage("None found.");
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course courseSelected = (Course) parent.getItemAtPosition(position);
                Course.setSelectedCourse(courseSelected);
                printMessage("Editing Course: " + Course.getSelectedCourse());

                if (User.getCurrentUser().isAdministrator()){
                    clearAll();
                    startNewActivity(ManageCourseActivity.class);
                } else if (User.getCurrentUser().isInstructor()) {
                    // Update Code with Instructor Information
                    clearAll();
                    startNewActivity(InstructorManageCourseActivity.class);
                } else if (User.getCurrentUser().isStudent()) {
                    clearAll();
                    // Open Activity for User enrolling in course
                    startNewActivity(StudentManageCourseActivity.class);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
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

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                List<Course> allCourses = Course.findAllCourses(SearchCoursesActivity.this);
                updateListView(allCourses);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    }

    private void clearAll() {
        updateListView(new ArrayList<>());
        courseName.setText("");
        faculty.setText("");
        code.setText("");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(SearchCoursesActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(SearchCoursesActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        search = findViewById(R.id.searchCourseInstructor);
        clear = findViewById(R.id.clear);
        viewAll = findViewById(R.id.viewAll);
        back = findViewById(R.id.backBtnSearchPage);
        logout = findViewById(R.id.logoutBtnSearchPage);
        courseName = findViewById(R.id.et_searchCourse);
        code = findViewById(R.id.courseCode);
        faculty = findViewById(R.id.facultyCode);
        daysOfWeek = findViewById(R.id.dayOfTheWeek);
        list = findViewById(R.id.lv_courseSearchResult);
    }

    private void setPermission() {
        if (User.getCurrentUser().isAdministrator()) {
            setAdminPermission();
        } else if (User.getCurrentUser().isInstructor()) {
            setInstructorPermission();
        }
    }

    private void setAdminPermission() {
        daysOfWeek.setEnabled(false);
        courseName.setEnabled(false);
    }

    private void setInstructorPermission(){
        daysOfWeek.setEnabled(false);
    }

    private void generateDaysOfWeekDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daysOfWeek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        daysOfWeek.setAdapter(adapter);
    }


    private boolean checkIfFacultyOrCodeEmpty() {
        return faculty.getText().toString().isEmpty() || code.getText().toString().isEmpty();
    }

    private boolean checkIfFacultyAndCodeEmpty() {
        return faculty.getText().toString().trim().length() == 0 || code.getText().toString().trim().length() == 0;
    }

    private boolean checkIfCourseNameEmpty() {
        return courseName.getText().toString().trim().length() == 0;
    }

    private boolean checkIfDaysOfWeekEmpty() {
        if (!daysOfWeek.isEnabled()) {
            return true;
        }
        return daysOfWeek.getSelectedItem().toString().isEmpty();
    }

    private void updateListView(List<Course> courses) {

        coursesArrayAdapter = new ArrayAdapter<Course>(SearchCoursesActivity.this, android.R.layout.simple_list_item_1, courses);
        list.setAdapter(coursesArrayAdapter);
    }
}