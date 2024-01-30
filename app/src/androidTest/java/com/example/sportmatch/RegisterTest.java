package com.example.sportmatch;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private ActivityScenario<MainActivity> activityScenario;
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
    public void testLoginWithValidCredentialsNavigatesToBottomNavActivity() throws InterruptedException {
        // Launch the activity
        activityScenario = activityScenarioRule.getScenario();
        Espresso.onView(withId(R.id.backG)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.hometext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.bottons)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.googleLogo)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.button_register)).perform(ViewActions.click());

        Intents.intended(IntentMatchers.hasComponent(RegisterActivity.class.getName()));

        // Enter valid credentials
        Espresso.onView(withId(R.id.FullNameInserted))
                .perform(ViewActions.typeText("Full name"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.UsernameInserted))
                .perform(ViewActions.typeText("FullName@yahoo.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.PasswordInserted))
                .perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.PasswordConfirmed))
                .perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());




    }


}