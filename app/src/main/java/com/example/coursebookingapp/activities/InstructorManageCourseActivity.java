package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.course.Course;
import com.example.coursebookingapp.user.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

public class InstructorManageCourseActivity extends AppCompatActivity {
    Button save, logout, home, back, viewStudents;
    EditText time1, time2, duration, capacity, description;
    Spinner day1, day2;
    Switch assign;
    TextView courseCode, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_manage_course);

        assignInputs();
        setCourseCodeAndCourseNameUI();
        generateDayDropdown();
        setCourseDetails();
        managePermissions();

        assign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (assign.isChecked()) {
                    Course.getSelectedCourse().setCourseInstructor(User.getCurrentUser().getUsername());
                    printMessage(Course.getSelectedCourse().updateCourse(InstructorManageCourseActivity.this) ? "Assigned as Instructor." : "Unable to assign as Instructor.");
                    viewStudents.setEnabled(true);
                } else {
                    Course.getSelectedCourse().emptyAllFields();
                    viewStudents.setEnabled(false);
                    if (Course.getSelectedCourse().updateCourse(InstructorManageCourseActivity.this)) {
                        emptyAllUIFields();
                        printMessage("Unassigned as Instructor.");
                        startNewActivity(SearchCoursesActivity.class);
                    } else {
                        printMessage("Unable to un-assign as Instructor.");
                    }
                }
                managePermissions();
            }
        });

        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(ViewEnrolledStudentsActivity.class);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setUIValuesToCourse();
                    if (Course.getSelectedCourse().updateCourse(InstructorManageCourseActivity.this)) {
                        printMessage("Successfully saved changes.");
                        startNewActivity(SearchCoursesActivity.class);
                    } else {
                        printMessage("Unable to save changes.");
                    }
                } catch (ParseException e) {
                    printMessage("Unable to save changes. Make sure to fill fields properly.");
                    e.printStackTrace();
                }

            }
        });

        day1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (day1.getSelectedItem().toString().trim().length() == 0) {
                    time1.setEnabled(false);
                } else if (day1.getSelectedItem().toString().trim().length() != 0 && !userIsInstructor()){
                    time1.setEnabled(false);
                } else {
                    time1.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                time1.setEnabled(false);
            }
        });

        day2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (day2.getSelectedItem().toString().trim().length() == 0) {
                    time2.setEnabled(false);
                } else if (day2.getSelectedItem().toString().trim().length() != 0 && !userIsInstructor()){
                    time2.setEnabled(false);
                } else {
                    time2.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                time2.setEnabled(false);
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
                startNewActivity(InstructorMainActivity.class);
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
    }

    private void setUIValuesToCourse() throws ParseException {

        DateFormat formatter = new SimpleDateFormat("hh:mm");

        if (day1.getSelectedItem().toString().trim().length() != 0) {
            String time1Input = time1.getText().toString();
            Date date1 = formatter.parse(time1Input);
            DayOfWeek dayOfWeek1 = DayOfWeek.valueOf(day1.getSelectedItem().toString());
            Course.getSelectedCourse().setStartTime1(date1);
            Course.getSelectedCourse().setDayOfWeek1(dayOfWeek1);
        }

        if (day2.getSelectedItem().toString().trim().length() != 0) {
            String time2Input = time2.getText().toString();
            Date date2 = formatter.parse(time2Input);
            DayOfWeek dayOfWeek2 = DayOfWeek.valueOf(day2.getSelectedItem().toString());
            Course.getSelectedCourse().setStartTime2(date2);
            Course.getSelectedCourse().setDayOfWeek2(dayOfWeek2);
        }

        if (capacity.getText().toString().trim().length() != 0) {
            Course.getSelectedCourse().setCourseCapacity(Integer.parseInt(capacity.getText().toString()));
        }

        if (duration.getText().toString().trim().length() != 0) {
            Course.getSelectedCourse().setDuration(Integer.parseInt(duration.getText().toString()));
        }
        Course.getSelectedCourse().setCourseDescription(description.getText().toString());
    }

    private void emptyAllUIFields() {
        generateDayDropdown();
        time1.setText("");
        time2.setText("");
        duration.setText("");
        capacity.setText("");
        description.setText("");
    }

    private void managePermissions() {
        if (Course.getSelectedCourse().getCourseInstructor() == null) {
            enableToggle();
            disableInputs();
            disableBtn();
            viewStudents.setEnabled(false);
            assign.setChecked(false);
        } else if (userIsInstructor()) {
            enableInputs();
            enableToggle();
            enableBtn();
            viewStudents.setEnabled(true);
            assign.setChecked(true);
            assign.setText("You are the Instructor.");
        } else if (!userIsInstructor()) {
            disableInputs();
            disableToggle();
            assign.setChecked(false);
            disableBtn();
            viewStudents.setEnabled(false);
            assign.setText("Course already assigned to " + Course.getSelectedCourse().getCourseInstructor() + ".");
        }

    }

    private boolean userIsInstructor() {
        return Course.getSelectedCourse().getCourseInstructor().equals(User.getCurrentUser().getUsername());
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

    private void enableInputs() {
        day1.setEnabled(true);
        day2.setEnabled(true);
        time1.setEnabled(false);
        time2.setEnabled(false);
        duration.setEnabled(true);
        capacity.setEnabled(true);
        description.setEnabled(true);
    }

    private void disableToggle() {
        assign.setEnabled(false);
    }

    private void enableToggle() {
        assign.setEnabled(true);
    }

    private void disableBtn() {
        save.setEnabled(false);
    }

    private void enableBtn() {
        save.setEnabled(true);
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(InstructorManageCourseActivity.this, activityToStart);
        startActivity(activity);
    }

    private void printMessage(String message) {
        Toast.makeText(InstructorManageCourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        save = findViewById(R.id.saveChangesInstructor);
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
        assign = findViewById(R.id.assignToSelf);
        viewStudents = findViewById(R.id.viewEnrolledStudents);
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
}