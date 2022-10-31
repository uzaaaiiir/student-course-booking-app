package com.example.coursebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    public static final String ALREADY_EXISTS = "User already exists.";
    public final static String UNSUCCESSFUL_LOGIN = "Incorrect Username or Password.";
    public static final String ADMINISTRATOR_NOT_ALLOWED = "Cannot make account as an Administrator.";
    public static final String SUCCESSFUL_SIGN_UP = "Successful sign up. Enter details to Login.";
    public static final String ERROR = "Error. Cannot Sign-up.";
    EditText username, password;
    Button login, signUp;
    Spinner roleDropdown;

    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Reference EditTexts and Button
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        roleDropdown = findViewById(R.id.role_dropdown);

        generateLoginDropdown();
        databaseHandler = new DatabaseHandler(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user;
                try {
                    DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
                    user = new User(-1, username.getText().toString(), password.getText().toString(), roleDropdown.getSelectedItem().toString());
                    if (databaseHandler.userExists(user)) {
                        Toast.makeText(LoginActivity.this, "Success. Logging in as " + user.getRole().toString() + ".", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        password.setText("");
                        startMainActivity(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, UNSUCCESSFUL_LOGIN, Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User (-1, username.getText().toString(), password.getText().toString(), roleDropdown.getSelectedItem().toString());
                if (databaseHandler.userExists(user)) {
                    Toast.makeText(LoginActivity.this, ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                } else if (user.getRole().equals("Administrator")) {
                    Toast.makeText(LoginActivity.this, ADMINISTRATOR_NOT_ALLOWED, Toast.LENGTH_SHORT).show();
                } else if (databaseHandler.addUser(user)) {
                    Toast.makeText(LoginActivity.this, SUCCESSFUL_SIGN_UP, Toast.LENGTH_SHORT).show();
                    password.setText("");
                    username.setText("");
                } else {
                    Toast.makeText(LoginActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void startMainActivity(User user) {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        mainActivity.putExtra("Username", user.getUsername());
        mainActivity.putExtra("Password", user.getPassword());
        mainActivity.putExtra("Role", user.getRole());
        startActivity(mainActivity);
    }

    private void generateLoginDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        roleDropdown.setAdapter(adapter);
    }
}