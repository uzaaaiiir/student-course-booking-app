package com.example.coursebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {
    DatabaseHandler dbHandler = new DatabaseHandler(ManageStudentsActivity.this);
    ArrayAdapter studentsArrayAdapter;
    User user;
    Button logout, search, back;
    ListView list;
    EditText searchBox;
    TextView welcome;
    String searchCharacters;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        logout = findViewById(R.id.logout);
        search = findViewById(R.id.search);
        searchBox = findViewById(R.id.searchStudents);
        list = findViewById(R.id.listOfStudents);
        welcome = findViewById(R.id.welcome);
        back = findViewById(R.id.backBtn);

        generateUser();
        welcome.setText("Welcome, " + user.getUsername() + "\n" +
                " (" +user.getRole()+ ")");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(ManageStudentsActivity.this);
                searchCharacters = searchBox.getText().toString();
                updateListView(dbHandler);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userDeleted = (User) parent.getItemAtPosition(position);
                dbHandler.deleteUser(userDeleted);
                Toast.makeText(ManageStudentsActivity.this, "Student Deleted: " + userDeleted.getUsername(), Toast.LENGTH_SHORT).show();
                updateListView(dbHandler);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

    }

    private void startMainActivity() {
        Intent mainActivity = new Intent(ManageStudentsActivity.this, MainActivity.class);
        mainActivity.putExtra("Username", user.getUsername());
        mainActivity.putExtra("Password", user.getPassword());
        mainActivity.putExtra("Role", user.getRole());
        startActivity(mainActivity);
    }

    private void updateListView(DatabaseHandler dbHandler) {
        List<User> studentsWithSearchCharacters = dbHandler.allStudents(searchCharacters);
        studentsArrayAdapter = new ArrayAdapter<User>(ManageStudentsActivity.this, android.R.layout.simple_list_item_1, studentsWithSearchCharacters);
        list.setAdapter(studentsArrayAdapter);
    }


    private void generateUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            user = new User(-1, bundle.getString("Username"), bundle.getString("Password"), bundle.getString("Role"));

        }
    }
}