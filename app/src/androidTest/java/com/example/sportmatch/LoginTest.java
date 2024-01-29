package com.example.sportmatch;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Before
    public void setUp() {
        // initialize FirebaseAuth for testing
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
    }

    @Test
    public void loginWithValidCredentials() {

        ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.activity_main_usernameEditText))
                .perform(ViewActions.typeText("buna@yahoo.com"));
        Espresso.onView(withId(R.id.activity_main_passwordEditText))
                .perform(ViewActions.typeText("123456"));

        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());


        intended(hasComponent(BottomNavActivity.class.getName()));
    }

    @After
    public void tearDown() {
        // sign out after testing
        FirebaseAuth.getInstance().signOut();
    }
}
