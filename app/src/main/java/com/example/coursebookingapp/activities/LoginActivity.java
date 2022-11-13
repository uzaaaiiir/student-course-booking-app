package com.example.coursebookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.coursebookingapp.user.Administrator;
import com.example.coursebookingapp.R;
import com.example.coursebookingapp.user.User;

public class LoginActivity extends AppCompatActivity {

    public static final String ALREADY_EXISTS = "User already exists.";
    public final static String UNSUCCESSFUL_LOGIN = "Incorrect Username or Password.";
    public static final String ADMINISTRATOR_NOT_ALLOWED = "Cannot make account as an Administrator.";
    public static final String SUCCESSFUL_SIGN_UP = "Successful sign up. Enter details to Login.";
    public static final String ERROR = "Error. Cannot Sign-up.";
    public static final String MISSING_USERNAME_OR_PASSWORD = "Must enter both Username and Password.";
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect username or password. Try again.";
    EditText username, password;
    Button login, signUp;
    Spinner roleDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        assignInputs();
        User root = new Administrator(-1, "admin", "admin123");
        generateLoginDropdown();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user;
                try {
                    user = new User(-1, username.getText().toString(), password.getText().toString(), roleDropdown.getSelectedItem().toString());
                    if (user.checkIfUserExists(LoginActivity.this)) {
                        printMessage("Success. Logging in as " + user.getRole());
                        emptyUsernameAndPasswordFields(username, password);
                        User.setCurrentUser(user);

                        if (user.isAdministrator()) {
                            startNewActivity(AdminMainActivity.class);
                        } else if (user.isInstructor()) {
                            startNewActivity(InstructorMainActivity.class);
                        } else if (user.isStudent()) {
                            startNewActivity(StudentMainActivity.class);
                        }
                    } else {
                        printMessage(INCORRECT_USERNAME_OR_PASSWORD);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    printMessage(UNSUCCESSFUL_LOGIN);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameOrPasswordEmpty()) {
                    printMessage(MISSING_USERNAME_OR_PASSWORD);
                }
                else {
                    User user = new User(-1, username.getText().toString(), password.getText().toString(), roleDropdown.getSelectedItem().toString());
                    if (user.checkIfUserExists(LoginActivity.this)) {
                        printMessage(ALREADY_EXISTS);
                    } else if (user.isAdministrator()) {
                        printMessage(ADMINISTRATOR_NOT_ALLOWED);
                    } else if (user.addUser(LoginActivity.this)) {
                        printMessage(SUCCESSFUL_SIGN_UP);
                        emptyUsernameAndPasswordFields(password, username);
                    } else {
                        printMessage(ERROR);
                    }
                }
            }
        });
    }

    private void assignInputs() {
        // Reference EditTexts and Button
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        roleDropdown = findViewById(R.id.role_dropdown);
    }

    private void startNewActivity(Class classToBegin) {
        Intent activity = new Intent(LoginActivity.this, classToBegin);
        startActivity(activity);
    }

    private void generateLoginDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        roleDropdown.setAdapter(adapter);
    }

    private void printMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void emptyUsernameAndPasswordFields(EditText password, EditText username) {
        password.setText("");
        username.setText("");
    }

    private boolean usernameOrPasswordEmpty() {
        return username.getText().toString().isEmpty() || password.getText().toString().isEmpty();
    }
}
