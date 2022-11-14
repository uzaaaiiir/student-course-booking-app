package com.example.coursebookingapp.user;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursebookingapp.activities.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserTest {
    User actionClass;
    Context context;
    SQLiteOpenHelper helper;

    @Before
    public void setUp() {
        actionClass = new User();
        context = Mockito.mock(Context.class);
        helper = Mockito.mock(SQLiteOpenHelper.class);
        actionClass.setUsername("Test Username");
        actionClass.setPassword("Test Password");
    }

    @Test
    public void givenUserIsAdministrator_WhenIsAdminCalled_TrueReturned() {
        actionClass.setRole("Administrator");

        boolean result = actionClass.isAdministrator();

        assertEquals(true, result);
    }

    @Test
    public void givenUserIsNotAdministrator_WhenIsAdminCalled_FalseReturned() {
        actionClass.setRole("Student");

        boolean result = actionClass.isAdministrator();

        assertEquals(false, result);
    }

    @Test
    public void givenUserIsInstructor_WhenIsInstructorCalled_TrueReturned() {
        actionClass.setRole("Instructor");

        boolean result = actionClass.isInstructor();

        assertEquals(true, result);
    }

    @Test
    public void givenUserIsNotInstructor_WhenIsInstructorCalled_FalseReturned() {
        actionClass.setRole("Administrator");

        boolean result = actionClass.isInstructor();

        assertEquals(false, result);
    }

    @Test
    public void givenUserIsStudent_WhenIsStudentCalled_TrueReturned() {
        actionClass.setRole("Student");

        boolean result = actionClass.isStudent();

        assertEquals(true, result);
    }

    @Test
    public void givenUserIsNotStudent_WhenIsStudentCalled_FalseReturned() {
        actionClass.setRole("Administrator");

        boolean result = actionClass.isStudent();

        assertEquals(false, result);
    }
}