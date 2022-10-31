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
    DatabaseHandler dbHandler = new DatabaseHandler(ManageInstructorsActivity.this);
    ArrayAdapter instructorsArrayAdapter;
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
        setContentView(R.layout.activity_manage_instructors);

        logout = findViewById(R.id.logout3);
        search = findViewById(R.id.search2);
        searchBox = findViewById(R.id.searchStudents2);
        list = findViewById(R.id.listOfStudents);
        welcome = findViewById(R.id.welcome2);
        back = findViewById(R.id.backBtn2);

        generateUser();
        welcome.setText("Welcome, " + user.getUsername() + "\n" +
                " (" +user.getRole()+ ")");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(ManageInstructorsActivity.this);
                searchCharacters = searchBox.getText().toString();
                updateListView(dbHandler);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userDeleted = (User) parent.getItemAtPosition(position);
                dbHandler.deleteUser(userDeleted);
                Toast.makeText(ManageInstructorsActivity.this, "Instructor Deleted: " + userDeleted.getUsername() + ".", Toast.LENGTH_SHORT).show();
                updateListView(dbHandler);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });
    }

    private void startLoginActivity() {
        Intent loginActivity = new Intent(ManageInstructorsActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    private void startMainActivity() {
        Intent mainActivity = new Intent(ManageInstructorsActivity.this, MainActivity.class);
        mainActivity.putExtra("Username", user.getUsername());
        mainActivity.putExtra("Password", user.getPassword());
        mainActivity.putExtra("Role", user.getRole());
        startActivity(mainActivity);
    }

    private void updateListView(DatabaseHandler dbHandler) {
        List<User> instructorsWithSearchCharacters = dbHandler.allInstructors(searchCharacters);
        instructorsArrayAdapter = new ArrayAdapter<User>(ManageInstructorsActivity.this, android.R.layout.simple_list_item_1, instructorsWithSearchCharacters);
        list.setAdapter(instructorsArrayAdapter);
    }


    private void generateUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            user = new User(-1, bundle.getString("Username"), bundle.getString("Password"), bundle.getString("Role"));

        }
    }
}