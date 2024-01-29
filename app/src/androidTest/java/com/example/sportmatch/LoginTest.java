package com.example.sportmatch;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {


    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    private ActivityScenario<LoginActivity> activityScenario;

    @Before
    public void setUp() {
        Intents.init();
        activityScenario = activityScenarioRule.getScenario();
    }

    @After
    public void tearDown() {
        Intents.release();
        activityScenario.close();
    }

    @Test
    public void testLoginComponentsDisplayed() {
        Espresso.onView(withId(R.id.activity_main_usernameEditText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.activity_main_passwordEditText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithValidCredentialsNavigatesToBottomNavActivity() throws InterruptedException {
        // Launch the activity
        activityScenario = activityScenarioRule.getScenario();

        // Enter valid credentials
        Espresso.onView(withId(R.id.activity_main_usernameEditText))
                .perform(ViewActions.typeText("buna@yahoo.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.activity_main_passwordEditText))
                .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        try {
            // Log information for debugging
            Log.d("TestDebug", "Checking for intended intent");
            Intents.intended(IntentMatchers.hasComponent(BottomNavActivity.class.getName()));
            Log.d("TestDebug", "Intended intent check completed");
        } catch (Exception e) {
            // Log or handle the exception
            Log.e("TestDebug", "Error during intent verification: " + e.getMessage(), e);

            // If needed, rethrow the exception to propagate it further
            throw e;
        }

    }
}