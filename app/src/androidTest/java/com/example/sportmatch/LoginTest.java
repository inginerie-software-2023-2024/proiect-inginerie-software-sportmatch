package com.example.sportmatch;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginComponentsDisplayed() {
        // Check if login components are displayed
        Espresso.onView(withId(R.id.activity_main_usernameEditText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.activity_main_passwordEditText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithValidCredentialsNavigatesToBottomNavActivity() {
        // Enter valid credentials
        Espresso.onView(withId(R.id.activity_main_usernameEditText)).perform(ViewActions.typeText("buna@yahoo.com"));
        Espresso.onView(withId(R.id.activity_main_passwordEditText)).perform(ViewActions.typeText("123456"));

        // Click the login button
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Check if the BottomNavActivity is launched
        intended(hasComponent(BottomNavActivity.class.getName()));
    }
}
