package com.example.coursebookingapp.user;

import android.content.Context;

import com.example.coursebookingapp.activities.LoginActivity;
import com.example.coursebookingapp.activities.ManageStudentsActivity;
import com.example.coursebookingapp.data.DatabaseHandler;

import java.util.List;

public class User {
    private static User currentUser;
    private int id;
    private String username;
    private String password;
    private String role;

    public User() {
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdministrator() {
        return this.getRole().equals("Administrator");
    }

    public boolean isInstructor() {
        return this.getRole().equals("Instructor");
    }

    public boolean isStudent() {
        return this.getRole().equals("Student");
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return getRole() + ": " + getUsername();
    }

    public boolean checkIfUserExists(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        if (databaseHandler.userExists(this)) {
            databaseHandler.close();
            return true;
        }

        databaseHandler.close();
        return false;
    }

    public boolean addUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        boolean addedSuccessfully = databaseHandler.addUser(this);
        databaseHandler.close();
        return addedSuccessfully;
    }

    public static List<User> getAllInstructors(Context context, String search) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<User> instructorsSearched = databaseHandler.allInstructors(search);
        databaseHandler.close();
        return instructorsSearched;
    }

    public static List<User> getAllStudents(Context context, String search) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<User> instructorsSearched = databaseHandler.allStudents(search);
        databaseHandler.close();
        return instructorsSearched;
    }

    public boolean deleteUser(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        boolean deleted = databaseHandler.deleteUser(this);
        databaseHandler.close();
        return deleted;
    }
}
