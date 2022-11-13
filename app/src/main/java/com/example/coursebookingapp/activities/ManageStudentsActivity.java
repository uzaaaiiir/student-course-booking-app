package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursebookingapp.R;
import com.example.coursebookingapp.data.DatabaseHandler;
import com.example.coursebookingapp.user.User;

import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {
    ArrayAdapter studentsArrayAdapter;
    Button logout, search, back;
    ListView list;
    EditText searchBox;
    TextView welcome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        assignInputs();
        setWelcomeText();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchCharacters = searchBox.getText().toString();
                updateListView(searchCharacters);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userDeleted = (User) parent.getItemAtPosition(position);
                userDeleted.deleteUser(ManageStudentsActivity.this);
                printMessage("Student Deleted: " + userDeleted.getUsername());
                updateListView("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(AdminMainActivity.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });

    }

    private void assignInputs() {
        logout = findViewById(R.id.logout2);
        search = findViewById(R.id.search);
        searchBox = findViewById(R.id.searchStudents);
        list = findViewById(R.id.listOfStudents);
        welcome = findViewById(R.id.welcome);
        back = findViewById(R.id.backBtn);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void printMessage(String message) {
        Toast.makeText(ManageStudentsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(ManageStudentsActivity.this, activityToStart);
        startActivity(activity);
    }

    private void updateListView(String searchCharacters) {
        List<User> studentsWithSearchCharacters = User.getAllStudents(ManageStudentsActivity.this ,searchCharacters);
        studentsArrayAdapter = new ArrayAdapter<User>(ManageStudentsActivity.this, android.R.layout.simple_list_item_1, studentsWithSearchCharacters);
        list.setAdapter(studentsArrayAdapter);
    }
}