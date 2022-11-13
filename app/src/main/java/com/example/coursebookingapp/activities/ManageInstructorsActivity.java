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

public class ManageInstructorsActivity extends AppCompatActivity {
    ArrayAdapter instructorsArrayAdapter;
    Button logout, search, back;
    ListView list;
    EditText searchBox;
    TextView welcome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_instructors);

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
                userDeleted.deleteUser(ManageInstructorsActivity.this);
                printMessage("Instructor Deleted: " + userDeleted);
                updateListView("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewActivity(AdminMainActivity.class); }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(LoginActivity.class);
            }
        });
    }

    private void printMessage(String message) {
        Toast.makeText(ManageInstructorsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void assignInputs() {
        logout = findViewById(R.id.logout3);
        search = findViewById(R.id.search2);
        searchBox = findViewById(R.id.searchStudents2);
        list = findViewById(R.id.listOfStudents);
        welcome = findViewById(R.id.welcome2);
        back = findViewById(R.id.backBtn2);
    }

    private void setWelcomeText() {
        welcome.setText("Welcome, " + User.getCurrentUser().getUsername() + "\n" +
                " (" + User.getCurrentUser().getRole() + ")");
    }

    private void startNewActivity(Class activityToStart) {
        Intent activity = new Intent(ManageInstructorsActivity.this, activityToStart);
        startActivity(activity);
    }

    private void updateListView(String searchCharacters) {
        List<User> instructorsWithSearchCharacters = User.getAllInstructors(ManageInstructorsActivity.this ,searchCharacters);
        instructorsArrayAdapter = new ArrayAdapter<User>(ManageInstructorsActivity.this, android.R.layout.simple_list_item_1, instructorsWithSearchCharacters);
        list.setAdapter(instructorsArrayAdapter);
    }

}