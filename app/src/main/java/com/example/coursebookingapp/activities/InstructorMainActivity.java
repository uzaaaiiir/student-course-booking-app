package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.user.User;

import org.w3c.dom.Text;

public class InstructorMainActivity extends AppCompatActivity {
    TextView welcome;
    Button searchAndEdit, logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        assignInputs();
        setWelcomeText();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(LoginActivity.class); }
        });

        searchAndEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(SearchCoursesActivity.class); }
        });
    }

    private void assignInputs() {
        welcome = findViewById(R.id.welcomeInstructorMain);
        searchAndEdit = findViewById(R.id.searchAndEditInstructorMain);
        logout = findViewById(R.id.logoutBtnInstructorMain);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(InstructorMainActivity.this, activityToStart);
        startActivity(activity);
    }
}